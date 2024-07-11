package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class CustomItemBlueprint extends SMPItemBlueprint {

    protected ItemService itemService;

    public CustomItemBlueprint(ItemService itemService) {
        super(itemService);
        this.itemService = itemService;
    }

    /**
     * Since this item is custom, return the CustomItem enum that this item is linked to.
     */
    public abstract CustomItemType getCustomItemType();

    @Override
    public ItemRarity getRarity(ItemStack item) {
        return getDefaultRarity();
    }

    @Override
    public ItemRarity getRarity(ItemMeta meta) {
        return getDefaultRarity();
    }

    @Override
    public ItemRarity getDefaultRarity() {
        return getCustomItemType().rarity;
    }

    /**
     * Given item meta, determine how this item's display name should look
     *
     * @param meta
     * @return
     */
    @Override
    public Component getNameComponent(ItemMeta meta) {
        return getRarity(meta).applyDecoration(Component.text(getCustomItemType().name)).decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Called to set various components and attributes of this item, can be overidden for extra functionality
     *
     * @param meta
     */
    public void updateMeta(ItemMeta meta) {

        // Apply the key to the item so the plugin knows this item is custom
        meta.getPersistentDataContainer().set(itemService.ITEM_TYPE_KEY, PersistentDataType.STRING, getCustomItemType().getKey());
        // Set model data
        meta.setCustomModelData(getCustomItemType().getModelData());

        super.updateMeta(meta);
    }

    public ItemStack generate() {

        // Make the item and retrieve meta
        ItemStack itemStack = new ItemStack(getCustomItemType().material);
        ItemMeta meta = itemStack.getItemMeta();

        // Update all meta components of this item. This method is a good place to hook into for specific tweaks
        updateMeta(meta);

        // Apply changes and return!
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Determines if item given is an item belonging to this blueprint
     *
     * @param itemStack
     * @return
     */
    public boolean isItemOfType(ItemStack itemStack) {

        String itemKey = itemService.getItemKey(itemStack);
        if (itemKey == null)
            return false;

        return itemKey.equals(getCustomItemType().getKey());
    }

    /**
     * All items that descend from this class are custom.
     */
    @Override
    public boolean isCustom() {
        return true;
    }

    @Override
    public boolean wantFakeEnchantGlow() {
        return getCustomItemType().glow;
    }
}
