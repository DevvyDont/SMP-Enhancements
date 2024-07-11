package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierContainer;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;

public interface Attributeable extends AttributeModifierContainer {

    /**
     * The slot that this item has to be worn in for attributes to kick in
     *
     * @return
     */
    EquipmentSlotGroup getActiveSlot();

    // Apply all modifiers on a piece of gear
    void applyModifiers(ItemMeta meta);

    /**
     * Generates a new attribute session for attribute modification
     *
     * @param type
     * @return
     */
    AttributeModifierType.AttributeSession getAttributeSession(AttributeModifierType type, ItemMeta meta);

}
