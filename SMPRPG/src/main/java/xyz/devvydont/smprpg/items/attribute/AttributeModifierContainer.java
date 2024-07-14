package xyz.devvydont.smprpg.items.attribute;

import java.util.Collection;

public interface AttributeModifierContainer {

    AttributeModifierType getAttributeModifierType();
    Collection<AttributeEntry> getAttributeModifiers();

    // The power rating this will increment a piece of gear by when present
    int getPowerRating();
}
