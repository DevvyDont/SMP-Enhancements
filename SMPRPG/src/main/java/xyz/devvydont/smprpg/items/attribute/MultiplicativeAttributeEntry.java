package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class MultiplicativeAttributeEntry extends AttributeEntry {

    public MultiplicativeAttributeEntry(Attribute attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

    public MultiplicativeAttributeEntry(AttributeWrapper attribute, double amount) {
        super(attribute.getAttribute(), amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

}
