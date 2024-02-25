package me.devvy.smpevents.hooks;

import me.devvy.deathdrops.events.AddDeathCertificateEvent;
import me.devvy.deathdrops.events.ProtectDeadDropItemsEvent;
import me.devvy.smpevents.SMPEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Handles cancelling death paper on respawn from DeathDrops
 */
public class DeathDropsHooks implements Listener {


    @EventHandler
    public void onAddDeathPaper(AddDeathCertificateEvent event) {

        // If our player state manager is keeping track of this player for an event, never add the paper
        if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId()) != null)
            event.setCancelled(true);

    }

    @EventHandler
    public void onProtectItems(ProtectDeadDropItemsEvent event) {

        // If our player state manager is keeping track of this player, don't protect their drops
        // We do not need to protect player items in event state
        if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayerDeathEvent().getPlayer().getUniqueId()) != null)
            event.setCancelled(true);


    }

}
