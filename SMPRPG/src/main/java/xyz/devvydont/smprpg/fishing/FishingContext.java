package xyz.devvydont.smprpg.fishing;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * A simple class that provides basic context that the {@link FishingCalculator} would find relevant.
 */
public class FishingContext {

    private final Player player;
    private final EquipmentSlot hand;
    private final Location location;

    /**
     * Construct the fishing context using the fishing event. This provides us with everything we need.
     * @param event The {@link PlayerFishEvent} providing relevant context.
     */
    public FishingContext(PlayerFishEvent event) {
        this.player = event.getPlayer();
        this.location = event.getHook().getLocation();
        this.hand = event.getHand();

//        this.location.getBlock().gettem
    }

    /**
     * Get the relevant player that is fishing.
     * @return A {@link Player} instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the equipment slot that was used to initiate this fish event. This is crucial to provide, as we need
     * to heavily nerf people who are trying to abuse dual wielding fishing rods to exploit attributes.
     * @return The slot used to initiate this event.
     */
    public EquipmentSlot getHand() {
        return hand;
    }

    /**
     * Gets the relevant location that should be checked in this context. This location represents the location of the
     * fishing bobber. If you want the player's location, use {@link FishingContext#getPlayer()} instead.
     * @return The location of this fishing event.
     */
    private Location getLocation() {
        return location;
    }

}
