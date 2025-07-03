package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

public class ScalarAttributeEntry extends AttributeEntry {

    public ScalarAttributeEntry(AttributeWrapper attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.ADD_SCALAR);
    }
}
