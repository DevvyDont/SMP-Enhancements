package me.devvy.smpevents.hooks;

import me.devvy.smpevents.SMPEvents;
import me.devvy.stimmys.events.AttemptOpenStimmyShop;
import me.devvy.stimmys.events.AttemptStimmyRedeem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Handles stopping people from redeeming + buying things in the event area
 */
public class StimmysHooks implements Listener {

    @EventHandler
    public void onOpenShopInEventMode(AttemptOpenStimmyShop event) {

        // If our player state manager is keeping track of this player for an event, never open the shop
        if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId()) != null) {
            event.setCancelled(true);
            event.setReason("You cannot open the shop while in the event area!");
        }

    }

    @EventHandler
    public void onRedeemStimmyInEventMode(AttemptStimmyRedeem event) {

        // If our player state manager is keeping track of this player for an event, never redeem stimmy
        if (SMPEvents.getInstance().getPlayerStateManager().getEventPlayer(event.getPlayer().getUniqueId()) != null) {
            event.setCancelled(true);
            event.setReason("You cannot redeem stimmies while in the event area!");
        }

    }

}
