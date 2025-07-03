package xyz.devvydont.smprpg.attribute.adapters;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.CustomAttributeInstance;
import xyz.devvydont.smprpg.attribute.CustomAttributeModifierCollection;

import static xyz.devvydont.smprpg.attribute.CustomAttributeInstance.ATTRIBUTE_BASE_VALUE_KEY;
import static xyz.devvydont.smprpg.attribute.CustomAttributeInstance.ATTRIBUTE_MODIFIERS_KEY;

public class CustomAttributeInstanceAdapter implements PersistentDataType<PersistentDataContainer, CustomAttributeInstance> {

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<CustomAttributeInstance> getComplexType() {
        return CustomAttributeInstance.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull CustomAttributeInstance complex, @NotNull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        container.set(ATTRIBUTE_BASE_VALUE_KEY, DOUBLE, complex.getBaseValue());
        container.set(ATTRIBUTE_MODIFIERS_KEY, CustomAttributeModifierCollection.ADAPTER, complex.getModifierCollection());
        return container;
    }

    @Override
    public @NotNull CustomAttributeInstance fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        var base = primitive.getOrDefault(ATTRIBUTE_BASE_VALUE_KEY, DOUBLE, 0d);
        var modifiers = primitive.getOrDefault(ATTRIBUTE_MODIFIERS_KEY, CustomAttributeModifierCollection.ADAPTER, CustomAttributeModifierCollection.empty());
        return new CustomAttributeInstance(base, modifiers.getModifiers());
    }

}
