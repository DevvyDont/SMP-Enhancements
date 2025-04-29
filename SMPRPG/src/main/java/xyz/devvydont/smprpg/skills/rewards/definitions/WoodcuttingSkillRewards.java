package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class WoodcuttingSkillRewards extends SkillRewardContainer {

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;

    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add STR per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(
                    AttributeWrapper.STRENGTH,
                    AttributeModifier.Operation.ADD_SCALAR,
                    SkillGlobals.getStatPerLevel(SkillGlobals.STR_PER_LEVEL, i),
                    SkillGlobals.getStatPerLevel(SkillGlobals.STR_PER_LEVEL, i-1)
            ));

        // Loop every 5 levels and add HP
        for (var i = SECONDARY_LEVEL_DIFFERENCE; i <= 100; i += SECONDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(
                    AttributeWrapper.HEALTH,
                    AttributeModifier.Operation.ADD_NUMBER,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.HP_PER_5_LEVELS, SECONDARY_LEVEL_DIFFERENCE, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.HP_PER_5_LEVELS, SECONDARY_LEVEL_DIFFERENCE, i-SECONDARY_LEVEL_DIFFERENCE)
            ));
    }

}
