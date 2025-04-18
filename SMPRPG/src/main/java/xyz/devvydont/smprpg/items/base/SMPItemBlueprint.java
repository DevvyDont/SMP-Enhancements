package xyz.devvydont.smprpg.items.base;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.registry.keys.tags.DamageTypeTagKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
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
     * Extract rarity from ItemMeta. We do this to check if rarity has been upgraded. If we don't find anything special,
     * we use getDefaultRarity()
     */
    public abstract ItemRarity getRarity(ItemMeta meta);

    /**
     * The rarity that this item will start at. Does not guarantee that the rarity will always be this, however.
     * (Rarity can be changed via upgrades on an item stack, this is simply for generating new items.)
     */
    public abstract ItemRarity getDefaultRarity();

    /**
     * Given item meta, determine how this item's display name should look
     */
    public Component getNameComponent(ItemMeta meta) {
        return getRarity(meta).applyDecoration(ComponentUtils.create(getReforgePrefix(meta) + getItemName(meta))).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Retrieves the currently applied reforge singleton instance contained on this reforge.
     * If this item doesn't have a reforge, null is returned
     *
     * @param meta ItemMeta contained on an item stack
     * @return a Reforge singleton if the ItemMeta has a reforge type stored in it
     */
    @Nullable
    public ReforgeBase getReforge(ItemMeta meta) {
        return itemService.getReforge(meta);
    }

    /**
     * Retrieves the currently applied reforge type contained on this reforge.
     * If this item doesn't have a reforge, null is returned
     *
     * @param meta ItemMeta contained on an item stack
     * @return a ReforgeType enum if the ItemMeta has a reforge type stored in it
     */
    @Nullable
    public ReforgeType getReforgeType(ItemMeta meta) {
        ReforgeBase reforge = getReforge(meta);
        if (reforge == null)
            return null;
        return reforge.getType();
    }

    public boolean isReforged(ItemMeta meta) {
        return getReforge(meta) != null;
    }

    /**
     * Gets the name of this item
     *
     * @param meta
     * @return
     */
    public abstract String getItemName(ItemMeta meta);

    /**
     * Gets the prefix to inject before the name of the item. Returns an empty string if no reforge is applied.
     *
     * @param meta
     * @return
     */
    public String getReforgePrefix(ItemMeta meta) {

        ReforgeBase reforge = getReforge(meta);
        if (reforge == null)
            return "";

        return reforge.getType().display() + " ";
    }

    /**
     * Set the fake glow status of this item. If an item is enchantable, you should make this false.
     */
    public abstract boolean wantFakeEnchantGlow();


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
     * @return
     */
    public int getMaxAllowedEnchantments(ItemMeta meta) {
        int rarityScore = getRarity(meta).ordinal();
        return rarityScore * 2 + 1;
    }

    /**
     * Given item meta for this specific item type, return what to display for enchants
     */
    public List<Component> getEnchantsComponent(ItemMeta meta) {
        List<Component> lines = new ArrayList<>();
        lines.add(ComponentUtils.EMPTY);
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(meta)) {
            Component name = enchantment.getEnchantment().displayName(enchantment.getLevel()).color(enchantment.getEnchantColor());
            name = ComponentUtils.create(Symbols.ENCHANTMENT + " ", NamedTextColor.LIGHT_PURPLE).append(name);
            lines.add(name);
            if (meta.getEnchants().size() <= 9)
                lines.add(enchantment.getDescription());
        }
        lines.add(ComponentUtils.create("Enchantments: " + meta.getEnchants().size() + "/" + getMaxAllowedEnchantments(meta), NamedTextColor.DARK_GRAY));
        return lines;
    }

    public List<Component> getFoodComponent(ItemMeta meta) {

        List<Component> lines = new ArrayList<>();
        lines.add(ComponentUtils.EMPTY);

        FoodComponent food = meta.getFood();

        // Nutrition
        int saturation = (int) food.getSaturation();
        lines.add(ComponentUtils.create(" - Nutrition: ").append(ComponentUtils.create("+" + food.getNutrition(), NamedTextColor.GREEN)));
        lines.add(ComponentUtils.create(" - Saturation: ").append(ComponentUtils.create("+" + saturation, NamedTextColor.GREEN)));

        // Potion effects todo: this has moved to the consumable API and is no longer apart of the food API, move this over when you get the chance.
//        for (FoodComponent.FoodEffect effect : food.getEffects()) {
//            String name = MinecraftStringUtils.getTitledString(effect.getEffect().getType().getKey().value());
//            Color rawColor = effect.getEffect().getType().getColor();
//            TextColor color = TextColor.color(rawColor.getRed(), rawColor.getGreen(), rawColor.getBlue());
//            int level = effect.getEffect().getAmplifier()+1;
//            int sec = effect.getEffect().getDuration() / 20;
//            String time = String.format(" (%d:%02d)", sec / 60, sec % 60);
//            String probability = String.format(" (%d%%)", (int)(effect.getProbability() * 100));
//            lines.add(ComponentUtils.create(" - Effect: ")
//                    .append(ComponentUtils.create(name + " " + level, color))
//                    .append(ComponentUtils.create(time, NamedTextColor.DARK_GRAY))
//                    .append(ComponentUtils.create(probability, NamedTextColor.DARK_GRAY)));
//        }

        return lines;

    }

    /**
     * Extract rarity from an ItemStack. By default, we just grab the rarity from ItemMeta. This can be overridden
     * for altered behavior.
     */
    public ItemRarity getRarity(ItemStack item) {
        return getRarity(item.getItemMeta());
    }

    /**
     * Description to provide about an item. Leave empty to not have one
     */
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        return new ArrayList<>();
    }

    public List<Component> getReforgeComponent(ItemMeta meta) {
        ReforgeBase reforge = getReforge(meta);
        if (reforge == null)
            return List.of();

        List<Component> lines = new ArrayList<>();
        lines.add(ComponentUtils.EMPTY);
        lines.add(ComponentUtils.create(reforge.getType().display() + " Reforge", NamedTextColor.BLUE));
        lines.addAll(reforge.getDescription());
        return lines;
    }

    /**
     * Additional info about an item that may not be as important.
     *
     * @param meta
     * @return
     */
    public List<Component> getFooterComponent(ItemMeta meta) {
        if (!isReforged(meta))
            return List.of();

        return getReforgeComponent(meta);
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

        // Set name of item
        meta.displayName(getNameComponent(meta));

        // Add fake glow (if wanted)
        if (wantFakeEnchantGlow())
            meta.setEnchantmentGlintOverride(true);

        // Set durability if desired, handle case where we set durability and the tool can support it
        if (this instanceof ToolBreakable breakable && meta instanceof Damageable damageable && breakable.getMaxDurability() > 0) {
            damageable.setMaxDamage(breakable.getMaxDurability());
        }
        // Handle case where we didn't define a durability (unbreakable)
        else if (meta instanceof Damageable) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        if (this instanceof Edible edible) {
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

        item.editMeta(meta -> {
            meta.setFood(food);
        });
    }

    /**
     * Called to retrieve item meta off of an item stack, apply new updated item meta to it, and apply it
     */
    public void updateMeta(ItemStack itemStack) {

        // If this is a vanilla item, we need to hack the vanilla food component back on the item.
        if (!isCustom())
            updateVanillaFoodComponent(itemStack);

        // If this is a consumable, apply the consumable data to the item stack.
        if (this instanceof IConsumable consumable)
            itemStack.setData(DataComponentTypes.CONSUMABLE, consumable.getConsumableComponent());

        // Update the item meta
        ItemMeta meta = itemStack.getItemMeta();
        updateMeta(meta);
        itemStack.setItemMeta(meta);

        // Finally, after applying all updates re-render the lore of the item.
        updateLore(itemStack);
    }

}
