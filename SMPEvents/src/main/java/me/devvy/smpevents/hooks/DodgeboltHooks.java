package me.devvy.smpevents.hooks;

import me.devvy.dodgebolt.events.PlayerEnterStadiumEvent;
import me.devvy.dodgebolt.events.PlayerLeaveStadiumEvent;
import me.devvy.smpevents.SMPEvents;
import me.devvy.smpevents.player.EventPlayer;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DodgeboltHooks implements Listener {

    private Set<UUID> playersInStadium = new HashSet<>();

    @EventHandler
    public void onEnterDodgeboltStadium(PlayerEnterStadiumEvent event) {
        event.getPlayer().setInvulnerable(false);
        playersInStadium.add(event.getPlayer().getUniqueId());
        EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId());
        if (ep != null) {
            ep.setPlayingGame(true);
            ep.setKeepFed(true);
            ep.setDropItemsOnDeath(true);
        }
    }

    @EventHandler
    public void onExitDodgeboltStadium(PlayerLeaveStadiumEvent event) {
        playersInStadium.remove(event.getPlayer().getUniqueId());
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        EventPlayer ep = SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId());
        if (ep != null) {
            ep.setPlayingGame(false);
            ep.setKeepFed(true);
            ep.setDropItemsOnDeath(false);
            event.getPlayer().setInvulnerable(true);
        }
    }
}
