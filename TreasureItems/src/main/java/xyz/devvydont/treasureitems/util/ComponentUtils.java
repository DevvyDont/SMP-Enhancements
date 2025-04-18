package xyz.devvydont.treasureitems.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtils {

    public static final TextColor TEXT_DEFAULT = NamedTextColor.GRAY;


    public static Component UNREPAIRABLE = merge(
            Component.text("Cannot be ", NamedTextColor.GRAY),
            Component.text("repaired", NamedTextColor.RED),
            Component.text("!", NamedTextColor.GRAY)
    );

    public static Component UNREPAIRABLE_ENCHANTABLE = merge(
            Component.text("Cannot be ", NamedTextColor.GRAY),
            Component.text("repaired/enchanted", NamedTextColor.RED),
            Component.text("!", NamedTextColor.GRAY)
    );

    public static Component UNENCHANTABLE = merge(
            Component.text("Cannot be ", NamedTextColor.GRAY),
            Component.text("enchanted", NamedTextColor.RED),
            Component.text("!", NamedTextColor.GRAY)
    );

    /**
     * Creates a text component with the default styling.
     *
     * @param message     The message to turn into a component.
     * @param decorations Additional decorations to apply to the component.
     * @return The styled text component.
     */
    public static TextComponent create(String message, TextDecoration... decorations) {
        return create(message, TEXT_DEFAULT, decorations);
    }

    /**
     * Creates a text component that's styled with the specified color and decorations.
     *
     * @param message     The message to turn into a component.
     * @param color       The color to apply to the component.
     * @param decorations Additional decorations to apply to the component.
     * @return The styled text component.
     */
    public static TextComponent create(String message, TextColor color, TextDecoration... decorations) {
        return Component.text(message, Style.style(color, decorations));
    }

    public static Component merge(Component... components) {
        Component merged = Component.empty();
        for (Component component : components) {
            merged = merged.append(component);
        }
        return merged;
    }

    public static Component removeItalics(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }

    public static List<Component> removeItalics(List<Component> components) {
        List<Component> fixed = new ArrayList<>();
        for (Component component : components)
            fixed.add(removeItalics(component));
        return fixed;
    }

    public static List<Component> removeItalics(Component...components) {
        List<Component> fixed = new ArrayList<>();
        for (Component component : components)
            fixed.add(removeItalics(component));
        return fixed;
    }

    public static Component getAbilityComponent(String ability) {
        return ComponentUtils.create("ABILITY ", TextDecoration.BOLD).append(ComponentUtils.create(ability, NamedTextColor.GOLD).decoration(TextDecoration.BOLD, false));
    }

    public static Component getCooldownComponent(String cooldown) {
        return ComponentUtils.create("(" + cooldown + " cooldown)", NamedTextColor.DARK_GRAY);
    }


}
