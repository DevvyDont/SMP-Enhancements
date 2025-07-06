package xyz.devvydont.smprpg.gui.economy;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.economy.CustomItemCoin;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public final class MenuWithdraw extends MenuBase {
    private int totalWithdrawn;
    private final CustomItemCoin[] coins;
    private final EconomyService economyService;

    public MenuWithdraw(SMPRPG plugin, Player owner) {
        super(owner, 3);
        this.economyService = SMPRPG.getService(EconomyService.class);
        this.coins = new CustomItemCoin[] {
            (CustomItemCoin) SMPRPG.getService(ItemService.class).getBlueprint(CustomItemType.COPPER_COIN),
            (CustomItemCoin) SMPRPG.getService(ItemService.class).getBlueprint(CustomItemType.SILVER_COIN),
            (CustomItemCoin) SMPRPG.getService(ItemService.class).getBlueprint(CustomItemType.GOLD_COIN),
            (CustomItemCoin) SMPRPG.getService(ItemService.class).getBlueprint(CustomItemType.PLATINUM_COIN),
            (CustomItemCoin) SMPRPG.getService(ItemService.class).getBlueprint(CustomItemType.ENCHANTED_COIN),
        };
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        // Prepare the inventory
        event.titleOverride(ComponentUtils.create("Withdraw Coins", NamedTextColor.BLACK));

        // Render the UI
        this.renderMenu();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        // This UI uses buttons, so there's no code here.
        // But we still need to cancel the event to prevent stealing the borders.
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        if (this.totalWithdrawn == 0)
            return;

        this.player.sendMessage(ComponentUtils.success(ComponentUtils.merge(
            ComponentUtils.create("You've withdrawn ", NamedTextColor.GREEN),
            ComponentUtils.create(EconomyService.formatMoney(this.totalWithdrawn), NamedTextColor.GOLD),
            ComponentUtils.create("! Your balance is now ", NamedTextColor.GREEN),
            ComponentUtils.create(this.economyService.formatMoney(this.player), NamedTextColor.GOLD)
        )));
    }

    /**
     * Clears and re-renders the menu UI.
     */
    private void renderMenu() {
        // Reset the UI
        this.clear();
        this.setBorderEdge();

        // Create the balance item
        this.setSlot(4, createNamedItem(Material.PAPER, ComponentUtils.merge(
            ComponentUtils.create("Your current balance is ", NamedTextColor.GREEN),
            ComponentUtils.create(this.economyService.formatMoney(this.player), NamedTextColor.GOLD)
        )));

        // Starts at row 2 column 2 (slot 10)
        var currentSlot = 10;
        var playerBalance = this.economyService.getMoney(this.player);
        for (var coin : this.coins) {
            currentSlot++;

            // They cannot afford the coin, add filler
            var coinStack = coin.generate();
            if (playerBalance < coin.getWorth(coinStack)) {
                var clayText = String.format("You are %s short!", EconomyService.formatMoney(playerBalance - coin.getWorth(coinStack)));
                var clayName = ComponentUtils.create(clayText, NamedTextColor.RED);
                this.setButton(currentSlot, createNamedItem(Material.CLAY_BALL, clayName), (e) -> {
                    this.playInvalidAnimation();
                });
                continue;
            }

            // They can afford the coin, create a button.
            this.setButton(currentSlot, coin.generate(), (e) -> {
                switch (e.getClick()){
                    case LEFT -> this.performWithdrawal(coin, 1);
                    case RIGHT -> this.performWithdrawal(coin, 50);
                    case SHIFT_LEFT, SHIFT_RIGHT -> this.performWithdrawal(coin, 99);
                }
            });
        }
    }

    /**
     * Deducts money from the player and adds coins to their inventory.
     */
    private void performWithdrawal(CustomItemCoin coin, int desiredStackSize) {
        // First lets calculate how much of this coin the player can withdraw.
        var coinStack = coin.generate();
        var currentBalance = this.economyService.getMoney(this.player);
        var maxCoinsPlayerCanAfford = currentBalance / coin.getWorth(coinStack);
        var amountOfCoinsToGive = Math.min(desiredStackSize, maxCoinsPlayerCanAfford);
        var totalCost = amountOfCoinsToGive * coin.getWorth(coinStack);

        // Ensure the player has enough money.
        if (amountOfCoinsToGive == 0 || currentBalance < totalCost) {
            this.playInvalidAnimation();
            this.player.sendMessage(ComponentUtils.error("You cannot afford to withdrawal this coin."));
            return;
        }

        // Try to take the money out of their account.
        var moneyTakenSuccessfully = this.economyService.takeMoney(this.player, totalCost);
        if (!moneyTakenSuccessfully) {
            this.playInvalidAnimation();
            this.player.sendMessage(ComponentUtils.error("Something went wrong. Transaction was canceled."));
            return;
        }

        // Spin up the money printer and make some coins.
        ItemStack mintedCoins = coin.generate();
        mintedCoins.setAmount(amountOfCoinsToGive);

        // Hand out the money to the player.
        var overflowItems = this.player.getInventory().addItem(mintedCoins);
        for (var entry : overflowItems.entrySet())
            this.player.getWorld().dropItemNaturally(this.player.getEyeLocation(), entry.getValue());

        // Tell them it was successful
        this.player.sendMessage(ComponentUtils.success(ComponentUtils.merge(
            ComponentUtils.create("You withdrew ", NamedTextColor.GREEN),
            ComponentUtils.create(EconomyService.formatMoney(totalCost), NamedTextColor.GOLD),
            ComponentUtils.create(" from your account! ", NamedTextColor.GREEN),
            ComponentUtils.create("Your balance is now ", NamedTextColor.GREEN),
            ComponentUtils.create(this.economyService.formatMoney(this.player), NamedTextColor.GOLD)
        )));

        // Add to the running total
        this.totalWithdrawn += totalCost;
        this.renderMenu();
        this.playSuccessAnimation();
    }
}
