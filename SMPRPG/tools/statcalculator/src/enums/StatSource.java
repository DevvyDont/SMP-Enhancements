package enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a source that a player can receive stats from. As a player starts to get further in the game,
 * there are many areas where they can increase their power as a player, and every source should have an expected
 * "contribution" or weight to their total stat pool.
 */
public enum StatSource {

    // Leveling up mining, combat, etc. Come in the form of small but frequent permanent stat boosts.
    SKILLS(0, 0, 35),

    // Gear. What they're wearing on their body and holding in their hands.
    ARMOR(5, 20, 35),
    CHARM(40, 50, 10),

    // Augments on gear. These have a scaling aspect, so they need to be considered.
    ENCHANTMENTS(20, 40, 20),
    ;

    /**
     * Represents the level that this stat source starts to matter.
     */
    public final int StartLevel;

    /**
     * Represents the minimum level needed in order for this source to be fully important.
     * This allows a gradual rise in importance up to a certain point to slowly introduce game mechanics.
     * Keep in mind that this range should be small enough to give room for the importance to gradually grow,
     * but also large enough so that it doesn't cause a sharp weight jump for other factors.
     */
    public final int EndLevel;
    public final int Importance;

    StatSource(int startLevel, int endLevel, int importance) {
        StartLevel = startLevel;
        EndLevel = endLevel;
        Importance = importance;
    }

    public int CalculateWeight(int level) {

        // If the level is already considered to be at max importance, just return that.
        if (level >= EndLevel)
            return Importance;

        // If the level is below the start level, it is not important at all.
        if (level < StartLevel)
            return 0;

        // Work out how many levels above the start level we are at relative to the level of max effectiveness.
        // Multiply this result by the importance.
        var levelRange = EndLevel - StartLevel;
        var levelsAboveStart = level - StartLevel + 1;
        var effectiveness = (float)levelsAboveStart / levelRange;
        return Math.round(effectiveness * Importance);
    }

    /**
     * Given a specific level, determine what percentage the player's setup should contribute to the expected player's stats.
     * Our program will spit out the expected stats of a player at a certain level, but we need to further break this
     * down by determining how much of that pool is allocated to various sources.
     * @param level
     * @return
     */
    public static Map<StatSource, Double> generateStatDistribution(int level) {
        var map = new HashMap<StatSource, Double>();
        var totalWeight = 0;

        // First work out the total weight.
        for (var stat : StatSource.values())
            totalWeight += stat.CalculateWeight(level);

        // Now that we have the total weight, we can start actually inputting percentages.
        for (var stat : StatSource.values()) {
            var weight = (double)stat.CalculateWeight(level) / totalWeight;
            map.put(stat, weight);
        }

        return map;
    }

    /**
     * A one off function, but still technically a stat source. How much damage should a weapon be expected to do?
     * Weapons will linearly increase in power from 10-500 base DPS, but a player's stats will do most of the heavy
     * lifting. It's good to define a solid anchor point however.
     * @param level
     * @param dpsMultiplier
     * @return
     */
    public static int getExpectedWeaponDamage(int level, ItemRarity rarity, double dpsMultiplier) {
        double effectiveness = Math.pow(level, 1.75) * dpsMultiplier;
        return (int) Math.round(effectiveness * rarity.Budget);
    }

}
