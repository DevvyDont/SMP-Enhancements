package xyz.devvydont.smprpg.fishing.loot;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.fishing.FishingConstants;
import xyz.devvydont.smprpg.fishing.events.FishingLootTypeChanceCalculateEvent;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.util.rng.WeightedSelector;

import java.util.HashMap;
import java.util.Map;

/**
 * A higher level class that contains inner loot pools. Fishing is a bit complicated, as you first roll for a "type"
 * of drop (trash, fish, treasure etc.), then roll for a drop within that type's pool. This pool is also dynamically
 * created every single time you want to get a drop, as it is context dependant. Simply construct an instance of this
 * with {@link FishingContext}. Because of this behavior, you can also "simulate" drops, or even query information about
 * the underlying pools.
 */
public class FishingLootTypeSelector {

    private final FishingContext context;

    public FishingLootTypeSelector(FishingContext context) {
        this.context = context;
    }

    /**
     * Get the context this instance started with.
     * @return A {@link FishingContext} instance.
     */
    public FishingContext getContext() {
        return context;
    }

    /**
     * Calculates the base chance of fishing up an item type ONLY using the context given. This chance is considered
     * the *base* chance, as other plugins cannot modify the result of this method. This method is perfect if you
     * want to query the base chance of a fishing type without alerting other plugins, but its main purpose is to aid
     * in construction of the map returned by {@link FishingLootTypeSelector#getLootTypeChances()}.
     * @param type The type of fishing loot you want to know the base fish up chance of.
     * @return The base chance for the fishing loot type to occur.
     */
    public double getBaseChance(FishingLootType type) {
        // Pretty simple, treasure/sea creature are directly related to their attributes and junk is tied to luck.
        return switch (type) {
            case CREATURE -> queryAttributeChance(AttributeWrapper.FISHING_CREATURE_CHANCE);
            case TREASURE -> queryAttributeChance(AttributeWrapper.FISHING_TREASURE_CHANCE);
            case JUNK -> FishingConstants.BASE_JUNK_CHANCE * getJunkLuckMultiplier();
            // Fish is special. It simply just fills in the gap from the other 3.
            case FISH -> 100.0 - getBaseChance(FishingLootType.JUNK) - getBaseChance(FishingLootType.TREASURE) - getBaseChance(FishingLootType.CREATURE);
            default -> 0;
        };
    }

    /**
     * Given the context, calculates the chances of the different types of loot that can be fished up.
     * This event will also fire the {@link xyz.devvydont.smprpg.fishing.events.FishingLootTypeChanceCalculateEvent}
     * event, allowing plugins to modify the chances of loot types. This can be useful if certain items or unique
     * mechanics modify the chances item types can occur.
     * @return The loot chances of different fishing item types occurring. Represents the final processed chances after
     * plugins have modified the chances.
     */
    public Map<FishingLootType, Double> getLootTypeChances() {

        // Start constructing base loot type chances.
        HashMap<FishingLootType, Double> lootTypeChances = new HashMap<>();
        for (var type : FishingLootType.values())
            lootTypeChances.put(type, getBaseChance(type));

        // Call the event. Let the rest of the plugin make modifications.
        var event = new FishingLootTypeChanceCalculateEvent(context, lootTypeChances);
        event.callEvent();

        // Done!
        return lootTypeChances;
    }

    /**
     * Rolls for a fishing loot type based on the context provided. Essentially a shortcut method for instantiating a
     * {@link xyz.devvydont.smprpg.util.rng.WeightedSelector} and doing the work manually.
     * @return A random fishing loot type based on the context.
     */
    public WeightedSelector.Result<FishingLootType> roll() {

        // Straightforward, create a weighted selector, add the options, and roll.
        var selector = new WeightedSelector<FishingLootType>();
        for (var entry : getLootTypeChances().entrySet())
            selector.add(entry.getKey(), entry.getValue());
        return selector.roll();
    }

    /**
     * Helper method to retrieve a "chance" stat from an attribute.
     * @param wrapper The attribute to query. Should be either sea creature or treasure chance.
     * @return
     */
    private double queryAttributeChance(AttributeWrapper wrapper) {
        // Get the attribute. If it's null, assume 0. If they have it, it's just the final value.
        var attr = SMPRPG.getService(AttributeService.class).getAttribute(context.getPlayer(), wrapper);
        return attr != null ? attr.getValue() : 0;
    }

    /**
     * Helper method to determine the multiplier to apply to the junk loot type.
     * @return A multiplier to apply to the junk chance.
     */
    private double getJunkLuckMultiplier() {

        // Get their luck. If they don't have it, we don't adjust the chance.
        var luck = SMPRPG.getService(AttributeService.class).getAttribute(context.getPlayer(), AttributeWrapper.LUCK);
        if (luck == null)
            return 1.0;

        // 100 luck represents no change. 200 luck represents x0.5. 50 luck represents x2. You get the idea.
        var attributeValue = luck.getValue();

        // Avoid strange behavior from insanely low/negative/zero luck. We will cap junk out at 5x to be safe.
        if (attributeValue <= 0)
            return 5;

        // Cap it at 5x, but let higher values do whatever we want to the multiplier.
        return Math.min(5, 100.0 / attributeValue);
    }
}
