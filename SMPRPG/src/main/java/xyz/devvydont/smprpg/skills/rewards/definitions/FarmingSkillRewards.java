package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class FarmingSkillRewards extends SkillRewardContainer {

    public static final int HP_PER_LEVEL = 2;

    public static final int SECONDARY_STAT_LEVEL_DIFF = 2;
    public static final int STR_PER_2_LEVEL = 4;

    public int getHpForLevel(int level) {
        return HP_PER_LEVEL * level;
    }

    public int getStrengthForLevel(int level) {
        return level * STR_PER_2_LEVEL / SECONDARY_STAT_LEVEL_DIFF;
    }

    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add HP per level
        for (int i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, getHpForLevel(i), getHpForLevel(i-1)));

        // Loop every 2 levels and add 4 STR
        for (int i = SECONDARY_STAT_LEVEL_DIFF; i <= 100; i += SECONDARY_STAT_LEVEL_DIFF)
            addReward(i, new AttributeReward(AttributeWrapper.STRENGTH, AttributeModifier.Operation.ADD_SCALAR, getStrengthForLevel(i), getStrengthForLevel(i-1)));
    }


}
