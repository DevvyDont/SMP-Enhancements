package xyz.devvydont.smprpg.services;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.*;
import xyz.devvydont.smprpg.attribute.listeners.AttributeApplyListener;
import xyz.devvydont.smprpg.events.CustomAttributeContainerUpdatedEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Used as an internal API in order to manage and unify vanilla minecraft attributes and our custom defined attributes.
 * This service should be responsible for:
 * - Storing/retrieving custom attributes on PDC holders
 * - Instantiate listeners to make custom attributes function
 * - Assist with showing attribute information to players
 */
public class AttributeService implements IService, Listener {

    // The path to the root attribute storage container on a PDC holder.
    public static final NamespacedKey ATTRIBUTE_CONTAINER_KEY = new NamespacedKey("smprpg", "custom_attribute_container");
    public static final NamespacedKey ATTRIBUTE_MODIFIERS_KEY = new NamespacedKey("smprpg", "custom_modifiers");

    @Override
    public boolean setup() {
        SMPRPG.getInstance().getServer().getPluginManager().registerEvents(this, SMPRPG.getInstance());

        // Register listeners related to attribute management.

        // Listener to apply/remove custom attributes.
        SMPRPG.getInstance().getServer().getPluginManager().registerEvents(new AttributeApplyListener(), SMPRPG.getInstance());
        return true;
    }

    @Override
    public void cleanup() {
    }

    @Override
    public boolean required() {
        return true;
    }

    /**
     * These methods are to be the "replacements" for any attribute calls in the entire plugin.
     * The idea here is that we are injecting custom attribute logic based on the type of attribute that is passed
     * in for the attribute parameter.
     * If an attribute is vanilla, we simply perform vanilla logic and let the game
     * handle it for us.
     * If an attribute is custom, we use PDCs to store attributes and modifiers long term in the same
     * exact way the vanilla game does, we just have to read from this PDC and do extra work every time we want to
     * access "custom" attributes.
     * <p>
     * The point of this service is to use these methods below as replacements for the following methods:
     * {@link Attributable#registerAttribute(Attribute)}
     * {@link Attributable#getAttribute(Attribute)}
     * <p>
     * And also provide functionality for {@link ItemStack} attribute modifier editing. Since there is a disconnect
     * with item meta and paper item components, we are going to reflect only the necessary {@link org.bukkit.inventory.meta.ItemMeta}
     * attribute modifier components. The return type will be similar to what the vanilla version returns, but will
     * use {@link AttributeWrapper}s instead in order to aggregate custom and vanilla attributes together.
     * The following mirrored methods are implemented here:
     * {@link ItemMeta#getAttributeModifiers()}
     * {@link ItemMeta#getAttributeModifiers(Attribute)}
     * {@link ItemMeta#getAttributeModifiers(EquipmentSlot)}
     * {@link ItemMeta#setAttributeModifiers(Multimap)}
     * {@link ItemMeta#addAttributeModifier(Attribute, AttributeModifier)}
     * {@link ItemMeta#hasAttributeModifiers()}
     * {@link ItemMeta#removeAttributeModifier(Attribute)}
     *
     * @return The AttributeService singleton to be used as an API.
     */
    public static AttributeService getInstance() {
        return SMPRPG.getInstance().getAttributeService();
    }

    /**
     * Registers an attribute on an instance that is allowed to store attributes.
     * If the attribute is a vanilla attribute, this is no different from calling
     * {@link Attributable#registerAttribute(Attribute)} on it directly.
     * If this attribute is custom, the instance will be given a default definition for the attribute and apply the
     * change IF AND ONLY IF the target did not have the attribute already registered in their PDC. This functionally
     * does nothing if they already have the attribute "registered".
     * @param target The attributable/PDC object.
     * @param wrapper The attribute to register.
     * @param <T> An object that is both capable of holding attributes and a persistent data container.
     */
    public <T extends Attributable & PersistentDataHolder> void registerAttribute(T target, AttributeWrapper wrapper) {

        // If this is vanilla, no need to do anything special!
        if (wrapper.isVanilla()) {
            if (wrapper.getWrappedAttribute() == null) {
                SMPRPG.getInstance().getLogger().severe("Incorrect attribute wrapper definition " + wrapper + ". If an attribute is vanilla, it must contain a wrapped attribute!");
                return;
            }
            target.registerAttribute(wrapper.getWrappedAttribute());
            return;
        }

        // Retrieve this thing's attribute container.
        var fullAttributeContainer = getAttributeContainer(target);

        // Do they already have it? Skip if so.
        if (fullAttributeContainer.getAttribute(wrapper) != null)
            return;

        // Add a new attribute instance to it and save the container.
        fullAttributeContainer.addAttribute(wrapper);
        setAttributeContainer(target, fullAttributeContainer);
    }

    /**
     * Removes the attribute from the target. Keep in mind this will clear any modifier/base data that may have been
     * set previously, but it can be good for a "recalculation" or a stat resync.
     * Keep in mind, if you want to edit attributes on items you do can skip registration/unregistration steps.
     * @param target The target to remove an attribute from.
     * @param wrapper The attribute to remove.
     * @param <T> The target that is meant to be an attribute holder and PDC holder.
     */
    public <T extends Attributable & PersistentDataHolder> void unregisterAttribute(T target, AttributeWrapper wrapper) {

        // If this is vanilla, no need to do anything special! We aren't responsible for de-registration.
        if (wrapper.isVanilla())
            return;

        // First make sure they have an attribute container. Grab it and if it doesn't exist, create it.
        var container = getAttributeContainer(target);

        // Now we can set the attribute instance.
        container.removeAttribute(wrapper);
        setAttributeContainer(target, container);
    }

    /**
     * Gets an attribute instance off of a target that can hold attributes and persistent data.
     * Returns null if they do not have it registered.
     * This should only be used against entities, similarly to how you would call {@link Attributable#getAttribute(Attribute)}
     * @param target The target to get an attribute off of.
     * @param wrapper The attribute to retrieve.
     * @return An attribute instance to tweak the values for.
     * @param <T> The attributable entity to hold this attribute. They must be able to also hold custom data.
     */
    @Nullable
    public <T extends PersistentDataHolder & Attributable> CustomAttributeInstance getAttribute(T target, AttributeWrapper wrapper) {

        // Inject vanilla logic. If this is a vanilla attribute, return a wrapper over the vanilla attribute instance.
        if (wrapper.isVanilla() && wrapper.getWrappedAttribute() != null) {

            // If they don't have the vanilla attribute registered, we can actually just return null. This is mirroring
            // vanilla logic.
            var attributeInstance = target.getAttribute(wrapper.getWrappedAttribute());
            if (attributeInstance == null)
                return null;

            // Return a wrapper over the vanilla attribute instance that will inject vanilla behavior into.
            return new VanillaAttributeInstanceWrapper(attributeInstance);
        }

        // We are working with a custom attribute, so we need to use PDC API.
        // Retrieve the container. If they don't have one, they probably don't have the attribute either.
        var container = target.getPersistentDataContainer().get(ATTRIBUTE_CONTAINER_KEY, CustomAttributeContainer.ADAPTER);
        if (container == null)
            return null;

        // They have a container! Retrieve the attribute if they have it.
        return container.getAttribute(wrapper);
    }

    /**
     * Retrieves the full attribute container that is contained on this target.
     * Keep in mind, that this only provides a mutable reflection of the container meaning you will have to
     * call setAttributeContainer(target) after you make your changes if you want them to persist.
     * @param target The target to get the full custom attribute container for.
     * @return A custom attribute container.
     */
    @NotNull
    public CustomAttributeContainer getAttributeContainer(PersistentDataHolder target) {
        return target.getPersistentDataContainer().getOrDefault(ATTRIBUTE_CONTAINER_KEY, CustomAttributeContainer.ADAPTER, new CustomAttributeContainer());
    }

    /**
     * Simply saves the given attribute container to the target. Will overwrite the last one.
     * @param target The target to save the attribute container to.
     * @param container The attribute container you would like to save.
     */
    public void setAttributeContainer(PersistentDataHolder target, CustomAttributeContainer container) {
        target.getPersistentDataContainer().set(ATTRIBUTE_CONTAINER_KEY, CustomAttributeContainer.ADAPTER, container);

        // Create an event so that plugins know that their custom attributes have changed.
        // This is critical so we know when to update things such as a player's max mana.
        var event = new CustomAttributeContainerUpdatedEvent(target, container);
        event.callEvent();
    }



    /**
     * Gets an attribute instance off of a target that can persist data.
     * If they do not have the attribute registered or an attribute container in their data, one will be created.
     * @param target The target that you would like to get attribute data from or create new attribute data on.
     * @param wrapper The attribute you would like to get/create.
     * @return An attribute instance that you can modify. Don't forget to call setAttribute() after making edits!
     */
    @NotNull
    public CustomAttributeInstance getOrCreateAttribute(LivingEntity target, AttributeWrapper wrapper) {

        // If this is vanilla, perform vanilla logic.
        if (wrapper.isVanilla() && wrapper.getWrappedAttribute() != null) {
            // Now retrieve the attribute. We should be given a vanilla attribute instance that functions as expected.
            var attrInstance = getAttribute(target, wrapper);

            // If the attribute was null attempt to register and re-get it. NOTE: registering an attribute in vanilla mc reverts it to default behavior.
            if (attrInstance == null) {
                target.registerAttribute(wrapper.getWrappedAttribute());
                attrInstance = getAttribute(target, wrapper);
            }

            // If the attribute is STILL null, the developer attempted to apply an illegal attribute. Force an exception.
            if (attrInstance == null)
                throw new IllegalStateException("Illegal attribute added to " + target.getName() + ". They cannot have the " + wrapper.getWrappedAttribute() + " attribute!");

            return attrInstance;
        }

        // Get the container.
        var container = getAttributeContainer(target);

        // Do they have the attribute?
        var attribute = container.getAttribute(wrapper);
        if (attribute != null)
            return attribute;

        // If it's missing, create a new instance and return it.
        var instance = new CustomAttributeInstance(0, new ArrayList<>());
        setAttribute(target, wrapper, instance);
        return instance;
    }

    /**
     * Sets the target to store the attribute instance under the attribute wrapper's key.
     * This is used to "save" an attribute instance to the target for the given attribute.
     * @param target The target to save attribute data to.
     * @param wrapper The attribute to save the instance to.
     * @param instance The attribute instance that you probably modified and wish to save.
     */
    public void setAttribute(PersistentDataHolder target, AttributeWrapper wrapper, CustomAttributeInstance instance) {

        // First make sure they have an attribute container. Grab it and if it doesn't exist, create it.
        var container = getAttributeContainer(target);

        // Now we can set the attribute instance.
        container.addAttribute(wrapper, instance);
        setAttributeContainer(target, container);
    }

    /**
     * Adds an attribute modifier to this item. If the attribute is vanilla, use vanilla behavior. If the attribute is
     * custom, add the modifier to the modifier container on the item stack.
     * @param item The item to add a modifier to.
     * @param attribute The attribute to modify.
     * @param attributeModifier The modifier to add.
     */
    public void addAttributeModifier(ItemStack item, AttributeWrapper attribute, AttributeModifier attributeModifier) {

        // If this is a vanilla attribute, do it the vanilla way.
        if (attribute.isVanilla() && attribute.getWrappedAttribute() != null) {
            item.editMeta(meta -> {
                meta.removeAttributeModifier(attribute.getWrappedAttribute(), attributeModifier);
                meta.addAttributeModifier(attribute.getWrappedAttribute(), attributeModifier);
            });
            return;
        }

        // If this is custom, retrieve the PDC that holds the attribute modifiers and add the attribute.
        item.editPersistentDataContainer(container -> {
            var modifierContainer = container.getOrDefault(ATTRIBUTE_MODIFIERS_KEY, PersistentDataType.TAG_CONTAINER, container.getAdapterContext().newPersistentDataContainer());
            var modifiersForAttribute = modifierContainer.getOrDefault(attribute.key(), CustomAttributeModifierCollection.ADAPTER, CustomAttributeModifierCollection.empty());
            modifiersForAttribute.addModifier(attributeModifier);
            modifierContainer.set(attribute.key(), CustomAttributeModifierCollection.ADAPTER, modifiersForAttribute);
            container.set(ATTRIBUTE_MODIFIERS_KEY, PersistentDataType.TAG_CONTAINER, modifierContainer);
        });

    }

    /**
     * Get all attribute modifiers for a specific attribute on an item.
     * @param item The item to query modifiers for.
     * @param wrapper The attribute to filter against.
     * @return A collection of attribute modifiers for the specific attribute.
     */
    @Nullable
    public Collection<AttributeModifier> getAttributeModifiers(ItemStack item, AttributeWrapper wrapper) {

        // If this is a vanilla attribute, do it the vanilla way.
        if (wrapper.isVanilla() && wrapper.getWrappedAttribute() != null)
            return item.getItemMeta().getAttributeModifiers(wrapper.getWrappedAttribute());

        // If this is custom, retrieve the PDC that holds the attribute modifiers and add the attribute.
        var container = item.getPersistentDataContainer();

        // If this item has no attribute container, return null.
        var modifierContainer = container.get(ATTRIBUTE_MODIFIERS_KEY, PersistentDataType.TAG_CONTAINER);
        if (modifierContainer == null)
            return null;

        // If this item has no modifiers for this attribute, return null.
        var modifierCollection = modifierContainer.get(wrapper.key(), CustomAttributeModifierCollection.ADAPTER);
        if (modifierCollection == null)
            return null;

        return modifierCollection.getModifiers();
    }

    /**
     * Query all attribute modifiers of an item, no matter what the modifiers are for.
     * This will return vanilla AND custom attributes. Returns null if the item has no
     * attribute modifiers.
     * @param item The item to query attributes for.
     * @return A map that contains key value pairs for attributes and their modifiers.
     */
    @Nullable
    public Multimap<AttributeWrapper, AttributeModifier> getAttributeModifiers(ItemStack item) {

        var meta = item.getItemMeta();
        if (meta == null)
            return null;

        // We need to find vanilla and custom attributes and put them together into a single map.
        // If the map ends up being empty, we will just return null to mimic vanilla behavior.
        HashMultimap<AttributeWrapper, AttributeModifier> modifiers = HashMultimap.create();

        // Start with vanilla modifiers. This is pretty simple as we are just using vanilla behavior.
        if (meta.getAttributeModifiers() != null) {
            for (var vanillaModifier : meta.getAttributeModifiers().entries()) {
                var attribute = AttributeWrapper.fromAttribute(vanillaModifier.getKey());
                if (attribute == null) {
                    SMPRPG.getInstance().getLogger().warning("Unknown attribute " + vanillaModifier.getKey() + ". Please add it to AttributeWrapper!");
                    continue;
                }
                modifiers.put(attribute, vanillaModifier.getValue());
            }
        }

        // Now do custom. Retrieve the container of modifiers and add all the modifiers.
        var container = item.getPersistentDataContainer();

        // If this item has no attribute container, return the vanilla modifiers only.
        var modifierContainer = container.get(ATTRIBUTE_MODIFIERS_KEY, PersistentDataType.TAG_CONTAINER);
        if (modifierContainer == null)
            return modifiers.isEmpty() ? null : modifiers;

        // We have a container, now add all the modifiers.
        for (var key : modifierContainer.getKeys()) {
            var attribute = AttributeWrapper.fromKey(key);
            var customModifiers = modifierContainer.get(key, CustomAttributeModifierCollection.ADAPTER);
            if (customModifiers == null || customModifiers.getModifiers().isEmpty())
                continue;

            modifiers.putAll(attribute, customModifiers.getModifiers());
        }

        // Return all the collected modifiers.
        return modifiers.isEmpty() ? null : modifiers;
    }

    /**
     * Does the exact same thing as getAttributeModifiers(), except ONLY returns attributes that are considered
     * custom. This is useful if you need to only apply logic for items with custom attributes.
     * @param item The item to query attributes for.
     * @return A map that contains key value pairs for attributes and their modifiers.
     */
    @Nullable
    public Multimap<AttributeWrapper, AttributeModifier> getCustomAttributeModifiers(ItemStack item) {

        // Simply just get all the modifiers, then filter out ones that aren't custom.
        var all = getAttributeModifiers(item);
        if (all == null)
            return null;

        // Filtering process. Simply check if the attribute wrapper is custom.
        Multimap<AttributeWrapper, AttributeModifier> filtered = HashMultimap.create();
        for (var entry : all.entries())
            if (entry != null && entry.getKey().isCustom())
                filtered.put(entry.getKey(), entry.getValue());

        return filtered;
    }

    /**
     * Removes attribute modifiers on this item that match the given key.
     * @param item The item to remove modifiers from.
     * @param namespacedKey The key to identify the attribute modifier.
     */
    public void removeAttributeModifiers(ItemStack item, NamespacedKey namespacedKey) {

        var meta = item.getItemMeta();
        if (meta == null)
            return;

        // Remove vanilla attribute modifiers that match the key.
        if (meta.getAttributeModifiers() != null)
            for (var vanillaModifier : meta.getAttributeModifiers().entries())
                if (vanillaModifier.getKey().getKey().equals(namespacedKey))
                    meta.removeAttributeModifier(vanillaModifier.getKey(), vanillaModifier.getValue());

        item.setItemMeta(meta);

        // Remove custom attribute modifiers with a matching key.
        item.editPersistentDataContainer(container -> {
            var modifierContainer = container.get(ATTRIBUTE_MODIFIERS_KEY, PersistentDataType.TAG_CONTAINER);
            if (modifierContainer == null)
                return;

            for (var key : modifierContainer.getKeys()) {
                var modifiers = modifierContainer.get(key, CustomAttributeModifierCollection.ADAPTER);
                if (modifiers == null)
                    continue;

                // Remove a modifier from this container if it has it.
                modifiers.removeModifier(namespacedKey);
                modifierContainer.set(key, CustomAttributeModifierCollection.ADAPTER, modifiers);
            }

            container.set(ATTRIBUTE_MODIFIERS_KEY, PersistentDataType.TAG_CONTAINER, modifierContainer);
        });
    }

    /**
     * Completely wipe any modifier data from this item.
     * @param item The item to completely remove all modifier data from.
     */
    public void clearAttributeModifiers(ItemStack item) {

        // All we need to do is remove the vanilla modifiers and clear the persistent data relating to modifiers.
        item.editMeta(meta -> meta.setAttributeModifiers(null));
        item.editPersistentDataContainer(container -> container.remove(ATTRIBUTE_MODIFIERS_KEY));
    }
}
