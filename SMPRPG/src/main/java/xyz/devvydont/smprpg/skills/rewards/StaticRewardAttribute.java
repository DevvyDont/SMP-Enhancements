package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class StaticRewardAttribute extends ProgressiveAttributeReward {

    private int amount;
    private int previousAmount;

    public StaticRewardAttribute(int level, AttributeWrapper attribute, int amount, int previousAmount) {
        super(level, attribute, 0);
        this.amount = amount;
        this.previousAmount = previousAmount;
    }

    public StaticRewardAttribute(int level, AttributeWrapper attribute, int amount, int previousAmount, AttributeModifier.Operation operation) {
        super(level, attribute, 0, operation);
        this.amount = amount;
        this.previousAmount = previousAmount;
    }

    @Override
    public Component getDisplayName() {
        String perc = operation == AttributeModifier.Operation.ADD_NUMBER ? "" : "%";
        String old = "+" + previousAmount + perc;
        String _new = "+" + getTotalAddition() + perc;
        return ComponentUtils.getDefaultText(attribute.getCleanName() + " ").append(ComponentUtils.getUpgradeComponent(old, _new, NamedTextColor.GREEN));
    }

    @Override
    public int getTotalAddition() {
        return this.amount;
    }

    public int getAmount() {
        return amount;
    }

}
