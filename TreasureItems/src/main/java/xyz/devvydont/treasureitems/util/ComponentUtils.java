package xyz.devvydont.treasureitems.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class ComponentUtils {


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

}
