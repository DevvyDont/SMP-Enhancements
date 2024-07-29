package xyz.devvydont.smprpg.skills.rewards;

import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class FishingSkillRewards extends SkillRewardContainer {


    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2 defense per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.DEFENSE, 4));

        // Loop every 4 levels and add 2 luck
        for (int i = 1; i < 100/4; i++)
            addReward(new StaticRewardAttribute(i*4, AttributeWrapper.LUCK, i*2, (i-1)*2));

        addReward(new StaticRewardAttribute(99, AttributeWrapper.DEFENSE, 400, 396));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.LUCK, 50, 48));
    }

}
