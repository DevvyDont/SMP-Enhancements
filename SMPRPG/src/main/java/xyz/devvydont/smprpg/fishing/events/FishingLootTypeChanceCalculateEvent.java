package xyz.devvydont.smprpg.fishing.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;

import java.util.HashMap;
import java.util.Map;

/**
 * Fires when the plugin is attempting to calculate the chances of a {@link FishingLootType}
 * being rolled before actually rolling it. This event can be modified to adjust the chances present before any drops
 * occur. This means you can override this event one time, and queries and actual fishing rolls will both be affected
 * as this method is called for simple queries and fish loot generation.
 */
public class FishingLootTypeChanceCalculateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private @NotNull final FishingContext context;
    private @NotNull Map<FishingLootType, Double> chances;

    public FishingLootTypeChanceCalculateEvent(@NotNull FishingContext context, @NotNull Map<FishingLootType, Double> chances) {
        this.context = context;
        this.chances = chances;
    }

    /**
     * Get the fishing context that was used to generate the original event.
     * @return A {@link FishingContext} instance.
     */
    public @NotNull FishingContext getContext() {
        return context;
    }

    /**
     * Get the chances of every fishing loot type to occur. This will start out with intended starting values, but
     * it is mutable and can be modified. Because of this, a fishing loot type is not guaranteed to be present.
     * @return A mutable map of all the chances of loot currently.
     */
    public @NotNull Map<FishingLootType, Double> getChances() {
        return chances;
    }

    /**
     * Overrides the rates for the types of loot that can occur from this event, if it rolls loot.
     * Exists for convenience.
     * @param chances The desired chances for loot types for this event. Pass in null to result in no rewards possible.
     */
    public void setChances(@Nullable Map<FishingLootType, Double> chances) {

        // Do not allow null. We allow null parameter for convenience.
        var update = new HashMap<FishingLootType, Double>();
        if (chances != null)
            update.putAll(chances);

        this.chances = update;
    }

    /**
     * Convenience method to retrieve the chance for a fishing loot type. This is a shortcut method for
     * event.getChances().getOrDefault(type, 0).
     * @param type The type of fishing loot that you want to know.
     * @return The chance of the type of fishing loot occurring. Returns 0 if not present.
     */
    public double getChance(FishingLootType type) {
        return getChances().getOrDefault(type, 0.0);
    }

    /**
     * Convenience method to set the chance for a fishing loot type. This is a shortcut method for
     * event.getChances().put(type, 0).
     * @param type The chance of the type of fishing loot that you want to set.
     * @@param chance The chance of the type of fishing loot occurring. Set to 0 if you don't want it to be available.
     */
    public void setChance(FishingLootType type, double chance) {
        this.chances.put(type, chance);
    }
}

