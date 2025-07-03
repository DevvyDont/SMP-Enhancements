package xyz.devvydont.smprpg.attribute.adapters;

import org.bukkit.attribute.AttributeModifier;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.CustomAttributeModifierCollection;

import java.util.ArrayList;

public class CustomAttributeModifierCollectionAdapter implements PersistentDataType<PersistentDataContainer, CustomAttributeModifierCollection> {

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<CustomAttributeModifierCollection> getComplexType() {
        return CustomAttributeModifierCollection.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull CustomAttributeModifierCollection complex, @NotNull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        for (var modifier : complex.getModifiers())
            container.set(modifier.getKey(), AttributeModifierAdapter.ADAPTER, modifier);
        return container;
    }

    @Override
    public @NotNull CustomAttributeModifierCollection fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        var modifiers = new ArrayList<AttributeModifier>();
        for (var key : primitive.getKeys()) {
            var modifier = primitive.get(key, AttributeModifierAdapter.ADAPTER);
            if (modifier != null)
                modifiers.add(modifier);
        }
        return new CustomAttributeModifierCollection(modifiers);
    }

}
