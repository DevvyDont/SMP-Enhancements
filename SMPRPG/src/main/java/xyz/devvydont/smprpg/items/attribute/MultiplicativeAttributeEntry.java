package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

public class MultiplicativeAttributeEntry extends AttributeEntry {

    public MultiplicativeAttributeEntry(AttributeWrapper attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

}
