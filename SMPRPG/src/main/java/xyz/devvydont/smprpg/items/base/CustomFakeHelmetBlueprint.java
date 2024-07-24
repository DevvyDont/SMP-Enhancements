package xyz.devvydont.smprpg.items.base;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;

/**
 * Used to detect when we are trying to equip a "fake" helmet. Fake helmets are simply normal blocks that a
 * player is able to wear on their head
 */
public abstract class CustomFakeHelmetBlueprint extends CustomAttributeItem implements Listener {

    public static final int HELMET_SLOT = 39;  // The slot that corresponds to the helmet slot.

    public CustomFakeHelmetBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }


    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setMaxStackSize(1);  // These cannot stack
    }

    /**
     *
     * @param event
     */
    private void handleShiftClick(InventoryClickEvent event) {

        // Are we already wearing a helmet? (can't do anything)
        ItemStack helmet = event.getWhoClicked().getInventory().getHelmet();
        if (helmet != null)
            return;

        // Are we clicking in the normal inventory?
        if (event.getClickedInventory() == null)
            return;

        if (!event.getSlotType().equals(InventoryType.SlotType.CONTAINER) && !event.getSlotType().equals(InventoryType.SlotType.QUICKBAR))
            return;

        ItemStack shiftClickedItem = event.getCurrentItem();
        // Did we not click anything?
        if (shiftClickedItem == null || shiftClickedItem.getType().equals(Material.AIR))
            return;


        // We know we are not wearing a helmet and we shift clicked an item in our inventory, does it belong to us?
        if (!isItemOfType(shiftClickedItem))
            return;

        // Good to equip
        event.getWhoClicked().getInventory().setHelmet(shiftClickedItem);
        shiftClickedItem.setAmount(0);
        event.setCancelled(true);
    }

    private void handleNormalClick(InventoryClickEvent event) {

        ItemStack mouse = event.getCursor();
        ItemStack clicked = event.getCurrentItem();
        boolean clickedArmorSlot = event.getSlotType().equals(InventoryType.SlotType.ARMOR) && event.getSlot() == HELMET_SLOT;

        // First consider the simple case where we are clicking on the helmet slot with the helmet on our cursor.
        if (clickedArmorSlot && !mouse.getType().equals(Material.AIR) && isItemOfType(mouse)) {

            // We are clicking on an armor slot and we are holding our item. Swap the mouse and helmet
            event.getView().setCursor(clicked);
            event.getWhoClicked().getInventory().setHelmet(mouse);
            event.setCancelled(true);
        }

    }

    private void handleInteract(PlayerInteractEvent event) {

        // Did we interact using our hand?
        if (event.getHand() == null)
            return;

        // Is this our item?
        if (event.getItem() == null || !isItemOfType(event.getItem()))
            return;

        // Swap the helmet and the item in our hand
        ItemStack newHelmet = event.getItem();
        event.getPlayer().getInventory().setItem(event.getHand(), event.getPlayer().getInventory().getHelmet());
        event.getPlayer().getInventory().setHelmet(newHelmet);
        event.setCancelled(true);
    }

    /**
     * When we click on this item in an inventory, we need to see if we are trying to equip it
     *
     * @param event
     */
    @EventHandler
    public void onShiftClick(InventoryClickEvent event) {

        // We only listen to this event if the player is in their inventory
        if (!event.getView().getType().equals(InventoryType.CRAFTING))
            return;

        // If this was a shift click...
        if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
            handleShiftClick(event);
            return;
        }

        // If this was a left/right click...
        if (event.getClick().equals(ClickType.LEFT) || event.getClick().equals(ClickType.RIGHT)) {
            handleNormalClick(event);
            return;
        }

    }

    /**
     * When we right click with this item, we need to equip it.
     *
     * @param event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        // Ignore left clicks
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;

        if (!isItemOfType(event.getItem()))
            return;

        // We are interacting with this item.
        handleInteract(event);
    }
}
