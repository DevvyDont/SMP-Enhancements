package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

/**
 * Rewards to get from mining. We should give rewards like mining efficiency and fortune.
 */
public class MiningSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Add fortune every level
        this.addAttributeRewardEveryLevel(AttributeWrapper.MINING_FORTUNE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.FORTUNE_PER_LEVEL);

        // Loop every 4 levels and add mining eff.
        this.addAttributeRewardEveryXLevels(AttributeWrapper.MINING_EFFICIENCY, AttributeModifier.Operation.ADD_SCALAR, SkillGlobals.MINING_EFF_PER_4_LEVELS, SkillGlobals.MINING_EFF_LEVEL_FREQUENCY);

        // Typical HP every level
        this.addAttributeRewardEveryXLevels(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.HP_PER_5_LEVELS, SkillGlobals.HP_LEVEL_FREQUENCY);

        // Give coins for every level.
        this.addCoinsEveryLevel();
    }

}
