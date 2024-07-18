package xyz.devvydont.smprpg.skills.rewards;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MagicSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Loop through all the enchantments in the game. Add the enchantment unlock to the rewards
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments())
            if (enchantment.getSkillRequirement() > 0)
                addReward(new EnchantmentSkillReward(enchantment));

        // Loop from 1-99 and add 2 defense per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.DEFENSE, 2));

        // Loop every 5 levels and add 1 luck
        for (int i = 5; i < 100; i = i + 5)
            addReward(new StaticRewardAttribute(i, AttributeWrapper.LUCK, (i/5), (i/10)));
    }

}
