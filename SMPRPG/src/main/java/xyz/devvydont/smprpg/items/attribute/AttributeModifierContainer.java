package xyz.devvydont.smprpg.items.attribute;

import java.util.Collection;

public interface AttributeModifierContainer {

    /**
     * What kind of attribute container is this? Items can have multiple containers of stats that stack
     * to prevent collisions
     *
     * @return
     */
    AttributeModifierType getAttributeModifierType();

    /**
     * What modifiers themselves will be contained on the item if there are no variables to affect them?
     *
     * @return
     */
    Collection<AttributeEntry> getAttributeModifiers();

    /**
     * How much should we increase the power rating of an item if this container is present?
     *
     * @return
     */
    int getPowerRating();
}
