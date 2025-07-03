package xyz.devvydont.smprpg.attribute.adapters;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.attribute.CustomAttributeContainer;
import xyz.devvydont.smprpg.attribute.CustomAttributeInstance;

import java.util.HashMap;

public class CustomAttributeContainerAdapter implements PersistentDataType<PersistentDataContainer, CustomAttributeContainer> {

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<CustomAttributeContainer> getComplexType() {
        return CustomAttributeContainer.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull CustomAttributeContainer complex, @NotNull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        // Convert all the attribute instances into the container.
        for (var attrEntry : complex.getAttributes().entrySet())
            container.set(attrEntry.getKey().key(), CustomAttributeInstance.ADAPTER, attrEntry.getValue());
        return container;
    }

    @Override
    public @NotNull CustomAttributeContainer fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {

        var attributes = new HashMap<AttributeWrapper, CustomAttributeInstance>();

        // Retrieve all the keys in the container. These should be keys for attributes.
        for (var attributeKey : primitive.getKeys()) {
            var attribute = AttributeWrapper.fromKey(attributeKey);
            if (attribute == null)
                continue;

            var instance = primitive.get(attributeKey, CustomAttributeInstance.ADAPTER);
            if (instance == null)
                continue;

            attributes.put(attribute, instance);
        }

        return new CustomAttributeContainer(attributes);
    }

}
