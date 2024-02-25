package me.devvy.stimmys;

import me.devvy.stimmys.events.AttemptStimmyRedeem;
import me.devvy.stimmys.util.ConfigManager;
import me.devvy.stimmys.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StimmyCurrencyManager implements Listener {

    public StimmyCurrencyManager() {
        Stimmys.getInstance().getServer().getPluginManager().registerEvents(this, Stimmys.getInstance());
    }

    /**
     * Called usually by commands or some sort of event where we award players with stimmy points
     * they do not have to be online to do this
     *
     * @param offlinePlayer
     */
    public void addStimmyPoints(OfflinePlayer offlinePlayer, int amount) {

        ConfigManager.addRedeemableStimmyPoints(offlinePlayer, amount);

        // Handle the case where they are online, we just send them a message that it is ready for redeem
        if (offlinePlayer.isOnline())
            sendRedeemReminderMessage(offlinePlayer.getPlayer());
    }

    public void setStimmyPoints(OfflinePlayer offlinePlayer, int amount) {

        ConfigManager.setRedeemableStimmyPoints(offlinePlayer, amount);

        // Handle the case where they are online, we just send them a message that it is ready for redeem
        if (offlinePlayer.isOnline())
            sendRedeemReminderMessage(offlinePlayer.getPlayer());
    }

    /**
     * Call to award a player with any stimmy points they have, this should be called upon command usage
     * @param player
     */
    public void redeemStimmyPoints(Player player) {

        int amount = ConfigManager.getRedeemableStimmyPoints(player);
        if (amount <= 0) {
            player.sendMessage(Component.text("No stimmies to redeem!", NamedTextColor.RED));
            return;
        }

        AttemptStimmyRedeem redeemStimmyEvent = new AttemptStimmyRedeem(player);
        redeemStimmyEvent.callEvent();
        if (redeemStimmyEvent.isCancelled()) {
            player.sendMessage(Component.text(redeemStimmyEvent.getReason(), NamedTextColor.RED));
            return;
        }

        ItemUtil.addItemToInventoryOverflowSafe(player, ItemUtil.getStimmyItem(amount));
        player.sendMessage(
                Component.text("Redeemed ", NamedTextColor.GREEN)
                        .append(Component.text(amount, NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text(" stimmies!", NamedTextColor.GREEN))

        );
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1.5f);
        setStimmyPoints(player, 0);
    }

    private void sendRedeemReminderMessage(Player player) {

        int numPoints = ConfigManager.getRedeemableStimmyPoints(player);
        if (numPoints <= 0)
            return;

        Component commandClickComponent = Component.text("/stimmy redeem", NamedTextColor.GREEN, TextDecoration.BOLD);
        commandClickComponent = commandClickComponent.clickEvent(ClickEvent.runCommand("/stimmy redeem"));
        commandClickComponent = commandClickComponent.hoverEvent(Component.text("Click to execute!", NamedTextColor.GREEN));

        player.sendMessage(
                Component.text("You have ", NamedTextColor.GRAY)
                        .append(Component.text(numPoints + " Stimmy Points ", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("available for redemption!", NamedTextColor.GRAY))
        );
        player.sendMessage(
                Component.text("Use ", NamedTextColor.GRAY)
                        .append(commandClickComponent)
                        .append(Component.text(" to redeem them!", NamedTextColor.GRAY))
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        sendRedeemReminderMessage(event.getPlayer());
    }

    public void cleanup() {
    }
}
