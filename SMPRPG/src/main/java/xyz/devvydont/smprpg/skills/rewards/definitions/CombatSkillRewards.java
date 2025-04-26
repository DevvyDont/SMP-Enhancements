package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class CombatSkillRewards extends SkillRewardContainer {

    public static final int STRENGTH_PER_LEVEL = 5;
    public static final int HP_PER_5_LEVEL = 10;

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;

    public int getStrengthForLevel(int level) {
        return STRENGTH_PER_LEVEL * level;
    }

    public int getHpForLevel(int level) {
        return Math.max(0, level * HP_PER_5_LEVEL / SECONDARY_LEVEL_DIFFERENCE);
    }

    @Override
    public void initializeRewards() {

        // Loop from 1-100 and add STR per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(AttributeWrapper.STRENGTH, AttributeModifier.Operation.ADD_SCALAR, getStrengthForLevel(i), getStrengthForLevel(i-1)));

        // Loop every 5 levels and add HP
        for (var i = SECONDARY_LEVEL_DIFFERENCE; i <= 100; i += SECONDARY_LEVEL_DIFFERENCE)
            addReward(i, new AttributeReward(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER,getHpForLevel(i), getHpForLevel(i-SECONDARY_LEVEL_DIFFERENCE)));
    }

}
