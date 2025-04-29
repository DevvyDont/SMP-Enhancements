package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class FishingSkillRewards extends SkillRewardContainer {

    public static final int SECONDARY_STAT_LEVEL_DIFF = 5;

    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add DEF per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(
                    AttributeWrapper.DEFENSE,
                    AttributeModifier.Operation.ADD_NUMBER,
                    SkillGlobals.getStatPerLevel(SkillGlobals.DEF_PER_LEVEL, i),
                    SkillGlobals.getStatPerLevel(SkillGlobals.DEF_PER_LEVEL, i-1)
            ));

        // Loop every 5 levels and add LUCK
        for (var i = SECONDARY_STAT_LEVEL_DIFF; i <= 100; i += SECONDARY_STAT_LEVEL_DIFF)
            addReward(i, new AttributeReward(
                    AttributeWrapper.LUCK,
                    AttributeModifier.Operation.ADD_NUMBER,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.LUCK_PER_5_LEVELS, SECONDARY_STAT_LEVEL_DIFF, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.LUCK_PER_5_LEVELS, SECONDARY_STAT_LEVEL_DIFF, i-SECONDARY_STAT_LEVEL_DIFF)
            ));
    }

}
