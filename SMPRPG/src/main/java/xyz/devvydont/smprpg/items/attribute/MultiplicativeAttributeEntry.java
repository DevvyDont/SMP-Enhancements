package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class MultiplicativeAttributeEntry extends AttributeEntry {

    public MultiplicativeAttributeEntry(Attribute attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
    }

}
