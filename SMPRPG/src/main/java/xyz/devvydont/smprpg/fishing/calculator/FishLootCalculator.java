package xyz.devvydont.smprpg.fishing.calculator;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.fishing.loot.FishingLootBase;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;
import xyz.devvydont.smprpg.fishing.loot.FishingLootTypeSelector;
import xyz.devvydont.smprpg.fishing.loot.ItemStackFishingLoot;
import xyz.devvydont.smprpg.fishing.pools.FishingLootPool;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.rng.WeightedSelector;

/**
 * Simple class that provides any necessary fish loot pool calculations. If you just want to simulate a fishing
 * reward roll, simply just call roll()!
 */
public class FishLootCalculator {

    /**
     * Contains the resulting context for a fish loot calculation. Can be used to obtain the probabilities of the
     * loot selection events that occurred.
     * @param Type The fishing loot type that was used to determine the loot pool.
     * @param Reward The actual reward that was selected.
     */
    public record CalculationResult(WeightedSelector.Result<FishingLootType> Type, WeightedSelector.Result<FishingLootBase> Reward) {

        /**
         * Calculate the final probability of this event from occurring. This is a compound probability of type and item.
         * @return A calculated final probability. Represents the probability to get this item from any fishing cast.
         */
        public double probability() {
            return Type.Probability() * Reward.Probability();
        }
    }

    /**
     * We want to make sure we always supply something when we roll for an item. If we fail to roll an item, just
     * supply a dead bush. If there is no loot available, we can assume this is a "dead" catch I guess...
     * @return A fallback fishing loot item.
     */
    private CalculationResult fallback() {
        return new CalculationResult(
                new WeightedSelector.Result<>(FishingLootType.JUNK, 1.0),
                new WeightedSelector.Result<>(new ItemStackFishingLoot.Builder(Material.DEAD_BUSH).build(), 1.0)
        );
    }

    /**
     * Rolls a fishing loot item.
     * @param ctx {@link FishingContext} instance that provides context from fishing events.
     * @return The loot to give.
     */
    public @NotNull CalculationResult roll(FishingContext ctx) {

        // First, roll the desired type.
        var typeSelector = new FishingLootTypeSelector(ctx);
        var rolledType = typeSelector.roll();

        // Based on the type, generate a loot pool.
        var pool = new FishingLootPool(ctx, rolledType.Element());
        var loot = pool.roll();

        // If we found an item, great!
        if (loot != null)
            return new CalculationResult(rolledType, loot);

        // Otherwise, send the fallback. Also, alert the plugin. Ideally we don't want fishing duds to occur.
        SMPRPG.broadcastToOperatorsCausedBy(ctx.getPlayer(), ComponentUtils.create("Failed to generate fish loot. ctx: " + ctx));
        return fallback();
    }

}
