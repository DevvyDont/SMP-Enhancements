package xyz.devvydont.smprpg.skills.rewards.definitions;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.CoinReward;
import xyz.devvydont.smprpg.skills.rewards.EnchantmentSkillReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

public class MagicSkillRewards extends SkillRewardContainer {

    public static final int SECONDARY_STAT_LEVEL_DIFF = 5;

    @Override
    public void initializeRewards() {

        // Loop through all the enchantments in the game. Add the enchantment unlock to the rewards
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments())
            if (enchantment.getSkillRequirement() > 0)
                addReward(enchantment.getSkillRequirement(), new EnchantmentSkillReward(enchantment));

        // Loop from 1-100 and add INT per level
        for (var i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(
                    AttributeWrapper.INTELLIGENCE,
                    SkillGlobals.DEFAULT_SKILL_OPERATION,
                    SkillGlobals.getStatPerLevel(SkillGlobals.INT_PER_LEVEL, i),
                    SkillGlobals.getStatPerLevel(SkillGlobals.INT_PER_LEVEL, i-1)
            ));

        // Loop every 5 levels and add LUCK
        for (var i = SECONDARY_STAT_LEVEL_DIFF; i <= 100; i += SECONDARY_STAT_LEVEL_DIFF)
            addReward(i, new AttributeReward(
                    AttributeWrapper.LUCK,
                    SkillGlobals.DEFAULT_SKILL_OPERATION,
                    SkillGlobals.getStatPerXLevel(SkillGlobals.LUCK_PER_5_LEVELS, SECONDARY_STAT_LEVEL_DIFF, i),
                    SkillGlobals.getStatPerXLevel(SkillGlobals.LUCK_PER_5_LEVELS, SECONDARY_STAT_LEVEL_DIFF, i-SECONDARY_STAT_LEVEL_DIFF)
            ));

        // Give coins for every level.
        for (var i = 1; i <= SkillGlobals.getMaxSkillLevel(); i++)
            addReward(i, new CoinReward(SkillGlobals.getCoinRewardForLevel(i)));
    }

}
