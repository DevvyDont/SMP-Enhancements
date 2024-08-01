package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.reforge.ReforgeBase;

import java.util.ArrayList;
import java.util.List;

public class InterfaceReforge extends PrivateInterface {

    public static final int INPUT_SLOT = 22;
    public static final int BUTTON_SLOT = 31;

    public InterfaceReforge(SMPRPG plugin, Player owner) {
        super(plugin, owner);
    }

    public ItemStack getAnvilButton() {
        return InterfaceUtil.getNamedItem(Material.ANVIL, Component.text("Reforge!"));
    }

    public ReforgeBase getRandomReforge() {
        List<ReforgeBase> choices = new ArrayList<>(SMPRPG.getInstance().getItemService().getReforges());
        return choices.get((int) (Math.random()*choices.size()));
    }

    public void reforge() {

        ItemStack item = getItem(INPUT_SLOT);
        if (item == null)
            return;

        SMPItemBlueprint blueprint = plugin.getItemService().getBlueprint(item);
        if (!(blueprint instanceof Attributeable attributeable))
            return;

        ReforgeBase reforge = getRandomReforge();
        reforge.apply(item);
        Location soundOrigin = owner.getLocation().add(owner.getLocation().getDirection().normalize().multiply(2));
        owner.getWorld().playSound(soundOrigin, Sound.BLOCK_ANVIL_USE, .5f, .75f);
        blueprint.updateMeta(item);
    }

    @Override
    public void initializeDefaultState() {
        super.initializeDefaultState();
        fill(InterfaceUtil.getInterfaceBorder());
        clearSlot(INPUT_SLOT);
        setSlot(BUTTON_SLOT, getAnvilButton());
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        super.handleInventoryClick(event);

        // Treat click events as a whitelist style
        event.setCancelled(true);

        if (event.getClickedInventory() == null)
            return;

        // If we are clicking in the player inventory allow it to happen
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            event.setCancelled(false);
            return;
        }

        // If we are clicking in the input slot allow it to happen
        if (event.getClickedInventory().equals(inventory) && event.getSlot() == INPUT_SLOT) {
            event.setCancelled(false);
            return;
        }

        // If we left clicked the reforge button then reforge
        if (event.getClick().equals(ClickType.LEFT) && event.getSlot() == BUTTON_SLOT) {
            reforge();
            return;
        }
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent event) {
        super.handleInventoryClose(event);

        transferItemToPlayer(INPUT_SLOT);
    }
}
