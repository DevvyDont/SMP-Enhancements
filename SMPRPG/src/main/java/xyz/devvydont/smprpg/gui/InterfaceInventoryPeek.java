package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.HashMap;
import java.util.Map;

/*
 * Represents a viewable inventory of another player. Used as a way for admins to "peek" into someone's inventory to
 * see what they have.
 */
public class InterfaceInventoryPeek extends PrivateInterface {

    // In order to prettily display the inventory, we override some slot positions in this interface to make it make
    // more sense. Keys are indexes of the player's inventory we are viewing, values are indexes of our GUI.
    private static Map<Integer, Integer> slotOverrides;
    static {
        slotOverrides = new HashMap<>();

        // First, map the hotbar to be in the 4th row by shifting it down 3 rows.
        for (int i = 0; i < 9; i++)
            slotOverrides.put(i, i+27);

        // Now shift the next 3 rows up by 1 row. This is the inventory of the player excluding the hotbar.
        for (int i = 9; i < 36; i++)
            slotOverrides.put(i, i-9);

        // Armor slots, these simply just need to be reversed. We can also shift them so offhand can be on the left
        slotOverrides.put(36, 39+5);
        slotOverrides.put(37, 38+5);
        slotOverrides.put(38, 37+5);
        slotOverrides.put(39, 36+5);

        // Now finally offhand. We are just shifting it so theres a gap between it and the armor.
        slotOverrides.put(40, 39);
    }

    public final static int CHEST_TOGGLE_SLOT = 46;
    public final static int CLOSE_BUTTON = 49;

    private Player currentlyViewing = null;
    private BukkitTask inventoryUpdateTask = null;

    // A flag that can be set to instead read the ender chest instead of the inventory.
    private boolean useEnderChest = false;

    public InterfaceInventoryPeek(SMPRPG plugin, Player owner) {
        super(plugin, owner);
    }

    /*
     * Updates the interface to match the inventory of the player that we are currently viewing, if there is one.
     */
    private void updateCurrentPlayer() {

        if (currentlyViewing == null)
            return;

        fill(InterfaceUtil.getInterfaceBorder());

        ItemStack[] inventoryToCheck = useEnderChest ? currentlyViewing.getEnderChest().getContents() : currentlyViewing.getInventory().getContents();

        for (int i = 0; i < inventoryToCheck.length; i++) {

            ItemStack item = inventoryToCheck[i];
            if (item == null) {
                inventory.setItem(useEnderChest ? i : slotOverrides.getOrDefault(i, i), null);
                continue;
            }

            // Check for an inventory slot override for our GUI. We only need to do this for the normal inventory.
            int index = useEnderChest ? i : slotOverrides.getOrDefault(i, i);
            inventory.setItem(index, item.clone());
        }

        ItemStack chestButton = useEnderChest ? InterfaceUtil.getNamedItem(Material.CHEST, ComponentUtils.create("Switch to Inventory", NamedTextColor.GOLD)) :
                InterfaceUtil.getNamedItem(Material.ENDER_CHEST, ComponentUtils.create("Switch to Ender Chest", NamedTextColor.LIGHT_PURPLE));
        inventory.setItem(CHEST_TOGGLE_SLOT, chestButton);
        inventory.setItem(CLOSE_BUTTON, InterfaceUtil.getNamedItem(Material.BARRIER, ComponentUtils.create("Close", NamedTextColor.RED)));
        inventoryView.setTitle(currentlyViewing.getName() + "'s " + (useEnderChest ? "Ender Chest" : "Inventory"));
    }

    /*
     * Call to update the interface to show the inventory of the given player and start a dynamic task for it.
     */
    public void showPlayer(Player player) {

        cleanupUpdateTask();
        currentlyViewing = player;

        // Update the inventory layout every tick to match the player.
        inventoryUpdateTask = Bukkit.getScheduler().runTaskTimer(plugin, this::updateCurrentPlayer, 0, 10);
    }

    /*
     * Cleans up the current player inventory update task, if it exists.
     */
    private void cleanupUpdateTask() {
        if (inventoryUpdateTask == null)
            return;

        inventoryUpdateTask.cancel();
        inventoryUpdateTask = null;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        // If we clicked the chest icon, flip the behavior.
        if (event.getSlot() == CHEST_TOGGLE_SLOT) {
            useEnderChest = !useEnderChest;
            updateCurrentPlayer();
            owner.playSound(owner.getLocation(), useEnderChest ? Sound.BLOCK_ENDER_CHEST_OPEN : Sound.BLOCK_ENDER_CHEST_CLOSE, 1, 1);
            return;
        }

        if (event.getSlot() == CLOSE_BUTTON)
            owner.closeInventory();

        owner.playSound(owner.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent event) {
        super.handleInventoryClose(event);
        cleanupUpdateTask();
    }
}
