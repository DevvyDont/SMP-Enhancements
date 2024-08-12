package xyz.devvydont.smprpg.skills;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.config.ConfigManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class SkillGlobals {

    public static int getMaxSkillLevel() {
        return SMPRPG.getInstance().getConfig().getInt(ConfigManager.OPTION_MAX_LEVEL, 99);
    }

    /**
     * Returns the total experience needed to hit the current skill experience cap
     *
     * @return
     */
    public static int getTotalExperienceCap() {
        return getCumulativeExperienceForLevel(getMaxSkillLevel());
    }

    private static Map<Integer, Integer> cumulativeExperienceCache = new HashMap<>();

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


    /**
     * Returns the total amount of experience required to reach the next level
     *
     * @param level
     * @return
     */
    public static int getTotalExperienceForNextLevel(int level) {
        return getCumulativeExperienceForLevel(level + 1);
    }

    public static int getExperienceForNextLevel(int level) {
        return getExperienceForNextLevel(level + 1);
    }

    public static int getLevelForExperience(int experience) {

        // Loop through the experience cumulative values until we haven't hit a threshold yet
        for (int i = 0; i <= getMaxSkillLevel(); i++)
            if (experience < getCumulativeExperienceForLevel(i+1))
                return i;

        // Our experience was never lower than the max level cumulative experience requirement, we must be max level
        return getMaxSkillLevel();
    }

}
