package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.HashMap;
import java.util.Map;

/*
 * Represents a viewable inventory of another player. Used as a way for admins to "peek" into someone's inventory to
 * see what they have.
 */
public class MenuInventoryPeek extends MenuBase {
    // In order to prettily display the inventory, we override some slot positions in this interface to make it make
    // more sense. Keys are indexes of the player's inventory we are viewing, values are indexes of our GUI.
    private static final Map<Integer, Integer> slotOverrides;
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

        // Now finally offhand. We are just shifting it so there's a gap between it and the armor.
        slotOverrides.put(40, 39);
    }
    public final static int CHEST_TOGGLE_SLOT = 46;
    public final static int CLOSE_BUTTON = 49;

    private boolean useEnderChest = false; // A flag that can be set to instead read the ender chest instead of the inventory.
    private BukkitTask inventoryUpdateTask;
    private final Player targetPlayer;

    public MenuInventoryPeek(Player owner, Player targetPlayer) {
        this(owner, targetPlayer, null);
    }

    public MenuInventoryPeek(Player owner, Player targetPlayer, MenuBase parentMenu) {
        super(owner, 6, parentMenu);
        this.targetPlayer = targetPlayer;
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        // Prepare the inventory
        event.titleOverride(ComponentUtils.create(targetPlayer.getName() + "'s Inventory", NamedTextColor.BLACK));

        // Update the inventory layout every tick to match the player.
        inventoryUpdateTask = Bukkit.getScheduler().runTaskTimer(SMPRPG.getInstance(), this::renderInventory, 0, 10);
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        if (inventoryUpdateTask != null) {
            inventoryUpdateTask.cancel();
            inventoryUpdateTask = null;
        }
    }

    /*
     * Updates the interface to match the inventory of the player that we are currently viewing, if there is one.
     */
    private void renderInventory() {
        // Prepare the inventory
        this.clear();
        this.setBorderEdge();

        // Render the players inventory
        var playersInventory = this.useEnderChest ? this.targetPlayer.getEnderChest() : this.targetPlayer.getInventory();
        var inventoryItems = playersInventory.getContents();
        for (var i = 0; i < inventoryItems.length; i++) {
            // Render nothing if there's no items.
            var item = inventoryItems[i];
            var slotIndex = this.useEnderChest ? i : slotOverrides.getOrDefault(i, i);
            if (item == null) {
                this.clearSlot(slotIndex);
                continue;
            }

            // Render the item into the slot
            this.setSlot(slotIndex, item.clone());
        }

        // Render the swap chest button
        var chestButton = createNamedItem(Material.CHEST, ComponentUtils.create("Switch to Inventory", NamedTextColor.GOLD));
        var enderButton = createNamedItem(Material.ENDER_CHEST, ComponentUtils.create("Switch to Ender Chest", NamedTextColor.LIGHT_PURPLE));
        var activeButton = this.useEnderChest ? chestButton : enderButton;
        this.setButton(CHEST_TOGGLE_SLOT, activeButton, (e) -> {
            this.useEnderChest = !this.useEnderChest;
            this.playSound(this.useEnderChest ? Sound.BLOCK_ENDER_CHEST_OPEN : Sound.BLOCK_ENDER_CHEST_CLOSE);
            this.renderInventory();
            this.playSuccessAnimation(false);
        });

        // Render the back/close button
        if (this.parentMenu == null) {
            this.setButton(CLOSE_BUTTON, BUTTON_EXIT, (e) -> this.closeMenu());
        } else {
            this.setButton(CLOSE_BUTTON, BUTTON_BACK, (e) -> this.openParentMenu());
        }
    }
}
