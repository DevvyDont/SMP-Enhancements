package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

public class AdditiveAttributeEntry extends AttributeEntry {

    public AdditiveAttributeEntry(Attribute attribute, double amount) {
        super(attribute, amount, AttributeModifier.Operation.ADD_NUMBER);
    }

}
