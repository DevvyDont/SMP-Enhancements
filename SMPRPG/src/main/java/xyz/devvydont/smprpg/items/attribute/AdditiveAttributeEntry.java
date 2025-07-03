package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

public class AdditiveAttributeEntry extends AttributeEntry {

    public AdditiveAttributeEntry(AttributeWrapper attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

}
