package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class ScalarAttributeEntry extends AttributeEntry {


    public ScalarAttributeEntry(Attribute attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.ADD_SCALAR);
    }
}
