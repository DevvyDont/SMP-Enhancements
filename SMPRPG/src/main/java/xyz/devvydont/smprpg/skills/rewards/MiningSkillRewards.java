package xyz.devvydont.smprpg.skills.rewards;

import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MiningSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2 HP per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.HEALTH, 2));

        // Loop every 4 levels and add 5 DEF
        for (int i = 1; i < 100/4; i++)
            addReward(new StaticRewardAttribute(i*4, AttributeWrapper.DEFENSE, i*5, (i-1)*5));

        // Add the maxed reward
        addReward(new StaticRewardAttribute(99, AttributeWrapper.HEALTH, 200, 196));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.DEFENSE, 125, 120));
    }


}
