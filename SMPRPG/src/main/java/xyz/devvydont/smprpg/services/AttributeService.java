package xyz.devvydont.smprpg.services;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.attribute.CustomAttributeContainer;
import xyz.devvydont.smprpg.attribute.CustomAttributeInstance;
import xyz.devvydont.smprpg.events.CustomAttributeContainerUpdatedEvent;

import java.util.ArrayList;

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

    @Override
    public boolean setup() {
        SMPRPG.getInstance().getServer().getPluginManager().registerEvents(this, SMPRPG.getInstance());
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
     * Registers an attribute on an instance that is allowed to store attributes.
     * If the attribute is a vanilla attribute, this is no different than calling registerAttribute() on it directly.
     * If this attribute is custom, the instance will be given a default definition for the attribute (if it doesn't have it already).
     * Keep in mind, if you want to edit attributes on items you do can skip registration/unregistration steps.
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
     * @param target The target to get an attribute off of.
     * @param wrapper The attribute to retrieve.
     * @return An attribute instance to tweak the values for.
     */
    @Nullable
    public CustomAttributeInstance getAttribute(PersistentDataHolder target, AttributeWrapper wrapper) {

        // Retrieve the container. If they don't have one, they probably don't have the attribute either.
        var container = target.getPersistentDataContainer().get(ATTRIBUTE_CONTAINER_KEY, CustomAttributeContainer.ADAPTER);
        if (container == null)
            return null;

        // They have a container! Retrieve the attribute if they have it.
        return container.getAttribute(wrapper);
    }

    /**
     * Gets an attribute instance off of a target that can persist data.
     * If they do not have the attribute registered or an attribute container in their data, one will be created.
     * @param target The target that you would like to get attribute data from or create new attribute data on.
     * @param wrapper The attribute you would like to get/create.
     * @return An attribute instance that you can modify. Don't forget to call setAttribute() after making edits!
     */
    @NotNull
    public CustomAttributeInstance getOrCreateAttribute(PersistentDataHolder target, AttributeWrapper wrapper) {

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

    @EventHandler
    private void __onBreakDirt(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.DIRT))
            return;

        var target = event.getPlayer();
        this.registerAttribute(target, AttributeWrapper.INTELLIGENCE);
        this.registerAttribute(target, AttributeWrapper.REGENERATION);

        var container = getAttributeContainer(target);
        var intelligence = container.getAttribute(AttributeWrapper.INTELLIGENCE);
        if (intelligence == null) {
            System.out.println("failed to get intelligence");
            return;
        }
        intelligence.addModifier(new AttributeModifier(new NamespacedKey("smprpg", "temp_reason"), 15, AttributeModifier.Operation.ADD_NUMBER));
        intelligence.addModifier(new AttributeModifier(new NamespacedKey("smprpg", "temp_reason2"), 1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        this.setAttribute(target, AttributeWrapper.INTELLIGENCE, intelligence);

        var regeneration = container.getAttribute(AttributeWrapper.REGENERATION);
        if (regeneration == null) {
            System.out.println("failed to get regeneration");
            return;
        }

        regeneration.setBaseValue(15.6);
        regeneration.addModifier(new AttributeModifier(new NamespacedKey("smprpg", "yaas"), .5, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        regeneration.addModifier(new AttributeModifier(new NamespacedKey("smprpg", "yaaaaaas"), .75, AttributeModifier.Operation.ADD_SCALAR));
        this.setAttribute(target, AttributeWrapper.REGENERATION, regeneration);

        System.out.println(getAttributeContainer(target));
    }
}
