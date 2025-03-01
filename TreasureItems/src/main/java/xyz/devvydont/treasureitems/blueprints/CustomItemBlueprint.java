package xyz.devvydont.treasureitems.blueprints;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.treasureitems.TreasureItems;
import xyz.devvydont.treasureitems.util.ComponentUtils;
import xyz.devvydont.treasureitems.util.FormatUtil;
import xyz.devvydont.treasureitems.util.PotentialEnchantmentWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public abstract class CustomItemBlueprint {

    /**
     * Return a map of enchantments to their min/max levels that this gear can roll and the chance of rolling it
     *
     * @return
     */
    public Map<Enchantment, PotentialEnchantmentWrapper> getAllowedEnchants() {
        return new HashMap<>();
    }

    public void randomlyEnchant(ItemStack item, float luckBoost) {

        for (Map.Entry<Enchantment, PotentialEnchantmentWrapper> entry : getAllowedEnchants().entrySet()) {

            // If we roll the enchantment
            if (Math.random() < entry.getValue().getChance() + luckBoost) {

                // Get a random level
                int level = (int) Math.floor(Math.random() * (entry.getValue().getMaxLevel() - entry.getValue().getMinLevel() + 1)) + entry.getValue().getMinLevel();
                if (luckBoost >= 1)
                    level = entry.getValue().getMaxLevel();

                // If the enchantment is already on the item, we want to take the higher level
                if (item.getEnchantmentLevel(entry.getKey()) > level)
                    level = item.getEnchantmentLevel(entry.getKey());

                // Add the enchantment
                item.addUnsafeEnchantment(entry.getKey(), level);

            }

        }

    }

    protected AttributeModifier generateAttributeModifier(Attribute attribute, EquipmentSlotGroup slotGroup, AttributeModifier.Operation operation, double amount) {

        // Dynamically generate a key to assign for this modifier. It must be unique to the item + slot + attribute combo.
        String keyName = getClass().getSimpleName() + attribute.toString() + slotGroup.toString();
        NamespacedKey key = new NamespacedKey(TreasureItems.getInstance(), keyName);

        return new AttributeModifier(key, amount, operation, slotGroup);
    }

    /**
     * When given an item of this blueprint, calculate a number from 1-100 that represents how good the item is
     *
     * @param itemStack
     * @return
     */
    public int calculateScore(ItemStack itemStack) {

        // If this item cannot be enchanted, it is a 100
        if (getAllowedEnchants().isEmpty())
            return 100;

        int totalLevelsAllowed = 0;
        int levelsRolled = 0;

        for (PotentialEnchantmentWrapper enchant : getAllowedEnchants().values()) {

            // Get the current level of the enchant, if it is 0 then skip
            int currentLevel = itemStack.getEnchantmentLevel(enchant.getEnchantment());
            if (currentLevel <= 0)
                continue;

            // Get the enchant level that is on the item, and figure out what percent of the max level it is
            // with the min level it could have been as the starting point
            int minPossibleLevel = enchant.getMinLevel();
            int levelRange = enchant.getMaxLevel() - minPossibleLevel + 1;

            totalLevelsAllowed += levelRange;
            levelsRolled += currentLevel - minPossibleLevel + 1;
        }

        float totalEnchantPercent = (float) levelsRolled / (float) totalLevelsAllowed;

        // Now we have a number from 0-1 that represents how good the enchants are
        return (int) (totalEnchantPercent * 100);
    }

    public static NamedTextColor chooseScoreColor(int score) {

        if (score < 25)
            return NamedTextColor.DARK_RED;

        if (score < 50)
            return NamedTextColor.GOLD;

        if (score < 75)
            return NamedTextColor.YELLOW;

        if (score < 100)
            return NamedTextColor.GREEN;

        // Maxed
        return NamedTextColor.AQUA;

    }

    /**
     * Gets the replacement for the enchants section of the lore
     * since we want to display the enchants our way
     *
     * @return
     */
    protected List<Component> getEnchantLore(ItemStack itemStack, boolean includeStats) {

        List<Component> lore = new ArrayList<>();
        List<Enchantment> enchantments = new ArrayList<>(itemStack.getEnchantments().keySet());
        Map<Enchantment, PotentialEnchantmentWrapper> potentialEnchantments = getAllowedEnchants();
        enchantments.sort(Comparator.comparing(FormatUtil::enchantFriendlyName));
        for (Enchantment enchantment : enchantments) {

            int level = itemStack.getEnchantmentLevel(enchantment);
            PotentialEnchantmentWrapper potentialEnchantment = potentialEnchantments.get(enchantment);

            NamedTextColor color = NamedTextColor.GRAY;

            // If the enchant is overleveled, make it dark aqua
            if (level > enchantment.getMaxLevel())
                color = NamedTextColor.DARK_AQUA;

            // If the enchant is overleveled and max leveled, make it bold orange
            if (!includeStats && level > enchantment.getMaxLevel() && level == potentialEnchantment.getMaxLevel())
                color = NamedTextColor.GOLD;

            if (includeStats)
                lore.add(Component.text(potentialEnchantment.display(), color));
            else
                if (enchantment.getStartLevel() == enchantment.getMaxLevel())
                    lore.add(Component.text(FormatUtil.enchantFriendlyName(enchantment), color));
                else
                    lore.add(Component.text(FormatUtil.enchantFriendlyName(enchantment) + " " + FormatUtil.numToRomanNumeral(level), color));

        }

        return ComponentUtils.removeItalics(lore);

    }

    protected List<Component> getExtraLore() {
        return new ArrayList<>();
    }

    public void updateLore(ItemStack itemStack, boolean display) {

        ItemMeta meta = itemStack.getItemMeta();

        List<Component> lore = new ArrayList<>();
        if (!itemStack.getEnchantments().isEmpty()) {
            lore.addAll(getEnchantLore(itemStack, display));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        lore.add(Component.empty());
        lore.addAll(getExtraLore());

        // Now add the score rating if it is not a display item
        if (!display) {
            lore.add(Component.empty());
            int itemScore = calculateScore(itemStack);
            lore.add(ComponentUtils.merge(
                    Component.text("Gear Rating: ", NamedTextColor.GRAY),
                    Component.text(itemScore, chooseScoreColor(itemScore)).decoration(TextDecoration.BOLD, itemScore >= 100)
            ));
        }

        meta.lore(ComponentUtils.removeItalics(lore));
        itemStack.setItemMeta(meta);
    }

    /**
     * Get an instance of this custom item to do whatever with
     *
     * @return an ItemStack to use however you wish
     */
    public ItemStack get() {
        return get(0f);
    }

    public ItemStack get(float luckBoost) {
        // Get a copy of the item, set its type in metadata and return
        ItemStack item = constructBaseItemStack();
        tagItem(item);
        randomlyEnchant(item, luckBoost);
        updateLore(item, false);
        return item;
    }

    /**
     * Same as get(), but acts as a display for the item to see stats etc
     *
     * @return
     */
    public ItemStack display() {
        // Get a copy of the item, set its type in metadata and return
        ItemStack item = constructBaseItemStack();
        tagItem(item);
        randomlyEnchant(item, 1f);
        updateLore(item, true);
        return item;
    }

    public Material getVanillaType() {
        return get().getType();
    }

    private void tagItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(CustomItemManager.CUSTOM_ITEM_KEY, PersistentDataType.STRING, key());
        item.setItemMeta(meta);
    }

    /**
     * Override to fix any issues with the itemstack after it has been created from previous updates
     */
    public void fix(ItemStack itemStack) {
        updateLore(itemStack, false);
    }

    abstract protected ItemStack constructBaseItemStack();  // Create an item stack that has lore, name, etc

    public boolean matches(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().getOrDefault(CustomItemManager.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "").equals(key());
    }

    public abstract String key(); // What do we save this item's tag as in metadata?


    public void onRightClick(PlayerInteractEvent event){}  // What should we do when we right-click this item while holding it?

    public void onLeftClick(PlayerInteractEvent event){}  // What should we do when we right-click this item while holding it?

    public void onInventoryClick(InventoryClickEvent e){}  // What should we do when we click on this item in an inventory?


    public void onBlockBreak(BlockBreakEvent event) {}  // What should we do when we break a block with this item?
}
