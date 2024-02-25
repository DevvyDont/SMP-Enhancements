package me.devvy.smpevents.hooks;

import me.devvy.smpduels.events.DuelCreateEvent;
import me.devvy.smpduels.events.DuelEndedEvent;
import me.devvy.smpevents.SMPEvents;
import me.devvy.smpevents.player.EventPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SMPDuelsHooks implements Listener {

    @EventHandler
    public void onDuelMatchStarted(DuelCreateEvent event) {

        for (Player player : event.getPlayers()) {

            // If any of these players are not registered, then cancel
            if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(player.getUniqueId()) == null) {
                event.setCancelled(true);
                event.setCancelReason(String.format("Player %s was not in the event area.", player.getName()));
                return;
            }

            EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(player.getUniqueId());
            if (ep.isPlayingGame()) {
                event.setCancelled(true);
                event.setCancelReason(String.format("Player %s is already playing a different game!", player.getName()));
                return;
            }

        }

        // Match is good to start, don't let them be fed
        for (Player player : event.getPlayers()) {
            EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(player.getUniqueId());
            if (ep != null) {
                ep.setKeepFed(false);
                ep.setPlayingGame(true);
            }

        }

    }

    @EventHandler
    public void onDuelMatchEnded(DuelEndedEvent event) {

        for (EventPlayer p : SMPEvents.getInstance().getPlayerStateManager().getAllEventPlayers()) {
            p.heal();
            p.setKeepFed(true);
            p.getPlayer().setInvulnerable(true);
            p.setPlayingGame(false);
        }

    }
}
