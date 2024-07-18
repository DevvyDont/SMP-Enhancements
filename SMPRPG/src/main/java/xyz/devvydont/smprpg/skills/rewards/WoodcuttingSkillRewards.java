package xyz.devvydont.smprpg.skills.rewards;

import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class WoodcuttingSkillRewards extends SkillRewardContainer {
    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2 defense per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.STRENGTH, 5));

        // Loop every 5 levels and add 2 health
        for (int i = 5; i < 100; i = i + 5)
            addReward(new StaticRewardAttribute(i, AttributeWrapper.HEALTH, (i/5*2), (i/10*2)));
    }

}
