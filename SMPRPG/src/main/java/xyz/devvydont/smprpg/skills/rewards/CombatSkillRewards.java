package xyz.devvydont.smprpg.skills.rewards;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class CombatSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2% STR per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.STRENGTH, 2, AttributeModifier.Operation.ADD_SCALAR));

        // Loop every 4 levels and add 5 HP
        for (int i = 1; i < 100/4; i++)
            addReward(new StaticRewardAttribute(i*4, AttributeWrapper.HEALTH, (i-1)*5, i*5));

        // Add the maxed reward
        addReward(new StaticRewardAttribute(99, AttributeWrapper.STRENGTH, 200, 196, AttributeModifier.Operation.ADD_SCALAR));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.HEALTH, 125, 120));
    }

}
