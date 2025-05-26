package xyz.devvydont.smprpg.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.adapters.CustomAttributeModifierCollectionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper over an ArrayList of attribute modifiers that allows a list of attribute modifiers to be stored/read from
 * persistent data containers.
 */
public class CustomAttributeModifierCollection  {

    public static final CustomAttributeModifierCollectionAdapter ADAPTER = new CustomAttributeModifierCollectionAdapter();

    public static CustomAttributeModifierCollection empty() {
        return new CustomAttributeModifierCollection(new ArrayList<>());
    }

    private final List<AttributeModifier> _modifiers;

    public CustomAttributeModifierCollection(List<AttributeModifier> modifiers) {
        this._modifiers = modifiers;
    }

    public List<AttributeModifier> getModifiers() {
        return _modifiers;
    }

    public void addModifier(AttributeModifier modifier) {

        // Remove the modifiers with a matching key.
        removeModifier(modifier);
        this._modifiers.add(modifier);
    }

    public void removeModifier(AttributeModifier modifier) {
        for (var mod : _modifiers.stream().toList())
            if (mod.getKey().equals(modifier.getKey()))
                _modifiers.remove(mod);
    }

    public void removeModifier(NamespacedKey key) {
        for (var mod : _modifiers.stream().toList())
            if (mod.getKey().equals(key))
                _modifiers.remove(mod);
    }

    @Override
    public String toString() {
        return "CustomAttributeModifierCollection{" +
                "_modifiers=" + _modifiers +
                '}';
    }
}
