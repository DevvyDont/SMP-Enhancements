package xyz.devvydont.smprpg.skills.rewards;

import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MagicSkillRewards extends SkillRewardContainer {

    public static final int DEF_PER_2_LEVEL = 7;
    public static final int PRIMARY_STAT_LEVEL_DIFF = 2;

    public static final int LUCK_PER_4_LEVEL = 2;
    public static final int SECONDARY_STAT_LEVEL_DIFF = 4;

    @Override
    public void initializeRewards() {

        // Loop through all the enchantments in the game. Add the enchantment unlock to the rewards
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments())
            if (enchantment.getSkillRequirement() > 0)
                addReward(new EnchantmentSkillReward(enchantment));

        // Loop every 2 levels and add 7 DEF
        for (int i = 1; i < 100/PRIMARY_STAT_LEVEL_DIFF; i++)
            addReward(new StaticRewardAttribute(i*PRIMARY_STAT_LEVEL_DIFF, AttributeWrapper.DEFENSE, i*DEF_PER_2_LEVEL, (i-1)*DEF_PER_2_LEVEL));

        // Loop every 4 levels and add 2 luck
        for (int i = 1; i < 100/SECONDARY_STAT_LEVEL_DIFF; i++)
            addReward(new StaticRewardAttribute(i*SECONDARY_STAT_LEVEL_DIFF, AttributeWrapper.LUCK, i*LUCK_PER_4_LEVEL, (i-1)*LUCK_PER_4_LEVEL));

        // Add the maxed reward
        int MAX_DEF = DEF_PER_2_LEVEL * (100 / PRIMARY_STAT_LEVEL_DIFF);
        int DEF_98 = DEF_PER_2_LEVEL * (100 / PRIMARY_STAT_LEVEL_DIFF - 1);
        int MAX_LUCK = LUCK_PER_4_LEVEL * (100 / SECONDARY_STAT_LEVEL_DIFF);
        int LUCK_98 = LUCK_PER_4_LEVEL * (100 / SECONDARY_STAT_LEVEL_DIFF - 1);
        addReward(new StaticRewardAttribute(99, AttributeWrapper.DEFENSE, MAX_DEF, DEF_98));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.LUCK, MAX_LUCK, LUCK_98));
    }

}
