package xyz.devvydont.smprpg.skills.rewards;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.skills.SkillRewardContainer;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MiningSkillRewards extends SkillRewardContainer {


    public static final int HP_PER_LEVEL = 2;
    public static final int DEF_PER_5_LEVEL = 10;

    public static final int SECONDARY_LEVEL_DIFFERENCE = 5;


    @Override
    public void initializeRewards() {

        // Loop from 1-99 and add 2 HP per level
        for (int i = 1; i < 99; i++)
            addReward(new ProgressiveAttributeReward(i, AttributeWrapper.HEALTH, HP_PER_LEVEL));

        // Loop every 4 levels and add 5 DEF
        for (int i = 1; i < 100/SECONDARY_LEVEL_DIFFERENCE; i++)
            addReward(new StaticRewardAttribute(i*SECONDARY_LEVEL_DIFFERENCE, AttributeWrapper.DEFENSE, i*DEF_PER_5_LEVEL, (i-1)*DEF_PER_5_LEVEL));

        // Add the maxed reward
        int MAX_HP = HP_PER_LEVEL * 100;
        int MAX_DEF = DEF_PER_5_LEVEL * (100 / SECONDARY_LEVEL_DIFFERENCE);
        int HP_98 = HP_PER_LEVEL * 98;
        int DEF_98 = DEF_PER_5_LEVEL * (100 / SECONDARY_LEVEL_DIFFERENCE - 1);
        addReward(new StaticRewardAttribute(99, AttributeWrapper.HEALTH, MAX_HP, HP_98));
        addReward(new StaticRewardAttribute(99, AttributeWrapper.DEFENSE, MAX_DEF, DEF_98));
    }


}
