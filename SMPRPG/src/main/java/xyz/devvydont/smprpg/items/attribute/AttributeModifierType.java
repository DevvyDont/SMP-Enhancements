package xyz.devvydont.smprpg.items.attribute;

import org.bukkit.NamespacedKey;

public enum AttributeModifierType {

    BASE,
    ENCHANTMENT,
    REFORGE,
    ;

    public final NamespacedKey Key = new NamespacedKey("smprpg", "modifier-" + name().toLowerCase());

    public NamespacedKey keyForItem(String item) {
        return new NamespacedKey(Key.getNamespace(), item + "_" + name().toLowerCase());
    }
}
