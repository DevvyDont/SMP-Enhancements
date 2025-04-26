package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.EnchantmentSkillReward;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MagicSkillRewards extends SkillRewardContainer {

    public static final int DEF_PER_LEVEL = 2;

    public static final int LUCK_PER_4_LEVEL = 2;
    public static final int SECONDARY_STAT_LEVEL_DIFF = 4;

    public int getDefForLevel(int level) {
        return DEF_PER_LEVEL * level;
    }

    public int getLuckForLevel(int level) {
        return Math.max(0, level * LUCK_PER_4_LEVEL / SECONDARY_STAT_LEVEL_DIFF);
    }

    @Override
    public void initializeRewards() {

        // Loop through all the enchantments in the game. Add the enchantment unlock to the rewards
        for (CustomEnchantment enchantment : SMPRPG.getInstance().getEnchantmentService().getCustomEnchantments())
            if (enchantment.getSkillRequirement() > 0)
                addReward(enchantment.getSkillRequirement(), new EnchantmentSkillReward(enchantment));

        // Loop from 1-100 and add DEF per level
        for (int i = 1; i <= 100; i++)
            addReward(i, new AttributeReward(AttributeWrapper.DEFENSE, AttributeModifier.Operation.ADD_NUMBER, getDefForLevel(i), getDefForLevel(i-1)));

        // Loop every 4 levels and add luck
        for (int i = SECONDARY_STAT_LEVEL_DIFF; i <= 100; i += SECONDARY_STAT_LEVEL_DIFF)
            addReward(i, new AttributeReward(AttributeWrapper.LUCK, AttributeModifier.Operation.ADD_NUMBER, getLuckForLevel(i), getLuckForLevel(i-SECONDARY_STAT_LEVEL_DIFF)));
    }

}
