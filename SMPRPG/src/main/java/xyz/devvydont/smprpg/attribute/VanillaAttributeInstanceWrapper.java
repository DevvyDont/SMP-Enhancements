package xyz.devvydont.smprpg.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.persistence.PersistentDataHolder;

import java.util.Collection;

/**
 * Used to inject custom attribute behavior to interact with vanilla minecraft's attribute API instead of using
 * PDCs like the custom attributes do. All we are doing is replacing all custom logic with interacting with a
 * wrapped attribute instance. All data persistence as well should be handled my minecraft and not us.
 */
public class VanillaAttributeInstanceWrapper extends CustomAttributeInstance {

    private final AttributeInstance wrapped;

    public VanillaAttributeInstanceWrapper(AttributeInstance wrapped) {
        super(wrapped.getBaseValue(), wrapped.getModifiers().stream().toList());
        this.wrapped = wrapped;
    }

    @Override
    public double getBaseValue() {
        return wrapped.getBaseValue();
    }

    @Override
    public void setBaseValue(double _base) {
        if (wrapped == null)
            return;
        wrapped.setBaseValue(_base);
    }

    @Override
    public double getValue() {
        return wrapped.getValue();
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        return wrapped.getModifiers();
    }

    @Override
    public void addModifier(AttributeModifier modifier) {

        // If the entity doesn't have the attribute, don't add it.
        if (wrapped == null)
            return;

        // If the modifier is already present, remove it before applying it.
        wrapped.removeModifier(modifier.key());
        wrapped.addModifier(modifier);
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        if (wrapped == null)
            return;
        wrapped.removeModifier(modifier);
    }

    @Override
    public void removeModifier(NamespacedKey key) {
        if (wrapped == null)
            return;
        wrapped.removeModifier(key);
    }

    @Override
    public void clearModifiers() {
        if (wrapped == null)
            return;
        for (var modifier : wrapped.getModifiers().stream().toList())
            wrapped.removeModifier(modifier);
    }

    @Override
    public void save(PersistentDataHolder target, AttributeWrapper attribute) {
        // Do nothing! Minecraft will handle this for us :)
    }

    @Override
    public CustomAttributeModifierCollection getModifierCollection() {
        return new CustomAttributeModifierCollection(wrapped.getModifiers().stream().toList());
    }

}
