package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class ScalarAttributeEntry extends AttributeEntry {


    public ScalarAttributeEntry(Attribute attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.ADD_SCALAR);
    }

    public ScalarAttributeEntry(AttributeWrapper attribute, double amount) {
        super(attribute.getAttribute(), amount, AttributeModifier.Operation.ADD_SCALAR);
    }
}
