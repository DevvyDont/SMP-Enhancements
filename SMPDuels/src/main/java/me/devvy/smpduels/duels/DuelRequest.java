package me.devvy.smpduels.duels;

import me.devvy.smpduels.SMPDuels;
import me.devvy.smpduels.events.DuelRequestTimeoutEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * A class used to keep track of a player attempting to duel another player, after the timeout happens
 * we cancel the request
 */
public class DuelRequest extends BukkitRunnable {

    public final static int TIMEOUT = 20*60;  // 60 seconds/1200 ticks to accept

    private UUID initiator;  // Who started the request
    private UUID requestee;  // Who needs to accept the request

    public DuelRequest(Player initiator, Player requestee) {
        this.initiator = initiator.getUniqueId();
        this.requestee = requestee.getUniqueId();
    }

    /**
     * Called to make sure that both players are online
     *
     * @return
     */
    public boolean validate() {
        return Bukkit.getPlayer(initiator) != null && Bukkit.getPlayer(requestee) != null;
    }

    /**
     * Called to initiate the request, send a message to the requestee allowing them to accept
     */
    public void initiate() {
        this.runTaskLater(SMPDuels.getInstance(), TIMEOUT);
    }

    @Override
    public void run() {
        cancel();
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        DuelRequestTimeoutEvent event = new DuelRequestTimeoutEvent(this);
        SMPDuels.getInstance().getServer().getPluginManager().callEvent(event);
        super.cancel();
    }

    public UUID getInitiator() {
        return initiator;
    }

    public UUID getRequestee() {
        return requestee;
    }

    public void sendMessageToBothPlayers(Component message) {

        if (Bukkit.getPlayer(initiator) != null)
            Bukkit.getPlayer(initiator).sendMessage(message);

        if (Bukkit.getPlayer(requestee) != null)
            Bukkit.getPlayer(requestee).sendMessage(message);

    }
}
