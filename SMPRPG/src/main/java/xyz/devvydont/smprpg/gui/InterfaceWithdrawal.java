package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.economy.CustomItemCoin;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Map;

public class InterfaceWithdrawal extends PrivateInterface {

    public InterfaceWithdrawal(SMPRPG plugin, Player owner) {
        super(plugin, owner);
    }

    /**
     * Attempts to add the item to inventory. If it fails, it will spawn into the world
     *
     * @param item
     */
    public void givePlayerItem(ItemStack item) {
        Map<Integer, ItemStack> overflow = owner.getInventory().addItem(item);
        for (Map.Entry<Integer, ItemStack> entry : overflow.entrySet())
            owner.getWorld().dropItemNaturally(owner.getEyeLocation(), entry.getValue());
    }

    public void performWithdrawal(CustomItemCoin coin, int desiredStackSize) {

        // Can we not afford even one of these coins?
        int balance = plugin.getEconomyService().getMoney(owner);
        if (coin.getWorth() > balance) {
            owner.sendMessage(ComponentUtils.getErrorMessage("You cannot afford to withdrawal this coin."));
            owner.playSound(owner.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        // Determine how much this person is desiring to take out. This is a combination of clicked item and click type.
        int actualStackSize = desiredStackSize;
        int toTakeOut = coin.getWorth() * actualStackSize;

        // If they are trying to take more than their account balance, decrement their stack size of coins until we can afford it.
        while (toTakeOut > balance) {
            actualStackSize--;
            toTakeOut = coin.getWorth() * actualStackSize;
        }

        // I don't think this can ever happen but just in case we somehow underflow
        if (actualStackSize <= 0) {
            owner.sendMessage(ComponentUtils.getErrorMessage("It seems you can no longer afford this coin."));
            owner.playSound(owner.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        // Take the money out and tell them
        boolean success = plugin.getEconomyService().takeMoney(owner, toTakeOut);
        if (!success) {
            owner.sendMessage(ComponentUtils.getErrorMessage("Something went wrong. Transaction was canceled."));
            owner.playSound(owner.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        // Get out the money printer and give it to them
        ItemStack itemDrop = coin.generate();
        itemDrop.setAmount(actualStackSize);
        givePlayerItem(itemDrop);

        // Tell them it was successful
        owner.sendMessage(ComponentUtils.getSuccessMessage(String.format("Withdrew %d coins from your account!", toTakeOut)));
        Component balMessage = Component.text("Your balance is now ").color(NamedTextColor.GRAY)
                .append(Component.text(plugin.getEconomyService().formatMoney(owner)).color(NamedTextColor.GOLD));
        owner.sendMessage(ComponentUtils.getGenericMessage(balMessage));
        owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
    }

    public void updateButtons() {

        CustomItemCoin[] COINS = {
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.COPPER_COIN),
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.SILVER_COIN),
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.GOLD_COIN),
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.PLATINUM_COIN),
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.EMERALD_COIN),
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.AMETHYST_COIN),
                (CustomItemCoin) plugin.getItemService().getBlueprint(CustomItemType.ENCHANTED_COIN),
        };

        // Starts at row 2 column 2 (slot 10)
        int playerBalance = plugin.getEconomyService().getMoney(owner);
        int slot = 9;
        for (CustomItemCoin coin : COINS) {
            slot++;
            // If they can afford the coin add the coin there
            if (playerBalance >= coin.getWorth()) {
                inventory.setItem(slot, coin.generate());
                continue;
            }

            // They cannot afford the coin, add filler
            inventory.setItem(slot, InterfaceUtil.getNamedItem(Material.CLAY_BALL, Component.text(String.format("You are %s short!", EconomyService.formatMoney(playerBalance-coin.getWorth()))).color(NamedTextColor.RED)));
        }

    }

    @Override
    public void initializeDefaultState() {
        inventoryView.setTitle(ChatColor.RED + "Click coins to withdrawal!");
        inventory.clear();
        border(InterfaceUtil.getInterfaceBorder());
        updateButtons();
    }

    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(owner, 3*9);
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {

        // Since this is a pure button clicking GUI, never accept any inventory modification events
        event.setCancelled(true);

        // If our custom inventory wasn't clicked, don't do anything
        if (!inventory.equals(event.getClickedInventory()))
            return;

        if (event.getCurrentItem() == null)
            return;

        // If the item stack clicked wasn't a coin, don't do anything
        SMPItemBlueprint potentialItemClick = plugin.getItemService().getBlueprint(event.getCurrentItem());
        if (!(potentialItemClick instanceof CustomItemCoin))
            return;

        // Now we know we clicked a coin, attempt to withdrawal
        CustomItemCoin clickedCoin = (CustomItemCoin) potentialItemClick;

        // Only listen to normal clicks right clicks and shift clicks.
        // normal -> 1, right -> 50, shift -> 99
        switch (event.getClick()){
            case LEFT -> performWithdrawal(clickedCoin, 1);
            case RIGHT -> performWithdrawal(clickedCoin, 50);
            case SHIFT_LEFT, SHIFT_RIGHT -> performWithdrawal(clickedCoin, 99);
        }

        updateButtons();

    }


}
