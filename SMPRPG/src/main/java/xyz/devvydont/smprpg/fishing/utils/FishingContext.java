package xyz.devvydont.smprpg.fishing.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple class that provides basic context for fishing loot generation.
 */
public class FishingContext {

    private final Player player;
    private final @Nullable EquipmentSlot hand;
    private final Location location;
    private final Set<IFishingRod.FishingFlag> flags = new HashSet<>();

    /**
     * Retrieve the present flags from a fishing hook.
     * @param hook The hook to query.
     * @return A set of {@link xyz.devvydont.smprpg.items.interfaces.IFishingRod.FishingFlag}s
     */
    public static Set<IFishingRod.FishingFlag> getFlagsFromHook(FishHook hook) {
        var presentFlags = new HashSet<IFishingRod.FishingFlag>();
        for (var flag : IFishingRod.FishingFlag.values())
            if (hook.hasMetadata(flag.name()))
                presentFlags.add(flag);
        return presentFlags;
    }

    /**
     * Retrieve the present flags from a fishing rod.
     * @param item The rod to query.
     * @return A set of {@link xyz.devvydont.smprpg.items.interfaces.IFishingRod.FishingFlag}s
     */
    public static Set<IFishingRod.FishingFlag> getFlagsFromItem(ItemStack item) {
        var presentFlags = new HashSet<IFishingRod.FishingFlag>();
        if (item == null || item.getType() == Material.AIR)
            return presentFlags;
        var blueprint = ItemService.blueprint(item);
        if (blueprint instanceof IFishingRod rod)
            presentFlags.addAll(rod.getFishingFlags());
        return presentFlags;
    }

    /**
     * Construct the fishing context using the fishing event. This provides us with everything we need.
     * @param event The {@link PlayerFishEvent} providing relevant context.
     */
    public FishingContext(PlayerFishEvent event) {
        this(event.getPlayer(), event.getHand(), event.getHook().getLocation(), getFlagsFromHook(event.getHook()));
    }

    /**
     * Construct the fishing context using various Bukkit objects.
     * @param player The player that is fishing.
     * @param hand The hand involved with this event. If this is null, then both hands will be used.
     * @param location The location to check. In a real fishing environment, this should be the hook's location.
     * @param flags The flags you want to EXPLICITLY set in this context. Passing in null will automatically work out flags based on passed in hand parameter.
     */
    public FishingContext(Player player, @Nullable EquipmentSlot hand, Location location, @Nullable Set<IFishingRod.FishingFlag> flags) {
        this.player = player;
        this.hand = hand;
        this.location = location;

        // If the hand is null, this is most likely being spoofed for rate displaying. Add flags in both hands.
        // Otherwise, just use the passed in slot.
        if (hand == null) {
            this.flags.addAll(getFlagsFromItem(player.getInventory().getItemInMainHand()));
            this.flags.addAll(getFlagsFromItem(player.getInventory().getItemInOffHand()));
        } else {
            this.flags.addAll(getFlagsFromItem(player.getInventory().getItem(hand)));
        }

        // If flags wasn't null, we are explicitly setting the flags. This is most likely called because the fishing hook overrode it.
        if (flags != null) {
            this.flags.clear();
            this.flags.addAll(flags);
        }
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

    public Set<IFishingRod.FishingFlag> getFlags() {
        return flags;
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
