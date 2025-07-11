package xyz.devvydont.smprpg.fishing.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.services.AttributeService;

/**
 * A simple class that provides basic context for fishing loot generation.
 */
public class FishingContext {

    private final Player player;
    private final @Nullable EquipmentSlot hand;
    private final Location location;

    /**
     * Construct the fishing context using the fishing event. This provides us with everything we need.
     * @param event The {@link PlayerFishEvent} providing relevant context.
     */
    public FishingContext(PlayerFishEvent event) {
        this(event.getPlayer(), event.getHand(), event.getHook().getLocation());
    }

    /**
     * Construct the fishing context using various Bukkit objects.
     * @param player The player that is fishing.
     * @param hand The hand involved with this event. This can be null if the hand is not important.
     * @param location The location to check. In a real fishing environment, this should be the hook's location.
     */
    public FishingContext(Player player, @Nullable EquipmentSlot hand, Location location) {
        this.player = player;
        this.hand = hand;
        this.location = location;
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
    public @Nullable EquipmentSlot getHand() {
        return hand;
    }

    /**
     * Gets the relevant location that should be checked in this context. This location represents the location of the
     * fishing bobber. If you want the player's location, use {@link FishingContext#getPlayer()} instead.
     * @return The location of this fishing event.
     */
    public Location getLocation() {
        return location;
    }

    public TemperatureReading getTemperature() {
        return TemperatureReading.fromValue(location.getBlock().getTemperature());
    }

    /**
     * Checks the player's catch quality in this interaction. This is determined by their catch quality attribute.
     * @return The player's catch quality.
     */
    public int getCatchQuality() {
        var quality = SMPRPG.getService(AttributeService.class).getAttribute(player, AttributeWrapper.FISHING_RATING);
        if (quality == null)
            return 0;
        return (int)quality.getValue();
    }

    @Override
    public String toString() {
        return "FishingContext{" +
                "temp=" + getTemperature() +
                ", biome=" + getLocation().getBlock().getBiome() +
                ", location=" + location +
                '}';
    }
}
