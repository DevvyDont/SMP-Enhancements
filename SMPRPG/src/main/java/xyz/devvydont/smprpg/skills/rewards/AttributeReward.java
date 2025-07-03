package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.player.ProfileDifficulty;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class AttributeReward implements ISkillReward {

    protected AttributeWrapper attribute;
    protected AttributeModifier.Operation operation;

    protected double amount;
    protected double previousAmount;

    public AttributeReward(AttributeWrapper attribute, AttributeModifier.Operation operation, double amount, double previousAmount) {
        this.attribute = attribute;
        this.operation = operation;
        this.amount = amount;
        this.previousAmount = previousAmount;
    }

    public AttributeReward(AttributeWrapper attribute, AttributeModifier.Operation operation, double amount) {
        this(attribute, operation, amount, 0);
    }

    public AttributeWrapper getAttribute() {
        return attribute;
    }

    public double calculateRewardAmount(Player player, double amount) {
        if (SMPRPG.getInstance().getDifficultyService().getDifficulty(player) == ProfileDifficulty.HARD)
            return amount / 2;
        return amount;
    }

    private String formatNumber(double number) {
        // If this number is already a whole number, use int representation.
        if (number - ((int) number) == 0)
            return String.valueOf((int)number);

        // Otherwise only use 1 decimal.
        return String.format("%.1f", number);
    }

    @Override
    public Component generateRewardComponent(Player player) {
        String perc = operation == AttributeModifier.Operation.ADD_NUMBER ? "" : "%";
        var _rawNew = calculateRewardAmount(player, amount);
        var _rawOld = calculateRewardAmount(player, previousAmount);
        String old = "+" + formatNumber(_rawOld) + perc;
        String _new = "+" + formatNumber(_rawNew) + perc;
        return ComponentUtils.merge(
                ComponentUtils.create(attribute.DisplayName + " "),
                ComponentUtils.upgrade(old, _new, NamedTextColor.GREEN)
        );
    }

    /**
     * This modifier can only be shared amongst one skill type and attribute
     * If one skill gives two bonuses of the same attribute, collision will occur
     *
     * @return
     */
    public NamespacedKey getModifierKey(SkillType skill) {
        return new NamespacedKey("smprpg", skill.getKey() + "_" +  attribute.name().toLowerCase() + "_bonus");
    }

    public void remove(Player player, SkillType skill) {

        var attributeInstance = AttributeService.getInstance().getAttribute(player, attribute);
        if (attributeInstance == null)
            return;

        attributeInstance.removeModifier(getModifierKey(skill));
        attributeInstance.save(player, attribute);
    }

    /**
     * Applies this reward to the player.
     * Queries the attribute modifier of this specific skill and modifies it accordingly.
     * Adds it to the player if it hasn't been added yet
     *
     * @param player
     */
    public void apply(Player player, SkillType skill) {

        var adjustedAmount = calculateRewardAmount(player, this.amount);

        // Depending on the operation, it may need to apply differently in order to work as a vanilla modifier.
        if (operation == AttributeModifier.Operation.ADD_SCALAR || operation == AttributeModifier.Operation.MULTIPLY_SCALAR_1)
            adjustedAmount /= 100.0;

        remove(player, skill);

        var attributeInstance = AttributeService.getInstance().getOrCreateAttribute(player, attribute);
        var modifier = new AttributeModifier(getModifierKey(skill), adjustedAmount, operation, EquipmentSlotGroup.ANY);
        attributeInstance.addModifier(modifier);
        attributeInstance.save(player, attribute);
    }
}
