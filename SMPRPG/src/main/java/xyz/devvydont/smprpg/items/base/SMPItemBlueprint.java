package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.components.ToolComponent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public abstract Component getNameComponent(ItemMeta meta);

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
     * Given item meta for this specific item type, return what to display for enchants
     */
    public List<Component> getEnchantsComponent(ItemMeta meta) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.empty());
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments(meta)) {
            Component name = enchantment.getEnchantment().displayName(enchantment.getLevel()).color(enchantment.getEnchantColor());
            name = Component.text(Symbols.ENCHANTMENT + " ").color(NamedTextColor.LIGHT_PURPLE).append(name);
            lines.add(name);
            if (meta.getEnchants().size() <= 8)
                lines.add(enchantment.getDescription());
        }
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
        updateLore(meta);
        itemStack.setItemMeta(meta);
    }

    public void updateLore(ItemMeta meta) {

        List<Component> lore = new ArrayList<>();

        // Now, a description
        if (!getDescriptionComponent(meta).isEmpty()) {
            lore.add(Component.empty());
            lore.addAll(getDescriptionComponent(meta));
        }

        // First, enchants. Are we not forcing glow? Only display enchants when we are not forcing glow (and have some).
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (meta.hasEnchants())
            lore.addAll(getEnchantsComponent(meta));

        // Durability if the item has it
        if (meta instanceof Damageable damageable && damageable.hasMaxDamage()) {
            lore.add(Component.empty());
            lore.add(
                    ComponentUtil.getDefaultText("Durability: ")
                            .append(ComponentUtil.getColoredComponent(MinecraftStringUtils.formatNumber(damageable.getMaxDamage()-damageable.getDamage()), NamedTextColor.RED))
                            .append(ComponentUtil.getColoredComponent("/" + MinecraftStringUtils.formatNumber(damageable.getMaxDamage()), NamedTextColor.DARK_GRAY))
            );
        }

        // Now, rarity
        lore.add(Component.empty());
        lore.add(getRarity(meta).applyDecoration(Component.text(getRarity(meta).name() + " " + getItemClassification().name())).decoration(TextDecoration.BOLD, true));

        meta.lore(ChatUtil.cleanItalics(lore));
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
        if (this instanceof ToolBreakable breakable && meta instanceof Damageable damageable) {
            damageable.setMaxDamage(breakable.getMaxDurability());
        }
        // Handle case where we didn't define a durability (unbreakable)
        else if (meta instanceof Damageable) {
            meta.setUnbreakable(true);
        }

        // Update the lore to display
        updateLore(meta);
    }

    /**
     * Called to retrieve item meta off of an item stack, apply new updated item meta to it, and apply it
     */
    public void updateMeta(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        updateMeta(meta);
        itemStack.setItemMeta(meta);
    }

}
