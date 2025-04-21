package xyz.devvydont.smprpg.items.base;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class CustomItemBlueprint extends SMPItemBlueprint {

    protected ItemService itemService;
    protected final CustomItemType type;

    public CustomItemBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService);
        this.itemService = itemService;
        this.type = type;
    }

    /**
     * Since this item is custom, return the CustomItem enum that this item is linked to.
     */
    public CustomItemType getCustomItemType() {
        return type;
    }

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

    @Override
    public String getItemName(ItemMeta meta) {
        return getCustomItemType().name;
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

        // Make the starting item.
        ItemStack itemStack = new ItemStack(getCustomItemType().material);

        // Apply updates to this item according to our blueprint's spec.
        updateMeta(itemStack);
        return itemStack;
    }

    public ItemStack generate(int amount) {
        var stack = generate();
        stack.setAmount(amount);
        return stack;
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
