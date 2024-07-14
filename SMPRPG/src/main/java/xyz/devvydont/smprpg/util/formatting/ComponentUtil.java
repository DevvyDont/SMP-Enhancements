package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ComponentUtil {

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

}
