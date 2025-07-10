package xyz.devvydont.smprpg.fishing.pools;

import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.fishing.loot.FishingLootBase;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.util.rng.WeightedSelector;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Constructed by passing in a custom loot type, and a context. It will automatically roll a fishing reward.
 */
public class FishingLootPool {

    private final FishingLootType type;
    private final FishingContext ctx;

    public FishingLootPool(FishingContext ctx, FishingLootType type) {
        this.ctx = ctx;
        this.type = type;
    }

    /**
     * The fishing loot pool that satisfies the loot type and context.
     * @return The loot pool this pool rolls from.
     */
    public Collection<FishingLootBase> getPool() {

        // Filter all the rewards for this type based on this context.
        var allRewards = FishingRewardRegistry.getRegisteredRewards(type);
        var filtered = new ArrayList<FishingLootBase>();
        for (var reward : allRewards)
            if (reward.passesAllRequirements(ctx))
                filtered.add(reward);
        return filtered;
    }

    /**
     * Randomly roll a reward from this pool using the supplied loot type and context.
     * @return A fishing loot instance. Returns null if no loot is available for this context.
     */
    public @Nullable WeightedSelector.Result<FishingLootBase> roll() {

        // If our pool is empty, return null.
        var pool = getPool();
        if (pool.isEmpty())
            return null;

        // Initialize our weighted RNG selector.
        var selector = new WeightedSelector<FishingLootBase>();
        for (var loot : pool)
            selector.add(loot, loot.getWeight());

        // Get an option.
        return selector.roll();
    }
}
