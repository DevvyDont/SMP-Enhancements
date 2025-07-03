package xyz.devvydont.smprpg.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.attribute.adapters.CustomAttributeContainerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Complex PDC wrapper that provides functionality to store inner PDCs that are meant to represent attribute instances.
 */
public class CustomAttributeContainer {

    public static final CustomAttributeContainerAdapter ADAPTER = new CustomAttributeContainerAdapter();

    private Map<AttributeWrapper, CustomAttributeInstance> attributes;

    public CustomAttributeContainer(Map<AttributeWrapper, CustomAttributeInstance> attributes) {
        this.attributes = attributes;
    }

    public CustomAttributeContainer() {
        this.attributes = new HashMap<>();
    }

    public Map<AttributeWrapper, CustomAttributeInstance> getAttributes() {
        return attributes;
    }

    @Nullable
    public CustomAttributeInstance getAttribute(@NotNull final AttributeWrapper attribute) {
        return attributes.get(attribute);
    }

    public void setAttributes(Map<AttributeWrapper, CustomAttributeInstance> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(AttributeWrapper attribute, CustomAttributeInstance attributeInstance) {
        this.attributes.put(attribute, attributeInstance);
    }

    public void addAttribute(AttributeWrapper attribute) {
        this.attributes.put(attribute, new CustomAttributeInstance(0d, new ArrayList<>()));
    }


    public void removeAttribute(AttributeWrapper wrapper) {
        this.attributes.remove(wrapper);
    }

    @Override
    public String toString() {
        return "CustomAttributeContainer{" +
                "attributes=" + attributes +
                '}';
    }
}
