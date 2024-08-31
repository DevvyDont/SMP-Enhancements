package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComponentUtils {

    public static final TextColor DEFAULT_TEXT = NamedTextColor.GRAY;

    public static final TextColor BRACKET_COLOR = NamedTextColor.GRAY;

    /**
     * [!]
     *
     * @return
     */
    public static Component getAlertPrefix(TextColor color) {
        return Component.text("[").color(BRACKET_COLOR)
                .append(Component.text("!").color(color))
                .append(Component.text("] ").color(BRACKET_COLOR));
    }

    /**
     * Gets an alert message like: [!] blahblahblah
     *
     * @param message
     * @param alertColor
     * @return
     */
    public static Component getAlertMessage(Component message, TextColor alertColor) {
        return getAlertPrefix(alertColor).append(message);
    }

    /**
     * Gets an alert message like: [!] blahblahblah
     * with the option of providing in a color for the entire message string
     *
     * @param message
     * @param alertColor
     * @param textColor
     * @return
     */
    public static Component getAlertMessage(Component message, TextColor alertColor, TextColor textColor) {
        return getAlertPrefix(alertColor).append(message.color(textColor));
    }

    /**
     * Returns a simple component with a certain color
     *
     * @param text
     * @param textColor
     * @return
     */
    public static Component getColoredComponent(String text, TextColor textColor) {
        return Component.text(text).color(textColor);
    }


    /**
     * Returns a component with default gray text
     *
     * @param text
     * @return
     */
    public static Component getDefaultText(String text) {
        return getColoredComponent(text, DEFAULT_TEXT);
    }

    public static Component getUpgradeComponent(String old, String _new, TextColor textColor) {
        return getColoredComponent(old + Symbols.RIGHT_ARROW, NamedTextColor.DARK_GRAY).append(getColoredComponent(_new, textColor));
    }

    public static Component getUpgradeComponent(Component old, Component _new) {
        return old.append(Component.text(Symbols.RIGHT_ARROW)).color(NamedTextColor.DARK_GRAY).append(_new);
    }

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
