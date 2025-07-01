package xyz.devvydont.smprpg.attribute.listeners;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.services.AttributeService;

/**
 * A class responsible for the removal/application of custom attribute modifiers as vanilla minecraft cannot
 * handle that for us. If this listener is not active, custom attributes will seem to have no effect if they are
 * present on items as modifiers. For example, when we hold a wand that gives us +50 intelligence, we need that
 * update to apply to the player once they hold it, and to be removed when they stop holding it.
 */
public class AttributeApplyListener implements Listener {

    /**
     * When an entity swaps out any equipment, remove old modifiers and apply new ones.
     * @param event The {@link EntityEquipmentChangedEvent} that is fired whenever an entity changes any equipment.
     */
    @EventHandler
    public void onSwapArmorWithCustomAttributes(EntityEquipmentChangedEvent event) {

        // This is actually very simple due to how the event gives us the information.
        // Simply remove old attribute modifiers, and apply new ones.
        // There is a bit of nuance to this however, as we should do removals first then additions. That way, we are
        // always ensuring that modifiers are present for newly equipped items.
        Multimap<AttributeWrapper, AttributeModifier> toRemove = HashMultimap.create();
        Multimap<AttributeWrapper, AttributeModifier> toApply = HashMultimap.create();

        for (var entry : event.getEquipmentChanges().entrySet()) {
            var old = entry.getValue().oldItem();
            var _new = entry.getValue().newItem();

            // Add the modifiers to their respective lists. Remember, we only care about CUSTOM attributes.
            // The modifiers also have no effect if the slot doesn't result in a match.
            var oldModifiers = AttributeService.getInstance().getCustomAttributeModifiers(old);
            var newModifiers = AttributeService.getInstance().getCustomAttributeModifiers(_new);
            if (oldModifiers != null)
                for (var mod : oldModifiers.entries())
                    if (mod.getValue().getSlotGroup().test(entry.getKey()))
                        toRemove.put(mod.getKey(), mod.getValue());
            if (newModifiers != null)
                for (var mod : newModifiers.entries())
                    if (mod.getValue().getSlotGroup().test(entry.getKey()))
                        toApply.put(mod.getKey(), mod.getValue());
        }

        // Now just remove and apply modifiers. Pretty simple.
        for (var entry : toRemove.asMap().entrySet()) {

            var attrInstance = AttributeService.getInstance().getAttribute(event.getEntity(), entry.getKey());
            if (attrInstance == null)
                continue;

            for (var modifier : entry.getValue())
                attrInstance.removeModifier(modifier);

            attrInstance.save(event.getEntity(), entry.getKey());
        }

        // Do the same thing but for additions.
        for (var entry : toApply.asMap().entrySet()) {

            var attrInstance = AttributeService.getInstance().getAttribute(event.getEntity(), entry.getKey());
            if (attrInstance == null)
                continue;

            for (var modifier : entry.getValue())
                attrInstance.addModifier(modifier);

            attrInstance.save(event.getEntity(), entry.getKey());
        }
    }
}
