package xyz.devvydont.smprpg.skills.rewards;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class FarmingSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2 HP per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.HEALTH, 2));

        // Loop every 4 levels and add 5 STR
        for (int i = 1; i < 100/4; i++)
            addReward(new StaticRewardAttribute(i*4, AttributeWrapper.STRENGTH, i*5, (i-1)*5, AttributeModifier.Operation.ADD_SCALAR));

        addReward(new StaticRewardAttribute(99, AttributeWrapper.HEALTH, 200, 196));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.STRENGTH, 125, 120, AttributeModifier.Operation.ADD_SCALAR));
    }


}
