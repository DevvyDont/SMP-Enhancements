package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;

import java.util.Collection;

public interface IAttributeItem {

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
     * @param item The item that is supposed to be holding the modifiers.
     * @return
     */
    Collection<AttributeEntry> getAttributeModifiers(ItemStack item);

    /**
     * How much should we increase the power rating of an item if this container is present?
     *
     * @return
     */
    int getPowerRating();

    /**
     * The slot that this item has to be worn in for attributes to kick in.
     *
     * @return
     */
    EquipmentSlotGroup getActiveSlot();

    /**
     * A unique key to use when applying modifiers. Needs to be unique enough to allow uniqueness when alongside other
     * items of a different type. In most cases, this can just be the item name.
     * @return The name to use for keys.
     */
    String getUniqueModifierKey();

}
