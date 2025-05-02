package xyz.devvydont.smprpg.items.base;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.IConsumable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.FoodUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for every single item in the game. All items and blueprints will inherit from this
 * Can be broken into two major chunks:
 * - VanillaItem
 * - CustomItem
 */
public abstract class SMPItemBlueprint {

    protected ItemService itemService;

    public SMPItemBlueprint(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Determine what type of item this is.
     */
    public abstract ItemClassification getItemClassification();

    /**
     * Extract rarity from the item. We do this to check if rarity has been upgraded. If we don't find anything special,
     * we use getDefaultRarity().
     */
    public abstract ItemRarity getRarity(ItemStack item);

    /**
     * The rarity that this item will start at. Does not guarantee that the rarity will always be this, however.
     * (Rarity can be changed via upgrades on an item stack, this is simply for generating new items.)
     */
    public abstract ItemRarity getDefaultRarity();

    /**
     * Determines the string that this item will be identified by when using custom model data.
     * @return A string to be used for resource pack creation.
     */
    public abstract String getCustomModelDataIdentifier();


    /**
     * Given item meta, determine how this item's display name should look
     */
    public Component getNameComponent(ItemStack item) {
        return getRarity(item).applyDecoration(ComponentUtils.create(getReforgePrefix(item) + getItemName(item))).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Retrieves the currently applied to reforge singleton instance contained on this reforge.
     * If this item doesn't have a reforge, null is returned
     *
     * @param item ItemMeta contained on an item stack
     * @return a Reforge singleton if the ItemMeta has a reforge type stored in it
     */
    @Nullable
    public ReforgeBase getReforge(ItemStack item) {
        return itemService.getReforge(item);
    }

    /**
     * Retrieves the currently applied to reforge type contained on this reforge.
     * If this item doesn't have a reforge, null is returned
     *
     * @param item ItemMeta contained on an item stack
     * @return a ReforgeType enum if the ItemMeta has a reforge type stored in it
     */
    @Nullable
    public ReforgeType getReforgeType(ItemStack item) {
        ReforgeBase reforge = getReforge(item);
        if (reforge == null)
            return null;
        return reforge.getType();
    }

    public boolean isReforged(ItemStack item) {
        return getReforge(item) != null;
    }

    /**
     * Gets the name of this item
     *
     * @param item The item.
     * @return The item name.
     */
    public abstract String getItemName(ItemStack item);

    /**
     * Gets the prefix to inject before the name of the item. Returns an empty string if no reforge is applied.
     *
     * @param item The item.
     * @return The reforge prefix.
     */
    public String getReforgePrefix(ItemStack item) {

        ReforgeBase reforge = getReforge(item);
        if (reforge == null)
            return "";

        return reforge.getType().display() + " ";
    }

    /**
     * Set the fake glow status of this item. If an item is enchantable, you should make this false.
     */
    public boolean wantFakeEnchantGlow() {
        return false;
    }

    /**
     * Determines if item given is an item belonging to this blueprint
     */
    public abstract boolean isItemOfType(ItemStack itemStack);

    /**
     * Determines if this item is custom. When this is false, we have a purely vanilla item.
     */
    public abstract boolean isCustom();

    /**
     * Generate an ItemStack of this blueprint.
     */
    public abstract ItemStack generate();


    public ItemStack generate(int amount) {
        var stack = generate();
        stack.setAmount(amount);
        return stack;
    }

    /**
     * The number of allowed enchantments on an item depends on the rarity of it.
     * Common = 1
     * Uncommon = 3
     * Rare = 5
     * Epic = 7
     * Legendary = 9
     * Mythic = 11
     * Divine = 13
     * Transcendent = 15
     * Special = 17
     *
     * @return How many enchants this item can have.
     */
    public int getMaxAllowedEnchantments(ItemStack item) {
        int rarityScore = getRarity(item).ordinal();
        return rarityScore * 2 + 1;
    }

    /**
     * Given item meta for this specific item type, return what to display for enchants
     */
    public List<Component> getEnchantsComponent(ItemStack item) {
        var meta = item.getItemMeta();
        List<Component> lines = new ArrayList<>();
        lines.add(ComponentUtils.EMPTY);
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(meta)) {
            Component name = enchantment.getEnchantment().displayName(enchantment.getLevel()).color(enchantment.getEnchantColor());
            name = ComponentUtils.create(Symbols.ENCHANTMENT + " ", NamedTextColor.LIGHT_PURPLE).append(name);
            lines.add(name);
            if (meta.getEnchants().size() <= 9)
                lines.add(enchantment.getDescription());
        }
        lines.add(ComponentUtils.create("Enchantments: " + meta.getEnchants().size() + "/" + getMaxAllowedEnchantments(item), NamedTextColor.DARK_GRAY));
        return lines;
    }

    public List<Component> getReforgeComponent(ItemStack item) {
        ReforgeBase reforge = getReforge(item);
        if (reforge == null)
            return List.of();

        List<Component> lines = new ArrayList<>();
        lines.add(ComponentUtils.EMPTY);
        lines.add(ComponentUtils.create(reforge.getType().display() + " Reforge", NamedTextColor.BLUE));
        lines.addAll(reforge.getDescription());
        return lines;
    }

    /**
     * Applies this blueprint's lore when we have a given itemstack.
     * For most items, the structure of lore will be like so:
     * -
     * - Enchants (if it has any)
     * -
     * - Description
     * -
     * [RARITY] ITEM
     */
    public void updateLore(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(itemService.renderItemStackLore(itemStack));
        itemStack.setItemMeta(meta);
    }

    /**
     * Called to set various components and attributes of this item, can be overidden for extra functionality
     */
    public void updateMeta(ItemMeta meta) {

        // Add fake glow (if wanted)
        if (wantFakeEnchantGlow())
            meta.setEnchantmentGlintOverride(true);

        // Set durability if desired, handle case where we set durability and the tool can support it
        if (this instanceof IBreakableEquipment breakable && meta instanceof Damageable damageable && breakable.getMaxDurability() > 0) {
            damageable.setMaxDamage(breakable.getMaxDurability());
        }
        // Handle case where we didn't define a durability (unbreakable)
        else if (meta instanceof Damageable) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        if (this instanceof IEdible edible) {
            FoodComponent food = FoodUtil.getVanillaFoodComponent(Material.APPLE);
            food.setNutrition(edible.getNutrition());
            food.setSaturation(edible.getSaturation());
            food.setCanAlwaysEat(edible.canAlwaysEat());
            meta.setFood(food);
        }

        // Set this item to be vulnerable to damage if it is custom no matter what.
        if (isCustom())
            meta.setDamageResistant(null);

        // Never allow vanilla attribute data to show.
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    public void updateVanillaFoodComponent(ItemStack item) {

        if (item.getType().asItemType() == null)
            return;

        if (!item.getType().asItemType().isEdible())
            return;

        FoodComponent food = FoodUtil.getVanillaFoodComponent(item.getType());
        item.editMeta(meta -> meta.setFood(food));
    }

    /**
     * Called to retrieve item meta off of an item stack, apply new updated item meta to it, and apply it
     */
    public void updateMeta(ItemStack itemStack) {

        // If this is a vanilla item, we need to hack the vanilla food component back on the item.
        if (!isCustom())
            updateVanillaFoodComponent(itemStack);

        // Apply custom model data.
        itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(this.getCustomModelDataIdentifier())
                .build()
        );

        // If this is a consumable, apply the consumable data to the item stack.
        if (this instanceof IConsumable consumable)
            itemStack.setData(DataComponentTypes.CONSUMABLE, consumable.getConsumableComponent());

        // If this item contains attributes, apply them.
        AttributeUtil.applyModifiers(this, itemStack);

        // Set name of item
        itemStack.editMeta(meta -> meta.displayName(getNameComponent(itemStack)));

        // Update the item meta
        ItemMeta meta = itemStack.getItemMeta();
        updateMeta(meta);
        itemStack.setItemMeta(meta);

        // Finally, after applying all updates re-render the lore of the item.
        updateLore(itemStack);
    }

}
