package xyz.devvydont.smprpg.skills.rewards;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class FarmingSkillRewards extends SkillRewardContainer {

    public static final int HP_PER_LEVEL = 2;

    public static final int SECONDARY_STAT_LEVEL_DIFF = 2;
    public static final int STR_PER_2_LEVEL = 4;

    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2 HP per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.HEALTH, HP_PER_LEVEL));

        // Loop every 2 levels and add 4 STR
        for (int i = 1; i < 100/SECONDARY_STAT_LEVEL_DIFF; i++)
            addReward(new StaticRewardAttribute(i*SECONDARY_STAT_LEVEL_DIFF, AttributeWrapper.STRENGTH, i*STR_PER_2_LEVEL, (i-1)*STR_PER_2_LEVEL, AttributeModifier.Operation.ADD_SCALAR));

        int MAX_HP = HP_PER_LEVEL * 100;
        int HP_98 = HP_PER_LEVEL * 98;
        int MAX_STR = STR_PER_2_LEVEL * (100 / SECONDARY_STAT_LEVEL_DIFF);
        int STR_98 = STR_PER_2_LEVEL * (100 / SECONDARY_STAT_LEVEL_DIFF - 1);
        addReward(new StaticRewardAttribute(99, AttributeWrapper.HEALTH, MAX_HP, HP_98));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.STRENGTH, MAX_STR, STR_98, AttributeModifier.Operation.ADD_SCALAR));
    }


}
