package xyz.devvydont.smprpg.attribute.adapters;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides a persistent data container adapter over the vanilla AttributeModifier instance.
 * This allows you to store API provided AttributeModifier instances on persistent data containers.
 */
public class AttributeModifierAdapter implements PersistentDataType<PersistentDataContainer, AttributeModifier> {

    public static AttributeModifierAdapter ADAPTER = new AttributeModifierAdapter();

    static final NamespacedKey ATTRIBUTE_MODIFIER_KEY_KEY = new NamespacedKey("smprpg", "key");
    static final NamespacedKey ATTRIBUTE_MODIFIER_SLOT_KEY = new NamespacedKey("smprpg", "slot");
    static final NamespacedKey ATTRIBUTE_MODIFIER_OPERATION_KEY = new NamespacedKey("smprpg", "operation");
    static final NamespacedKey ATTRIBUTE_MODIFIER_VALUE_KEY = new NamespacedKey("smprpg", "value");

    private NamespacedKey resolveKey(String key) {
        var value = key;
        if (value == null)
            value = "unknown_modifier_key";
        return new NamespacedKey("smprpg", value);
    }

    private AttributeModifier.Operation resolveOperation(@Nullable String operation) {
        try {
            return AttributeModifier.Operation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            return AttributeModifier.Operation.ADD_NUMBER;
        }
    }
    private EquipmentSlotGroup resolveSlotGroup(@Nullable String slot) {
        if (slot == null)
            return EquipmentSlotGroup.ANY;
        var ret = EquipmentSlotGroup.getByName(slot);
        if (ret == null)
            return EquipmentSlotGroup.ANY;
        return ret;
    }

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<AttributeModifier> getComplexType() {
        return AttributeModifier.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull AttributeModifier complex, @NotNull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        container.set(ATTRIBUTE_MODIFIER_VALUE_KEY, DOUBLE, complex.getAmount());
        container.set(ATTRIBUTE_MODIFIER_SLOT_KEY, STRING, complex.getSlotGroup().toString());
        container.set(ATTRIBUTE_MODIFIER_OPERATION_KEY, STRING, complex.getOperation().toString());
        container.set(ATTRIBUTE_MODIFIER_KEY_KEY, STRING, complex.key().value());
        return container;
    }

    @Override
    public @NotNull AttributeModifier fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        var key = resolveKey(primitive.get(ATTRIBUTE_MODIFIER_KEY_KEY, PersistentDataType.STRING));
        var value = primitive.getOrDefault(ATTRIBUTE_MODIFIER_VALUE_KEY, PersistentDataType.DOUBLE, 0d);
        var operation = resolveOperation(primitive.get(ATTRIBUTE_MODIFIER_OPERATION_KEY, PersistentDataType.STRING));
        var slot = resolveSlotGroup(primitive.get(ATTRIBUTE_MODIFIER_SLOT_KEY, PersistentDataType.STRING));

        return new AttributeModifier(key, value, operation, slot);
    }
}
