package xyz.devvydont.smprpg.items.interfaces;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.ArrayList;
import java.util.List;

public interface IConsumable {

    Consumable getConsumableComponent();

    static List<Component> generateEffectComponent(ConsumeEffect effect) {

        // Begin checking what kind of effect this is. If an effect implements two of the interfaces we are checking
        // here, we are just going to treat this chain of if statements as a "priority" system.

        // Random teleportation.
        if (effect instanceof ConsumeEffect.TeleportRandomly)
            return List.of(ComponentUtils.create("* Teleport to a random location", NamedTextColor.LIGHT_PURPLE));

        // Status effect application.
        if (effect instanceof ConsumeEffect.ApplyStatusEffects statusEffectsApplicator) {

            var components = new ArrayList<Component>();

            for (var potionEffect : statusEffectsApplicator.effects()) {
                var name = MinecraftStringUtils.getTitledString(potionEffect.getType().getKey().value());
                var color = potionEffect.getType().getColor();
                var textColor = TextColor.color(color.getRed(), color.getGreen(), color.getBlue());
                var sec = potionEffect.getDuration() / 20;
                var time = String.format(" %d:%02d", sec / 60, sec % 60);

                components.add(ComponentUtils.merge(
                        ComponentUtils.create("* "),
                        ComponentUtils.create(name + " " + (potionEffect.getAmplifier()+1), textColor),
                        ComponentUtils.create(String.format(time + " (%d%%)", (int)Math.ceil(statusEffectsApplicator.probability() * 100)), NamedTextColor.DARK_GRAY)
                ));
            }
            return components;
        }

        if (effect instanceof ConsumeEffect.ClearAllStatusEffects)
            return List.of(ComponentUtils.create("* Clear all potion effects", NamedTextColor.AQUA));

        if (effect instanceof ConsumeEffect.RemoveStatusEffects statusEffectRemover)
            return List.of(ComponentUtils.create(String.format("* Remove %d types of potion effects", statusEffectRemover.removeEffects().size()), NamedTextColor.AQUA));

        return List.of();
    }

    static List<Component> generateConsumabilityComponent(IConsumable consumable) {
        var lore = new ArrayList<Component>();

        // Start with the header. Letting them know this is food and how long it takes to eat.
        lore.add(ComponentUtils.merge(
                ComponentUtils.create("Consumable: ", NamedTextColor.GOLD),
                ComponentUtils.create(String.format("(%.1fs)", consumable.getConsumableComponent().consumeSeconds()), NamedTextColor.DARK_GRAY)
        ));

        // Effects if they are present.
        if (!consumable.getConsumableComponent().consumeEffects().isEmpty()) {
            lore.add(ComponentUtils.create("Additional Effects: "));

            // Loop through every effect. Depending on the type of interface that it implements, explain what it does.
            for (var effect : consumable.getConsumableComponent().consumeEffects())
                lore.addAll(IConsumable.generateEffectComponent(effect));
        }

        return lore;
    }

}
