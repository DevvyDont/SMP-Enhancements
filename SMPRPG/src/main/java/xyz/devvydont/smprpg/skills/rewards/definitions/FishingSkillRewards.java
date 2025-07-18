package xyz.devvydont.smprpg.skills.rewards.definitions;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer;

public class FishingSkillRewards extends SkillRewardContainer {

    @Override
    public void initializeRewards() {

        // Add fishing rating every level.
        this.addAttributeRewardEveryLevel(AttributeWrapper.FISHING_RATING, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.FORTUNE_PER_LEVEL);

        // Add sea creature chance and treasure chance every 4 levels.
        this.addAttributeRewardEveryXLevels(AttributeWrapper.FISHING_CREATURE_CHANCE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.FISHING_CHANCE_PER_4_LEVEL, SkillGlobals.FISHING_CHANCE_FREQUENCY);
        this.addAttributeRewardEveryXLevels(AttributeWrapper.FISHING_TREASURE_CHANCE, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.FISHING_CHANCE_PER_4_LEVEL, SkillGlobals.FISHING_CHANCE_FREQUENCY);

        // Typical HP every level
        this.addScalingAttributeRewardEveryXLevels(AttributeWrapper.HEALTH, AttributeModifier.Operation.ADD_NUMBER, SkillGlobals.HP_PER_5_LEVELS, SkillGlobals.HP_LEVEL_FREQUENCY);

        // Give coins for every level.
        this.addCoinsEveryLevel();
    }

}
