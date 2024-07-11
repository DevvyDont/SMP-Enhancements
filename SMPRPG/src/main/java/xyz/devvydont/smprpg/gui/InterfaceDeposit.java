package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.economy.CustomItemCoin;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;

public class InterfaceDeposit extends PrivateInterface {

    public InterfaceDeposit(SMPRPG plugin, Player owner) {
        super(plugin, owner);
    }

    public void performDeposit() {

        int value = 0;

        for (ItemStack item : inventory.getContents()) {

            if (item == null)
                continue;

            SMPItemBlueprint potentialCoinType = plugin.getItemService().getBlueprint(item);
            if (!(potentialCoinType instanceof CustomItemCoin))
                continue;

            CustomItemCoin coin = (CustomItemCoin) potentialCoinType;
            value += coin.getValue(item);
            item.setAmount(0);
        }

        if (value <= 0)
            return;

        plugin.getEconomyService().addMoney(owner, value);
        owner.sendMessage(ChatUtil.getSuccessMessage(String.format("Deposited %d coins into your account!", value)));
        Component balMessage = Component.text("Your balance is now ").color(NamedTextColor.GRAY)
                .append(Component.text(plugin.getEconomyService().formatMoney(owner)).color(NamedTextColor.GOLD));
        owner.sendMessage(ChatUtil.getGenericMessage(balMessage));
        owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
    }

    @Override
    public void initializeDefaultState() {
        inventoryView.setTitle(ChatColor.GREEN + "Add coins to deposit them!");
        inventory.clear();
        border(InterfaceUtil.getInterfaceBorder());
        inventory.setMaxStackSize(100);
    }

    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(owner, 3*9);
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {

        // Never allow hotkey inventory modifications
        if (event.getClick().equals(ClickType.NUMBER_KEY)) {
            event.setCancelled(true);
            return;
        }

        // If an inventory was clicked and the slot clicked was empty, allow it
        if (event.getClickedInventory() != null && event.getClickedInventory().getItem(event.getSlot()) == null) {
            event.setCancelled(false);
            return;
        }

        // No item involved, don't allow it
        if (event.getCurrentItem() == null) {
            event.setCancelled(true);
            return;
        }

        // If the item clicked is not a coin, we can't do anything with it.
        if (!(plugin.getItemService().getBlueprint(event.getCurrentItem()) instanceof CustomItemCoin)) {
            event.setCancelled(true);
            return;
        }

        // Item clicked was a coin, we should allow this
        event.setCancelled(false);
    }

    @Override
    public void handleInventoryClose(InventoryCloseEvent event) {
        performDeposit();
        super.handleInventoryClose(event);
    }
}
