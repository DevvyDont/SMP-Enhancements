package me.devvy.smpevents.hooks;

import me.devvy.smpevents.SMPEvents;
import me.devvy.smpevents.player.EventPlayer;
import me.devvy.smpparkour.events.AttemptEnterParkourWorldEvent;
import me.devvy.smpparkour.events.AttemptLeaveParkourWorldEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SMPParkourHooks implements Listener {

    @EventHandler
    public void onAttemptWarpToParkour(AttemptEnterParkourWorldEvent event) {

        System.out.println("HELLO??? onAttemptWarpToParkour");

        // If this player is not at event area do not let them go
        EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId());

        if (ep == null) {
            event.setCancelled(true);
            event.setReason("You must be in the event area.");
            return;
        }


        if (ep.isPlayingGame()) {
            event.setCancelled(true);
            event.setReason("You are already playing a game!");
            return;
        }

        // Should be good to go
        ep.heal();
        ep.setKeepFed(true);
        ep.setPlayingGame(true);
        ep.getPlayer().setInvulnerable(false);
    }

    @EventHandler
    public void onAttemptWarpOut(AttemptLeaveParkourWorldEvent event) {

        System.out.println("HELLO??? onAttemptWarpOut");

        // If this player is not at event area something REALLY went wrong so we really cannot do anything
        EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId());
        if (ep == null)
            return;

        // Should be good to go
        ep.heal();
        ep.setKeepFed(true);
        ep.setPlayingGame(false);
        ep.getPlayer().setInvulnerable(true);

    }

}
