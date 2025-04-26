package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MiningSkillRewards extends SkillRewardContainer {


    public static final int HP_PER_LEVEL = 2;
    public static final int DEF_PER_5_LEVEL = 4;
    public static final int MINING_EFF_PER_4_LEVEL = 5;

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;
    public static final int THIRDARY_LEVEL_DIFFERENCE = 4;

    public int getHpForLevel(int level) {
        return HP_PER_LEVEL * level;
    }

    public int getDefForLevel(int level) {
        return level * DEF_PER_5_LEVEL / SECONDARY_LEVEL_DIFFERENCE;
    }

    public int getMiningEfficiencyForLevel(int level) {
        return level * MINING_EFF_PER_4_LEVEL / THIRDARY_LEVEL_DIFFERENCE;
    }

    @Override
    public void initializeRewards() {


        // Loop from 1-100 and add DEF per level
        for (int i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, getHpForLevel(i), getHpForLevel(i-1)));

        // Loop every 5 levels and add luck
        for (int i = DEF_PER_5_LEVEL; i <= 100; i += SECONDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(AttributeWrapper.LUCK, AttributeModifier.Operation.ADD_NUMBER, getDefForLevel(i), getDefForLevel(i-1)));

        // Loop every 4 levels and add mining eff
        for (int i = MINING_EFF_PER_4_LEVEL; i <= 100; i += THIRDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(AttributeWrapper.MINING_EFFICIENCY, AttributeModifier.Operation.ADD_SCALAR, getMiningEfficiencyForLevel(i), getMiningEfficiencyForLevel(i-1)));
    }

}
