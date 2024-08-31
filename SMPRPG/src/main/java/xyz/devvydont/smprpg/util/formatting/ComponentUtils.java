package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ComponentUtils {
    // Colors
    public static final TextColor TEXT_DEFAULT = NamedTextColor.GRAY;

    // Presets
    public static final TextComponent SPACE = create(" ");
    public static final TextComponent EMPTY = Component.empty();
    public static final TextComponent BRACKET_LEFT = create("[", TEXT_DEFAULT);
    public static final TextComponent BRACKET_RIGHT = create("]", TEXT_DEFAULT);


    /**
     * Creates a text component with the default styling.
     *
     * @param text        The string to turn into a component.
     * @param decorations Additional decorations to apply to the component.
     * @return The styled text component.
     */
    public static TextComponent create(String text, TextDecoration... decorations) {
        return create(text, TEXT_DEFAULT, decorations);
    }

    /**
     * Creates a text component that's styled with the specified color and decorations.
     *
     * @param text        The string to turn into a component.
     * @param color       The color to apply to the component.
     * @param decorations Additional decorations to apply to the component.
     * @return The styled text component.
     */
    public static TextComponent create(String text, TextColor color, TextDecoration... decorations) {
        return Component.text(text, Style.style(color, decorations));
    }

    // ---------------
    //   To Refactor
    // ---------------

    /**
     * [!]
     *
     * @return
     */
    public static Component getAlertPrefix(TextColor color) {
        return Component.text("[").color(TEXT_DEFAULT)
                .append(Component.text("!").color(color))
                .append(Component.text("] ").color(TEXT_DEFAULT));
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






    public static Component getUpgradeComponent(String old, String _new, TextColor textColor) {
        return create(old + Symbols.RIGHT_ARROW, NamedTextColor.DARK_GRAY).append(create(_new, textColor));
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
