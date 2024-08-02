package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatUtil {

    public static Component getPrefix(char symbol, TextColor color) {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text(symbol).color(color))
                .append(Component.text("] ").color(NamedTextColor.GRAY));
    }

    /**
     * Translates a generic error message from commands into a chat component formatted correctly
     *
     * @param text
     * @return
     */
    public static Component getErrorMessage(String text) {
        return getPrefix('!', NamedTextColor.DARK_RED)
                .append(Component.text(text).color(NamedTextColor.RED));
    }

    /**
     * Translates a generic success message from commands into a chat component formatted correctly
     *
     * @param text
     * @return
     */
    public static Component getSuccessMessage(String text) {
        return getPrefix('!', NamedTextColor.DARK_GREEN)
                .append(Component.text(text).color(NamedTextColor.GREEN));
    }

    /**
     * Given a component, add a prefix. Mainly used for command responses or plugin alerts
     *
     * @param component
     * @return
     */
    public static Component getGenericMessage(Component component) {
        return getPrefix('!', NamedTextColor.YELLOW)
                .append(component);
    }

    public static List<Component> cleanItalics(Collection<Component> components) {

        List<Component> newComponents = new ArrayList<>();

        for (Component component : components)
            newComponents.add(component.decoration(TextDecoration.ITALIC, false));

        return newComponents;
    }

    /**
     * Returns a component to display a level with the ✦ icon
     *
     * @param level
     * @return
     */
    public static Component getPowerComponent(int level) {
        return Component.text(Symbols.POWER + level).color(NamedTextColor.YELLOW);
    }

    /**
     * Does the same thing as getPowerComponent() but wraps it in brackets
     *
     * @param level
     * @return
     */
    public static Component getBracketedPowerComponent(int level) {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(getPowerComponent(level))
                .append(Component.text("]").color(NamedTextColor.GRAY));
    }

}
