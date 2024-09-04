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
    public static final TextComponent SYMBOL_BRACKET_LEFT = create("[");
    public static final TextComponent SYMBOL_BRACKET_RIGHT = create("]");
    public static final TextComponent SYMBOL_EXCLAMATION = create("!");

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

    // -----------
    //   Helpers
    // -----------

    /**
     * Merges a collection of component into a single component.
     *
     * @param components The components to merge together.
     * @return The result of the merge.
     */
    public static Component merge(Component... components) {
        var outputComponent = EMPTY;
        for (var component : components) {
            outputComponent = outputComponent.append(component);
        }
        return outputComponent;
    }

    /**
     * Removes the italics from a collection of components.
     *
     * @param components The components to clean.
     * @return The cleaned components.
     */
    public static List<Component> cleanItalics(Collection<Component> components) {
        var cleanComponents = new ArrayList<Component>(components.size());
        for (var component : components) {
            cleanComponents.add(component.decoration(TextDecoration.ITALIC, false));
        }
        return cleanComponents;
    }

    // -----------
    //   Presets
    // -----------

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param message A message alerting the user about something.
     * @return The styled text component.
     */
    public static TextComponent alert(String message) {
        return alert(create(message));
    }

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param message     A message alerting the user about something.
     * @param prefixColor The color of the alert prefix.
     * @return The styled text component.
     */
    public static TextComponent alert(String message, TextColor prefixColor) {
        return alert(create(message), prefixColor);
    }

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param message     A message alerting the user about something.
     * @param prefixColor The color of the alert prefix.
     * @param textColor   The color of the alert text.
     * @return The styled text component.
     */
    public static TextComponent alert(String message, TextColor prefixColor, TextColor textColor) {
        return alert(create(message, textColor), prefixColor);
    }

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param message A message alerting the user about something.
     * @return The styled text component.
     */
    public static TextComponent alert(Component message) {
        return alert(message, TEXT_DEFAULT);
    }

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param message     A message alerting the user about something.
     * @param prefixColor The color of the alert prefix.
     * @return The styled text component.
     */
    public static TextComponent alert(Component message, TextColor prefixColor) {
        return alert(SYMBOL_EXCLAMATION.color(prefixColor), message);
    }

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param prefix  The contents of the alert prefix.
     * @param message A message alerting the user about something.
     * @return The styled text component.
     */
    public static TextComponent alert(Component prefix, Component message) {
        return EMPTY
                .append(SYMBOL_BRACKET_LEFT)
                .append(prefix)
                .append(SPACE)
                .append(SYMBOL_BRACKET_RIGHT)
                .append(SPACE)
                .append(message);
    }

    /**
     * Creates a text component that's styled like a success message.
     *
     * @param text A message explaining the success.
     * @return The styled text component.
     */
    public static TextComponent success(String text) {
        return alert(text, NamedTextColor.DARK_GREEN, NamedTextColor.GREEN);
    }

    /**
     * Creates a text component that's styled like a success message.
     *
     * @param message A message explaining the success.
     * @return The styled text component.
     */
    public static TextComponent success(Component message) {
        return alert(message, NamedTextColor.DARK_GREEN);
    }

    /**
     * Creates a text component that's styled like an error message.
     *
     * @param text A message explaining the failure.
     * @return The styled text component.
     */
    public static TextComponent error(String text) {
        return alert(text, NamedTextColor.DARK_RED, NamedTextColor.RED);
    }

    /**
     * Creates a text component that's styled like an error message.
     *
     * @param message A message explaining the failure.
     * @return The styled text component.
     */
    public static TextComponent error(Component message) {
        return alert(message, NamedTextColor.DARK_RED);
    }

    /**
     * Creates a text component that's styled like an upgrade message.
     *
     * @param oldValue      The value before the upgrade.
     * @param newValue      The value after the upgrade.
     * @param newValueColor The colour of the upgraded value.
     * @return The styled text component.
     */
    public static TextComponent upgrade(String oldValue, String newValue, TextColor newValueColor) {
        return upgrade(create(oldValue, NamedTextColor.DARK_GRAY), create(newValue, newValueColor));
    }

    /**
     * Creates a text component that's styled like an upgrade message.
     *
     * @param oldValue The value before the upgrade.
     * @param newValue The value after the upgrade.
     * @return The styled text component.
     */
    public static TextComponent upgrade(Component oldValue, Component newValue) {
        return EMPTY
                .append(oldValue)
                .append(SPACE)
                .append(ComponentUtils.create(Symbols.RIGHT_ARROW, NamedTextColor.DARK_GRAY))
                .append(SPACE)
                .append(newValue);
    }

    /**
     * Creates a text component that displays a power level.
     *
     * @param level The level to display.
     * @return The styled text component.
     */
    public static TextComponent powerLevel(int level) {
        return ComponentUtils.create(Symbols.POWER + level, NamedTextColor.YELLOW);
    }

    /**
     * Creates a text component that displays a power level as a prefix.
     *
     * @param level The level to display.
     * @return The styled text component.
     */
    public static TextComponent powerLevelPrefix(int level) {
        return EMPTY.append(SYMBOL_BRACKET_LEFT).append(powerLevel(level)).append(SYMBOL_BRACKET_RIGHT);
    }
}
