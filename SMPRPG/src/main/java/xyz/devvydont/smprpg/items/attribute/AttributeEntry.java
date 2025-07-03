package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

public class AttributeEntry {

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
