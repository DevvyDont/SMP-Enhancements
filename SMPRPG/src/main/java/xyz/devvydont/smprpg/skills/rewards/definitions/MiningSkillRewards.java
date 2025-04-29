package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MiningSkillRewards extends SkillRewardContainer {

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;
    public static final int THIRDARY_LEVEL_DIFFERENCE = 4;


    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add HP per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(
                    AttributeWrapper.HEALTH,
                    AttributeModifier.Operation.ADD_NUMBER,
                    SkillGlobals.getStatPerLevel(SkillGlobals.HP_PER_LEVEL, i),
                    SkillGlobals.getStatPerLevel(SkillGlobals.HP_PER_LEVEL, i-1)
            ));

        // Loop every 5 levels and add DEF
        for (var i = SECONDARY_LEVEL_DIFFERENCE; i <= 100; i += SECONDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(
                    AttributeWrapper.DEFENSE,
                    AttributeModifier.Operation.ADD_NUMBER,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.DEF_PER_5_LEVELS, SECONDARY_LEVEL_DIFFERENCE, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.DEF_PER_5_LEVELS, SECONDARY_LEVEL_DIFFERENCE, i-SECONDARY_LEVEL_DIFFERENCE)
            ));

        // Loop every 4 levels and add mining eff
        for (int i = THIRDARY_LEVEL_DIFFERENCE; i <= 100; i += THIRDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(
                    AttributeWrapper.MINING_EFFICIENCY,
                    AttributeModifier.Operation.ADD_SCALAR,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.MINING_EFF_PER_4_LEVELS, THIRDARY_LEVEL_DIFFERENCE, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.MINING_EFF_PER_4_LEVELS, THIRDARY_LEVEL_DIFFERENCE, i-THIRDARY_LEVEL_DIFFERENCE)
            ));
    }

}
