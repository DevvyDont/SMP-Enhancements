package xyz.devvydont.smprpg.skills.rewards;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class WoodcuttingSkillRewards extends SkillRewardContainer {

    public static final int STRENGTH_PER_LEVEL = 5;
    public static final int HP_PER_5_LEVEL = 10;

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;

    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 3% STR per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.STRENGTH, STRENGTH_PER_LEVEL, AttributeModifier.Operation.ADD_SCALAR));

        // Loop every 5 levels and add 10 HP
        for (int i = 1; i < 100/SECONDARY_LEVEL_DIFFERENCE; i++)
            addReward(new StaticRewardAttribute(i*SECONDARY_LEVEL_DIFFERENCE, AttributeWrapper.HEALTH, i*HP_PER_5_LEVEL, (i-1)*HP_PER_5_LEVEL));

        // Add the maxed reward
        int MAX_STR = STRENGTH_PER_LEVEL * 100;
        int MAX_HP = HP_PER_5_LEVEL * (100 / SECONDARY_LEVEL_DIFFERENCE);
        int STR_98 = STRENGTH_PER_LEVEL * 98;
        int HP_98 = HP_PER_5_LEVEL * (100 / SECONDARY_LEVEL_DIFFERENCE - 1);
        addReward(new StaticRewardAttribute(99, AttributeWrapper.STRENGTH, MAX_STR, STR_98, AttributeModifier.Operation.ADD_SCALAR));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.HEALTH, MAX_HP, HP_98));
    }

}
