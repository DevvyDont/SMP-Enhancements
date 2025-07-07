package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

/**
 * The rewards received from combat. Combat related stats should be given, like damage and criticals.
 */
public class CombatSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Combat should give strength and crit% every level.
        this.addAttributeRewardEveryLevel(AttributeWrapper.STRENGTH, SkillGlobals.STRENGTH_SKILL_OPERATION, SkillGlobals.STR_PER_LEVEL);
        this.addAttributeRewardEveryLevel(AttributeWrapper.CRITICAL_CHANCE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.CRITICAL_CHANCE_PER_LEVEL);

        // Also give some crit% every 4 levels.
        this.addAttributeRewardEveryXLevels(AttributeWrapper.CRITICAL_DAMAGE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.CRITICAL_RATING_PER_4_LEVELS, SkillGlobals.CRITICAL_RATING_LEVEL_FREQUENCY);

        // Typical HP every level
        this.addAttributeRewardEveryXLevels(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.HP_PER_5_LEVELS, SkillGlobals.HP_LEVEL_FREQUENCY);

        // Give coins for every level.
        this.addCoinsEveryLevel();
    }

}
