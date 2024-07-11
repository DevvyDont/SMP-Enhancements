package xyz.devvydont.smprpg.items.attribute;

import java.util.Collection;

public interface AttributeModifierContainer {

    AttributeModifierType getAttributeModifierType();
    Collection<AttributeEntry> getAttributeModifiers();

    // Define defense for a piece of equipment, since there isn't an attribute associated we have to do it here :(
    int getDefense();

    // The power rating this will increment a piece of gear by when present
    int getPowerRating();
}
