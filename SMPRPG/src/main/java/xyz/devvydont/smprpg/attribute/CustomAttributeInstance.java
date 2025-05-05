package xyz.devvydont.smprpg.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.adapters.CustomAttributeInstanceAdapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mirror of the normal AttributeInstance, but only works with AttributeWrapper attributes.
 * Also implements the functionality for PDC storage.
 */
public class CustomAttributeInstance {

    public static final CustomAttributeInstanceAdapter ADAPTER = new CustomAttributeInstanceAdapter();

    public static final NamespacedKey ATTRIBUTE_BASE_VALUE_KEY = new NamespacedKey("smprpg", "base_value");
    public static final NamespacedKey ATTRIBUTE_MODIFIERS_KEY = new NamespacedKey("smprpg", "modifiers");

    private double _base;
    private final Map<NamespacedKey, AttributeModifier> _modifiers;

    public CustomAttributeInstance(double _base, List<AttributeModifier> modifiers) {
        this._base = _base;
        _modifiers = new HashMap<>();
        for (var modifier : modifiers)
            addModifier(modifier);
    }

    public double getBaseValue() {
        return _base;
    }

    public void setBaseValue(double _base) {
        this._base = _base;
    }

    public double getValue() {
        var sum = 0d;
        var scalar = 1.0;
        var multiplier = 1.0;

        // Apply all modifiers.
        for (var modifier : this.getModifiers()) {
            switch (modifier.getOperation()) {
                case ADD_NUMBER -> sum += modifier.getAmount();
                case ADD_SCALAR -> scalar += modifier.getAmount();
                case MULTIPLY_SCALAR_1 -> multiplier *= (1+modifier.getAmount());
            }
        }

        var total = 0d;
        total += _base;
        total += sum;
        total *= scalar;
        total *= multiplier;
        return total;
    }

    public Collection<AttributeModifier> getModifiers() {
        return _modifiers.values();
    }

    public void addModifier(AttributeModifier modifier) {
        _modifiers.put(modifier.getKey(), modifier);
    }

    public void removeModifier(AttributeModifier modifier) {
        _modifiers.remove(modifier.getKey());
    }

    public void removeModifier(NamespacedKey key) {
        _modifiers.remove(key);
    }

    public void clearModifiers() {
        _modifiers.clear();
    }

    public CustomAttributeModifierCollection getModifierCollection() {
        return new CustomAttributeModifierCollection(_modifiers.values().stream().toList());
    }

    @Override
    public String toString() {
        return "CustomAttributeInstance{" +
                "_base=" + _base +
                ", _modifiers=" + _modifiers +
                '}';
    }

}
