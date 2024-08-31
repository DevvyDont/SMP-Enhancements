package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class ProgressiveAttributeReward extends SkillReward {

    protected AttributeWrapper attribute;
    private int perLevelAddition;
    protected AttributeModifier.Operation operation;

    public ProgressiveAttributeReward(int level, AttributeWrapper attribute, int perLevelAddition) {
        super(level);
        this.attribute = attribute;
        this.perLevelAddition = perLevelAddition;
        this.operation = AttributeModifier.Operation.ADD_NUMBER;
    }

    public ProgressiveAttributeReward(int level, AttributeWrapper attribute, int perLevelAddition, AttributeModifier.Operation operation) {
        super(level);
        this.attribute = attribute;
        this.perLevelAddition = perLevelAddition;
        this.operation = operation;
    }

    public AttributeWrapper getAttribute() {
        return attribute;
    }

    public int getPerLevelAddition() {
        return perLevelAddition;
    }

    public int getTotalAddition() {
        return perLevelAddition * getLevel();
    }

    @Override
    public Component getDisplayName() {
        String perc = operation == AttributeModifier.Operation.ADD_NUMBER ? "" : "%";
        String old = "+" + (getTotalAddition() - getPerLevelAddition()) + perc;
        String _new = "+" + getTotalAddition() + perc;
        return ComponentUtils.getDefaultText(attribute.getCleanName() + " ").append(ComponentUtils.getUpgradeComponent(old, _new, NamedTextColor.GREEN));
    }

    /**
     * This modifier can only be shared amongst one skill type and attribute
     * If one skill gives two bonuses of the same attribute, collision will occur
     *
     * @return
     */
    public NamespacedKey getModifierKey(SkillType skill) {
        return new NamespacedKey("smprpg", skill.getKey() + attribute.name().toLowerCase() + "-bonus");
    }

    public void remove(Player player, SkillType skill) {
        player.getAttribute(attribute.getAttribute()).removeModifier(getModifierKey(skill));
    }

    /**
     * Applies this reward to the player.
     * Queries the attribute modifier of this specific skill and modifies it accordingly.
     * Adds it to the player if it hasn't been added yet
     *
     * @param player
     */
    public void apply(Player player, SkillType skill) {
        double amount = operation == AttributeModifier.Operation.ADD_NUMBER ? getTotalAddition() : getTotalAddition() / 100.0;
        remove(player, skill);
        AttributeModifier modifier = new AttributeModifier(getModifierKey(skill), amount, operation, EquipmentSlotGroup.ANY);
        player.getAttribute(attribute.getAttribute()).addModifier(modifier);
    }
}
