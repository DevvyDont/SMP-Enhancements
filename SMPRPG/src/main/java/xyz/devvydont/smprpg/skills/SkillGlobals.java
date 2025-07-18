package xyz.devvydont.smprpg.skills;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.SMPRPG;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class SkillGlobals {

    /**
     * Returns the max skill level as defined by the plugin's config.yml.
     * @return The max skill level.
     */
    public static int getMaxSkillLevel() {
        return SMPRPG.getInstance().getConfig().getInt("max-skill-level", 100);
    }

    /**
     * Returns the total experience needed to hit the current skill experience cap.
     * @return Total experience.
     */
    public static int getTotalExperienceCap() {
        return getCumulativeExperienceForLevel(getMaxSkillLevel());
    }

    /**
     * A static cache for cumulative experience requirements. This is due to the fact that experience has a
     * recursive relationship, as most experience formulas are.
     */
    private static final Map<Integer, Integer> cumulativeExperienceCache = new HashMap<>();

    /**
     * Gets the TOTAL amount of experience required for a level. Works as a lifetime experience value as opposed to
     * a current level experience.
     * @param level The level of experience to calculate.
     * @return The cumulative experience to reach that level.
     */
    public static int getCumulativeExperienceForLevel(int level) {

        // If we are attempting to find experience for a level that is not valid (base case also)
        if (level <= 0)
            return 0;

        // If we previously cached the answer for this level use that
        if (cumulativeExperienceCache.containsKey(level))
            return cumulativeExperienceCache.get(level);

        // Recursively add the experience from previous levels to this one
        int experience = getExperienceForLevel(level) + getCumulativeExperienceForLevel(level-1);
        cumulativeExperienceCache.put(level, experience);
        return experience;
    }

    /**
     * Returns the amount of experience required to level up when starting at a certain level.
     * Keep in mind, this is not cumulative. If you need total experience, use {@link SkillGlobals#getCumulativeExperienceForLevel(int)}
     * @param level The level to calculate experience for.
     * @return The experience needed.
     */
    public static int getExperienceForLevel(int level) {
        return dropIntegerPrecision((int) (Math.pow(level+1, 3) + 92), 2);
    }

    /**
     * Works as a way to make larger numbers appear pretty, even though we are doing pretty normal formulas.
     * For example, if you pass in 12,345, you could receive 12,000.
     * @param n The number to drop precision for, e.g. 12,345.
     * @param numSignificantDigits How many numbers you want to keep for the left side of the number.
     * @return The final number. For example, if you pass in 12,345 w/ 2 significant digits, you get 12,000.
     */
    public static int dropIntegerPrecision(int n, int numSignificantDigits) {
        BigDecimal bd = new BigDecimal(n);
        bd = bd.round(new MathContext(numSignificantDigits));
        return bd.intValue();
    }

    /**
     * Given a CUMULATIVE experience amount, return the level that it represents.
     * @param experience The total experience to use for calculating.
     * @return The level that this experience represents.
     */
    public static int getLevelForExperience(int experience) {

        // Loop through the experience cumulative values until we haven't hit a threshold yet
        for (int i = 0; i <= getMaxSkillLevel(); i++)
            if (experience < getCumulativeExperienceForLevel(i+1))
                return i;

        // Our experience was never lower than the max level cumulative experience requirement, we must be max level
        return getMaxSkillLevel();
    }

    /**
     * Calculates how many coins we should give for a certain level.
     * Overall, this right here probably defines our goal for how we want the economy to look since there are only
     * two main ways to introduce money into the economy.
     * - Skill level ups
     * - Selling items
     * We should define a total target of what we want a maxed player to obtain by maxing all skills, and distribute
     * the coins accordingly. We can also tie the amount of coins to experience for something more realistic.
     * @param level The skill level to award coins for.
     * @return How many coins should be awarded.
     */
    public static int getCoinRewardForLevel(int level) {
        return getExperienceForLevel(level) / 2;
    }

    /**
     * Strength/attack damage is a very strange attribute for us to handle.
     * <p>
     * Weapons will use the additive modifiers for base attack damage, which makes sense.
     * <p>
     * Gear will use scalar modifiers since there should realistically be no more than 6 active at a time, all with
     * equal weighting.
     * <p>
     * Skills will use multiplicative modifiers so that the small increments still feel meaningful at any point in
     * the game.
     * <p>
     * The reason it is set up this way is due to the fact that if skills were scalar, any sort of scalar additions
     * from any other source would feel meaningless due to the shear amount of expected DPS of a player. With only
     * gear respecting the fact that it is scalar, the numbers will *feel* more impactful. The old issue that we faced
     * was when armor was adding +50% scalar attack damage to a player that already had ~1000% scalar attack damage
     * from skills. This made it seem like the +50% damage literally wasn't affecting your damage.
     * Another benefit we receive by making gear scalar, is that the calculations on the items from enchantments and
     * reforges will actually mathematically make sense since multiplicative stacking doesn't translate well.
     * Here is an example of what a potential item display could look like and why it can be confusing:
     * ((200) 100 + 50% + 50% = 100 + 100% = 200) vs ((200) 100 + 50% + 50% = 100 * 1.5 * 1.5 = 225)
     */
    public static AttributeModifier.Operation STRENGTH_SKILL_OPERATION = AttributeModifier.Operation.MULTIPLY_SCALAR_1;

    // Typically, we give HP for every skill, every certain amount of levels. Define that here.
    public static int HP_LEVEL_FREQUENCY = 5;
    public static double HP_PER_5_LEVELS = 2;

    // The combat skill improves some damage related attributes.
    public static double STR_PER_LEVEL = 5;
    public static double CRITICAL_CHANCE_PER_LEVEL = 0.25;
    public static double CRITICAL_RATING_PER_4_LEVELS = 4;
    public static int CRITICAL_RATING_LEVEL_FREQUENCY = 4;

    // All foraging skills give fortune for their respective attribute.
    public static final double FORTUNE_PER_LEVEL = 3;

    // Farming gives regeneration every 2 levels.
    public static final double REGENERATION_PER_2_LEVELS = 2;
    public static final int REGENERATION_LEVEL_FREQUENCY = 2;

    // Fishing skill gives fishing chances every 4 levels.
    public static double FISHING_CHANCE_PER_4_LEVEL = 0.2;
    public static int FISHING_CHANCE_FREQUENCY = 4;

    // The magic skill gives intelligence and luck.
    public static double INT_PER_LEVEL = 5;
    public static double LUCK_PER_4_LEVELS = 4;
    public static int LUCK_LEVEL_FREQUENCY = 4;

    // The mining skill awards mining efficiency every 4 levels.
    public static int MINING_EFF_LEVEL_FREQUENCY = 4;
    public static double MINING_EFF_PER_4_LEVELS = 5;

    // Woodcutting gives a small defense and critical rating boost.
    public static int DEFENSE_LEVEL_FREQUENCY = 2;
    public static double DEFENSE_PER_2_LEVELS = 4;

    /**
     * Used for scaling attribute rewards. Higher levels will result in better attributes.
     * @param level The level this attribute resides at.
     * @return The expected multiplier.
     */
    public static int getLevelMultiplier(int level) {
        return (level-1) / 10 + 1;
    }

    /**
     * Represents skills that reward stats at every level.
     * @param base The base amount. Typically, what's awarded at levels 1-9.
     * @param level The level of the skill.
     * @return How effective the stat should be.
     */
    public static double getScalingStatPerLevel(double base, int level) {

        // Base case, level 0 means no stats duh
        if (level <= 0)
            return 0;

        // Add the stat that we had from last level, and add to it.
        // todo: memoize this, i imagine this is a lot of gross recursive math in later levels.
        var addition = base * getLevelMultiplier(level);
        return getScalingStatPerLevel(base, level-1) + addition;
    }

    /**
     * Represents skills that reward stats only at certain level increments, which is the 'x' parameter.
     * This does the same thing as getStatPerLevel(), but there's an additional step where we divide by x.
     * @param base The base stat increment modifier.
     * @param x The difference in levels the stat is expected to be given.
     * @param level The level to calculate for.
     * @return How effective the stat should be.
     */
    public static double getScalingStatPerXLevel(double base, int x, int level) {

        // Base case, level 0 means no stats duh
        if (level <= 0)
            return 0;

        // Add the stat that we had from the previous tier to this one.
        var addition = base * getLevelMultiplier(level);
        return getScalingStatPerXLevel(base, x, level-x) + addition;
    }

}
