package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

/**
 * The rewards received from leveling up farming. Farming related skills should be given, like farming fortune.
 */
public class FarmingSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Add farming fortune every level
        this.addAttributeRewardEveryLevel(AttributeWrapper.FARMING_FORTUNE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.FORTUNE_PER_LEVEL);

        // Loop every 4 levels and add REGEN
        this.addAttributeRewardEveryXLevels(AttributeWrapper.REGENERATION, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.REGENERATION_PER_2_LEVELS, SkillGlobals.REGENERATION_LEVEL_FREQUENCY);

        // Typical HP every level
        this.addScalingAttributeRewardEveryXLevels(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.HP_PER_5_LEVELS, SkillGlobals.HP_LEVEL_FREQUENCY);

        // Give coins for every level.
        this.addCoinsEveryLevel();
    }


}
