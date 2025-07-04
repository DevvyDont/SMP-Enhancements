package xyz.devvydont.smprpg.skills.rewards.definitions;

import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.CoinReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

public class WoodcuttingSkillRewards extends SkillRewardContainer {

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;

    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add STR per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(
                    AttributeWrapper.STRENGTH,
                    SkillGlobals.STRENGTH_SKILL_OPERATION,
                    SkillGlobals.getStatPerLevel(SkillGlobals.STR_PER_LEVEL, i),
                    SkillGlobals.getStatPerLevel(SkillGlobals.STR_PER_LEVEL, i-1)
            ));

        // Loop every 5 levels and add HP
        for (var i = SECONDARY_LEVEL_DIFFERENCE; i <= 100; i += SECONDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(
                    AttributeWrapper.HEALTH,
                    SkillGlobals.DEFAULT_SKILL_OPERATION,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.HP_PER_5_LEVELS, SECONDARY_LEVEL_DIFFERENCE, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.HP_PER_5_LEVELS, SECONDARY_LEVEL_DIFFERENCE, i-SECONDARY_LEVEL_DIFFERENCE)
            ));

        // Give coins for every level.
        for (var i = 1; i <= SkillGlobals.getMaxSkillLevel(); i++)
            addReward(i, new CoinReward(SkillGlobals.getCoinRewardForLevel(i)));
    }

}
