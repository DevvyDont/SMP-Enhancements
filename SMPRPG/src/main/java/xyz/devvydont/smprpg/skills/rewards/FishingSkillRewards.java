package xyz.devvydont.smprpg.skills.rewards;

import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class FishingSkillRewards extends SkillRewardContainer {

    public static final int DEF_PER_LEVEL = 2;

    public static final int LUCK_PER_4_LEVEL = 2;
    public static final int SECONDARY_STAT_LEVEL_DIFF = 4;

    @Override
    public void initializeRewards() {

        // Loop every 2 levels and add 7 DEF
        for (int i = 1; i < 100 / DEF_PER_LEVEL; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.DEFENSE, DEF_PER_LEVEL));

        // Loop every 4 levels and add 2 luck
        for (int i = 1; i < 100 / SECONDARY_STAT_LEVEL_DIFF; i++)
            addReward(new StaticRewardAttribute(i * SECONDARY_STAT_LEVEL_DIFF, AttributeWrapper.LUCK, i * LUCK_PER_4_LEVEL, (i - 1) * LUCK_PER_4_LEVEL));

        // Add the maxed reward
        int MAX_DEF = DEF_PER_LEVEL * 100;
        int DEF_98 = DEF_PER_LEVEL * 98;
        int MAX_LUCK = LUCK_PER_4_LEVEL * (100 / SECONDARY_STAT_LEVEL_DIFF);
        int LUCK_98 = LUCK_PER_4_LEVEL * (100 / SECONDARY_STAT_LEVEL_DIFF - 1);
        addReward(new StaticRewardAttribute(99, AttributeWrapper.DEFENSE, MAX_DEF, DEF_98));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.LUCK, MAX_LUCK, LUCK_98));
    }

}
