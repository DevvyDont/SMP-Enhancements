package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class AttributeEntry {

    Attribute attribute;
    double amount;
    AttributeModifier.Operation operation;

    public AttributeEntry(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getAmount() {
        return amount;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }
}
