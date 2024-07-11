package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public enum AttributeModifierType {

    BASE,
    ENCHANTMENT,
    REFORGE,
    ;

    public class AttributeSession {

        final AttributeModifierType type;
        final KeyableItem item;
        final ItemMeta meta;

        public AttributeSession(AttributeModifierType type, CustomItemBlueprint blueprint, ItemMeta meta) {
            this.type = type;
            this.item = toKeyableItem(blueprint);
            this.meta = meta;
        }

        public AttributeSession(AttributeModifierType type, Material material, ItemMeta meta) {
            this.type = type;
            this.item = toKeyableItem(material);
            this.meta = meta;
        }

        /**
         * Returns all attribute modifiers on an item that are of this key's type
         *
         * @return
         */
        public Collection<Map.Entry<Attribute, AttributeModifier>> getAttributeModifiers() {
            return type.getAttributeModifiers(item, meta);
        }

        /**
         * Returns all attribute modifiers on an item that are of this key's type and matches the attribute
         *
         * @return
         */
        public Collection<AttributeModifier> getAttributeModifiers(Attribute attribute) {
            List<AttributeModifier> modifiers = new ArrayList<>();
            for (Map.Entry<Attribute, AttributeModifier> entry : type.getAttributeModifiers(item, meta))
                if (entry.getKey().equals(attribute))
                    modifiers.add(entry.getValue());
            return modifiers;
        }

        /**
         * Removes all attribute modifiers of this specific type from an item
         *
         */
        public void removeAttributeModifiers() {
            type.removeAttributeModifiers(item, meta);
        }

        public void addAttributeModifier(AttributeEntry entry, EquipmentSlotGroup slot) {
            type.addAttributeModifier(item, meta, entry, slot);
        }
    }

    public AttributeSession session(Material material, ItemMeta meta) {
        return new AttributeSession(this, material, meta);
    }

    public AttributeSession session(CustomItemBlueprint blueprint, ItemMeta meta) {
        return new AttributeSession(this, blueprint, meta);
    }

    public interface KeyableItem {
        String key();
    }

    /**
     * Given a custom item blueprint, generate a namespaced key for attributes to use to prevent collisions.
     *
     * @param item
     * @return
     */
    public NamespacedKey getNamespacedKey(KeyableItem item) {
        return new NamespacedKey(SMPRPG.getInstance(), this.name().toLowerCase() + "-" + item.key());
    }

    /**
     * Returns all attribute modifiers on an item that are of this key's type
     *
     * @param item
     * @param meta
     * @return
     */
    public Collection<Map.Entry<Attribute, AttributeModifier>> getAttributeModifiers(KeyableItem item, ItemMeta meta) {

        List<Map.Entry<Attribute, AttributeModifier>> modifiers = new ArrayList<>();

        if (meta == null)
            return modifiers;

        if (meta.getAttributeModifiers() == null)
            return modifiers;

        for (Map.Entry<Attribute, AttributeModifier> entry : meta.getAttributeModifiers().entries())
            if (entry.getValue().getKey().equals(getNamespacedKey(item)))
                modifiers.add(entry);

        return modifiers;
    }

    /**
     * Removes all attribute modifiers of this specific type from an item
     *
     * @param item
     * @param meta
     */
    public void removeAttributeModifiers(KeyableItem item, ItemMeta meta) {
        for (Map.Entry<Attribute, AttributeModifier> entry : getAttributeModifiers(item, meta))
            meta.removeAttributeModifier(entry.getKey(), entry.getValue());
    }

    public void addAttributeModifier(KeyableItem item, ItemMeta meta, AttributeEntry entry, EquipmentSlotGroup slot) {
        meta.addAttributeModifier(entry.getAttribute(), new AttributeModifier(getNamespacedKey(item), entry.getAmount(), entry.getOperation(), slot));
    }

    public KeyableItem toKeyableItem(CustomItemBlueprint blueprint) {
        return () -> blueprint.getCustomItemType().getKey();
    }

    public KeyableItem toKeyableItem(Material material) {
        return () -> material.name().toLowerCase();
    }

}
