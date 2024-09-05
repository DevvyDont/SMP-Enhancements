package xyz.devvydont.smprpg.gui.economy;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public final class MenuDeposit extends MenuBase {
    private final ItemService itemService;
    private final EconomyService economyService;

    public MenuDeposit(SMPRPG plugin, Player owner) {
        super(owner, 5);
        this.itemService = plugin.getItemService();
        this.economyService = plugin.getEconomyService();
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        // Prepare the inventory
        event.titleOverride(ComponentUtils.create("Sell Items", NamedTextColor.BLACK));
        this.setMaxStackSize(100);

        // Render the UI
        this.clear();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
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

        // If the item clicked is not sellable, we can't do anything with it.
        var itemBlueprint = this.itemService.getBlueprint(event.getCurrentItem());
        event.setCancelled(!(itemBlueprint instanceof Sellable));
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        // Work out how much was deposited.
        var quantitySold = 0;
        var amountToCredit = 0;
        for (var depositedItem : this.getItems()) {
            if (depositedItem == null) {
                continue;
            }

            var itemBlueprint = this.itemService.getBlueprint(depositedItem);
            if (itemBlueprint instanceof Sellable sellable) {
                quantitySold += depositedItem.getAmount();
                amountToCredit += sellable.getWorth() * depositedItem.getAmount();
                depositedItem.setAmount(0);
            }
        }

        // Ignore if nothing was deposited.
        if (amountToCredit <= 0) {
            return;
        }

        this.economyService.addMoney(this.player, amountToCredit);
        this.player.sendMessage(ComponentUtils.success(ComponentUtils.merge(
            ComponentUtils.create("You sold ", NamedTextColor.GREEN),
            ComponentUtils.create(String.valueOf(quantitySold), NamedTextColor.AQUA),
            ComponentUtils.create(" items totaling ", NamedTextColor.GREEN),
            ComponentUtils.create(EconomyService.formatMoney(amountToCredit), NamedTextColor.GOLD),
            ComponentUtils.create("! Your balance is now ", NamedTextColor.GREEN),
            ComponentUtils.create(this.economyService.formatMoney(this.player), NamedTextColor.GOLD)
        )));
        this.sounds.playActionConfirm();
    }
}
