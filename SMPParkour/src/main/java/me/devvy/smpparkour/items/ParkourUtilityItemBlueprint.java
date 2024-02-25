package me.devvy.smpparkour.items;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class ParkourUtilityItemBlueprint {

    protected final ItemStack itemStack;

    protected ParkourUtilityItemBlueprint() {
        this.itemStack = constructBaseItemStack();
    }

    /**
     * Get an instance of this custom item to do whatever with
     *
     * @return an ItemStack to use however you wish
     */
    public ItemStack get() {
        // Get a copy of the item, set its type in metadata and return
        ItemStack item = itemStack.clone();
        tagItem(item);
        return item;
    }

    public Material getVanillaType() {
        return itemStack.getType();
    }

    private void tagItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(ItemManager.CUSTOM_ITEM_KEY, PersistentDataType.STRING, key());
        item.setItemMeta(meta);
    }

    abstract protected ItemStack constructBaseItemStack();  // Create an item stack that has lore, name, etc

    public boolean matches(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().getOrDefault(ItemManager.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "").equals(key());
    }

    public abstract String key(); // What do we save this item's tag as in metadata?


    public void onRightClick(Player player){}  // What should we do when we right-click this item while holding it?

    public void onLeftClick(Player player){}  // What should we do when we right-click this item while holding it?

    public void onInventoryClick(Player player){}  // What should we do when we click on this item in an inventory?

}
