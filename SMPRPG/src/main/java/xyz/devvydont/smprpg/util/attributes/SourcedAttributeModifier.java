package xyz.devvydont.smprpg.util.attributes;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;

/**
 * A class meant to add the "source" functionality to an attribute modifier. Only used for aiding in the attribute
 * display process for item lore to make it more clear where certain stats are coming from.
 */
public class SourcedAttributeModifier extends AttributeModifier {

    private final AttributeModifierType source;

    public SourcedAttributeModifier(@NotNull NamespacedKey key, double amount, @NotNull Operation operation, @NotNull EquipmentSlotGroup slot, AttributeModifierType type) {
        super(key, amount, operation, slot);
        this.source = type;
    }

    public SourcedAttributeModifier(AttributeModifier modifier, AttributeModifierType source) {
        this(modifier.getKey(), modifier.getAmount(), modifier.getOperation(), modifier.getSlotGroup(), source);
    }

    public AttributeModifierType getSource() {
        return source;
    }
}
