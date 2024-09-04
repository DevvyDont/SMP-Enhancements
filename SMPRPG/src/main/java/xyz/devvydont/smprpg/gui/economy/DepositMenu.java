package xyz.devvydont.smprpg.gui.economy;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public final class DepositMenu extends MenuBase {
    private final ItemService itemService;
    private final EconomyService economyService;

    public DepositMenu(SMPRPG plugin, Player owner) {
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
        this.setBorderEdge();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        // The player should be very limited here, to prevent exploits.
        // They should only be able to interact with sellable items and empty slots.

        // Allow the player to click an empty inventory slot.
        // This is so they can place a sellable item into the menu.
        if (event.getClickedInventory() != null && event.getClickedInventory().getItem(event.getSlot()) == null) {
            event.setCancelled(false);
            return;
        }

        // Ignore any case which doesn't involve an item.
        if (event.getCurrentItem() == null) {
            this.playInvalidAnimation();
            return;
        }

        // Ignore any case where an item clicked is not sellable.
        var itemBlueprint = this.itemService.getBlueprint(event.getCurrentItem());
        var itemIsSellable = itemBlueprint instanceof Sellable;
        if (!itemIsSellable) {
            this.playInvalidAnimation();
            return;
        }

        // Allow the operation to take place.
        event.setCancelled(false);
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        // Work out how much was deposited.
        var totalDeposited = 0;
        for (var depositedItem : this.getItems()) {
            if (depositedItem == null) {
                continue;
            }

            var itemBlueprint = this.itemService.getBlueprint(depositedItem);
            if (itemBlueprint instanceof Sellable sellable) {
                totalDeposited += sellable.getWorth() * depositedItem.getAmount();
                depositedItem.setAmount(0);
            }
        }

        // Ignore if nothing was deposited.
        if (totalDeposited <= 0) {
            return;
        }

        this.economyService.addMoney(this.player, totalDeposited);
        this.player.sendMessage(ComponentUtils.success(ComponentUtils.merge(
            ComponentUtils.create("You sold ", NamedTextColor.GREEN),
            ComponentUtils.create(EconomyService.formatMoney(totalDeposited), NamedTextColor.GOLD),
            ComponentUtils.create(" worth of items! ", NamedTextColor.GREEN),
            ComponentUtils.create("Your balance is now ", NamedTextColor.GREEN),
            ComponentUtils.create(this.economyService.formatMoney(this.player), NamedTextColor.GOLD)
        )));
        this.sounds.playActionConfirm();
    }
}
