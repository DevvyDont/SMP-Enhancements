package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

public class WoodcuttingSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Add fortune every level
        this.addAttributeRewardEveryLevel(AttributeWrapper.WOODCUTTING_FORTUNE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.FORTUNE_PER_LEVEL);

        // Loop every 4 levels and add crit.
        this.addAttributeRewardEveryXLevels(AttributeWrapper.CRITICAL_DAMAGE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.CRITICAL_RATING_PER_4_LEVELS, SkillGlobals.CRITICAL_RATING_LEVEL_FREQUENCY);

        // Loop every 2 levels and add def.
        this.addAttributeRewardEveryXLevels(AttributeWrapper.DEFENSE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.DEFENSE_PER_2_LEVELS, SkillGlobals.DEFENSE_LEVEL_FREQUENCY);

        // Typical HP every level
        this.addAttributeRewardEveryXLevels(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.HP_PER_5_LEVELS, SkillGlobals.HP_LEVEL_FREQUENCY);

        // Give coins for every level.
        this.addCoinsEveryLevel();
    }

}
