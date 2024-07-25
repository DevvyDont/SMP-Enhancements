package xyz.devvydont.smprpg.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;

/**
 * Interface used that only the viewer can view and interact with.
 */
public abstract class PrivateInterface implements Listener {

    protected final SMPRPG plugin;
    protected final Player owner;
    protected Inventory inventory;
    protected InventoryView inventoryView = null;

    public PrivateInterface(SMPRPG plugin, Player owner) {
        this.plugin = plugin;
        this.owner = owner;

        inventory = createInventory();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public int getNumRows() {
        return 6;
    }

    public Inventory createInventory() {
        return Bukkit.createInventory(owner, 9*getNumRows());
    }

    /**
     * Call to revert the inventory contents to its initial state.
     * Automatically called upon construction
     */
    public void initializeDefaultState() {

    }

    /**
     * Adds a border around the edges of the inventory to a certain material
     *
     * @param itemStack
     */
    public void border(ItemStack itemStack) {

        for (int slot = 0; slot < inventory.getSize(); slot++) {

            // If we are on an edge, always add the item
            if (slot % 9 == 0 || slot % 9 == 8) {
                inventory.setItem(slot, itemStack);
                continue;
            }

            // If we don't have at least 3 rows, we can't do horizontal borders
            if (inventory.getSize() < 27)
                continue;

            // Top row will always have a border
            if (slot <= 8) {
                inventory.setItem(slot, itemStack);
                continue;
            }

            // Last 9 spaces will always have a border
            if (inventory.getSize() - slot <= 9)
                inventory.setItem(slot, itemStack);

        }
    }

    /**
     * Fills the entire inventory with an item
     *
     * @param itemStack
     */
    public void fill(ItemStack itemStack) {
        for (int slot = 0; slot < inventory.getSize(); slot++)
            inventory.setItem(slot, itemStack);
    }

    public void open() {
        inventoryView = owner.openInventory(inventory);
        initializeDefaultState();
    }

    public void handleInventoryClick(InventoryClickEvent event) {}

    public void handleInventoryClose(InventoryCloseEvent event) {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getInventory().equals(inventory))
            return;

        handleInventoryClick(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        if (!event.getInventory().equals(inventory))
            return;

        handleInventoryClose(event);
    }
}
