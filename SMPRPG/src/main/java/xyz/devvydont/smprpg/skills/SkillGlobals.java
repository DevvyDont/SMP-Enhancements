package xyz.devvydont.smprpg.skills;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.config.ConfigManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class SkillGlobals {

    public static int getMaxSkillLevel() {
        return SMPRPG.getInstance().getConfig().getInt(ConfigManager.OPTION_MAX_LEVEL, 100);
    }

    /**
     * Returns the total experience needed to hit the current skill experience cap
     *
     * @return
     */
    public static int getTotalExperienceCap() {
        return getCumulativeExperienceForLevel(getMaxSkillLevel());
    }

    private static final Map<Integer, Integer> cumulativeExperienceCache = new HashMap<>();

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
     * Returns the amount of experience required to level up when starting at a certain level
     *
     * @param level
     * @return
     */
    public static int getExperienceForLevel(int level) {
        return dropIntegerPrecision((int) (Math.pow(level+1, 3) + 92), 2);
    }

    public static int dropIntegerPrecision(int n, int numSignificantDigits) {
        BigDecimal bd = new BigDecimal(n);
        bd = bd.round(new MathContext(numSignificantDigits));
        return bd.intValue();
    }

    public static int getLevelForExperience(int experience) {

        // Loop through the experience cumulative values until we haven't hit a threshold yet
        for (int i = 0; i <= getMaxSkillLevel(); i++)
            if (experience < getCumulativeExperienceForLevel(i+1))
                return i;

        // Our experience was never lower than the max level cumulative experience requirement, we must be max level
        return getMaxSkillLevel();
    }

    /**
     * Strength/attack damage is a very strange attribute for us to handle.
     *
     * Weapons will use the additive modifiers for base attack damage, which makes sense.
     *
     * Gear will use scalar modifiers since there should realistically be no more than 6 active at a time, all with
     * equal weighting.
     *
     * Skills will use multiplicative modifiers so that the small increments still feel meaningful at any point in
     * the game.
     *
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
    public static AttributeModifier.Operation STRENGTH_GEAR_OPERATION = AttributeModifier.Operation.ADD_SCALAR;
    public static AttributeModifier.Operation STRENGTH_SKILL_OPERATION = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
    public static AttributeModifier.Operation DEFAULT_SKILL_OPERATION = AttributeModifier.Operation.ADD_NUMBER;

    // For skills that award HP per level, how much should we start at? This will scale as levels get higher as well.
    public static double HP_PER_LEVEL = 2;

    // For skills that award HP per 5 levels, how much should we start at? This will scale as levels get higher as well.
    public static double HP_PER_5_LEVELS = 5;

    // For skills that award DEF per level, how much should we start at? This will scale as levels get higher as well.
    public static double DEF_PER_LEVEL = 1;

    // For skills that award DEF per 5 levels, how much should we start at? This will scale as levels get higher as well.
    public static double DEF_PER_5_LEVELS = 2;

    // For skills that award STR per level, how much should we start at? This will scale as levels get higher as well.
    public static double STR_PER_LEVEL = 1;

    // For skills that award STR per 5 levels, how much should we start at? This will scale as levels get higher as well.
    public static double STR_PER_5_LEVELS = 3;

    // For skills that award LUCK per level, how much should we start at? This will scale as levels get higher as well.
    public static double LUCK_PER_LEVEL = 2;

    // For skills that award LUCK per 5 levels, how much should we start at? This will scale as levels get higher as well.
    public static double LUCK_PER_5_LEVELS = 1;

    public static double MINING_EFF_PER_4_LEVELS = 5;

    public static int getLevelMultiplier(int level) {
        return (level-1) / 20 + 1;
    }

    /**
     * Represents skills that reward stats at every level.
     * @param base The base amount. Typically, what's awarded at levels 1-9.
     * @param level The level of the skill.
     * @return How effective the stat should be.
     */
    public static double getStatPerLevel(double base, int level) {
        return base * level * getLevelMultiplier(level);
    }

    /**
     * Represents skills that reward stats only at certain level increments, which is the 'x' parameter.
     * This does the same thing as getStatPerLevel(), but there's an additional step where we divide by x.
     * @param base The base stat increment modifier.
     * @param x The difference in levels the stat is expected to be given.
     * @param level The level to calculate for.
     * @return How effective the stat should be.
     */
    public static double getStatPerXLevel(double base, int x, int level) {
        return Math.max(0, level * base / x * getLevelMultiplier(level));
    }

}
