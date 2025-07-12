package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

public class AttributeEntry {

    /**
     * Shortcut method to get an additive attribute modifier.
     * @param wrapper The attribute.
     * @param amount The amount to add.
     * @return The attribute entry.
     */
    public static AttributeEntry additive(AttributeWrapper wrapper, double amount) {
        return new AttributeEntry(wrapper, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

    /**
     * Shortcut method to get a scalar attribute modifier.
     * @param wrapper The attribute.
     * @param amount The amount to scale by.
     * @return The attribute entry.
     */
    public static AttributeEntry scalar(AttributeWrapper wrapper, double amount) {
        return new AttributeEntry(wrapper, amount, AttributeModifier.Operation.ADD_SCALAR);
    }

    /**
     * Shortcut method to get a multiplicative attribute modifier.
     * @param wrapper The attribute.
     * @param amount The amount to multiply by.
     * @return The attribute entry.
     */
    public static AttributeEntry multiplicative(AttributeWrapper wrapper, double amount) {
        return new AttributeEntry(wrapper, amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

    AttributeWrapper attribute;
    double amount;
    AttributeModifier.Operation operation;

    public AttributeEntry(AttributeWrapper attribute, double amount, AttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeWrapper getAttribute() {
        return attribute;
    }

    public double getAmount() {
        return amount;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    public AttributeModifier asModifier(NamespacedKey key, EquipmentSlotGroup slot) {
        return new AttributeModifier(key, amount, operation, slot);
    }
}
