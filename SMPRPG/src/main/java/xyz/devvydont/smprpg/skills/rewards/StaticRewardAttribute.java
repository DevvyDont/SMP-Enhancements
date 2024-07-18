package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class StaticRewardAttribute extends ProgressiveAttributeReward {

    private int amount;
    private int previousAmount;

    public StaticRewardAttribute(int level, AttributeWrapper attribute, int amount, int previousAmount) {
        super(level, attribute, 0);
        this.amount = amount;
        this.previousAmount = previousAmount;
    }

    @Override
    public Component getDisplayName() {
        String old = "+" + previousAmount;
        String _new = "+" + getTotalAddition();
        return ComponentUtil.getDefaultText(attribute.getCleanName() + " ").append(ComponentUtil.getUpgradeComponent(old, _new, NamedTextColor.GREEN));
    }

    @Override
    public int getTotalAddition() {
        return this.amount;
    }

    public int getAmount() {
        return amount;
    }

}
