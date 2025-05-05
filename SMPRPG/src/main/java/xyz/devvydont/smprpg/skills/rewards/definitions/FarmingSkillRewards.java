package xyz.devvydont.smprpg.skills.rewards.definitions;

import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;

public class FarmingSkillRewards extends SkillRewardContainer {

    public static final int SECONDARY_STAT_LEVEL_DIFF = 5;

    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add HP per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(
                    AttributeWrapperLegacy.HEALTH,
                    SkillGlobals.DEFAULT_SKILL_OPERATION,
                    SkillGlobals.getStatPerLevel(SkillGlobals.HP_PER_LEVEL, i),
                    SkillGlobals.getStatPerLevel(SkillGlobals.HP_PER_LEVEL, i-1)
            ));

        // Loop every 5 levels and add STR
        for (var i = SECONDARY_STAT_LEVEL_DIFF; i <= 100; i += SECONDARY_STAT_LEVEL_DIFF)
            addReward(i, new AttributeReward(
                    AttributeWrapperLegacy.STRENGTH,
                    SkillGlobals.STRENGTH_SKILL_OPERATION,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.STR_PER_5_LEVELS, SECONDARY_STAT_LEVEL_DIFF, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.STR_PER_5_LEVELS, SECONDARY_STAT_LEVEL_DIFF, i-SECONDARY_STAT_LEVEL_DIFF)
            ));
    }


}
