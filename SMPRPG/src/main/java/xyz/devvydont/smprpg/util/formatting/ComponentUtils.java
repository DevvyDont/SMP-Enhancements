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

    /**
     * Merges a collection of component into a single component.
     *
     * @param components The components to merge together.
     * @return The result of the merge.
     */
    public static Component merge(Component... components) {
        var noComponentsProvided = components.length == 0;
        if (noComponentsProvided) {
            return Component.empty();
        }

        var oneComponentProvided = components.length == 1;
        if (oneComponentProvided) {
            return components[0];
        }

        var outputComponent = components[0];
        for (int i = 1; i < components.length; i++) {
            outputComponent = outputComponent.append(components[i]);
        }
        return outputComponent;
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
     * @param message A message alerting the user about something.
     * @param prefixColor The color of the alert prefix.
     * @return The styled text component.
     */
    public static TextComponent alert(String message, TextColor prefixColor) {
        return alert(create(message), prefixColor);
    }

    /**
     * Creates a text component that's styled like an alert message.
     *
     * @param message A message alerting the user about something.
     * @param prefixColor The color of the alert prefix.
     * @param textColor The color of the alert text.
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
     * @param message A message alerting the user about something.
     * @param prefixColor The color of the alert prefix.
     * @return The styled text component.
     */
    public static TextComponent alert(Component message, TextColor prefixColor) {
        return EMPTY
                .append(BRACKET_LEFT)
                .append(create("! ", prefixColor))
                .append(BRACKET_RIGHT)
                .append(SPACE)
                .append(message);
    }

    /**
     * Creates a text component that's styled like a success message.
     *
     * @param text A message explaining the success.
     * @return The styled text component.
     */
    public static Component success(String text) {
        return alert(text, NamedTextColor.DARK_GREEN, NamedTextColor.GREEN);
    }

    /**
     * Creates a text component that's styled like an error message.
     *
     * @param text A message explaining the failure.
     * @return The styled text component.
     */
    public static Component error(String text) {
        return alert(text, NamedTextColor.DARK_RED, NamedTextColor.RED);
    }

    // -------------
    //  To Refactor
    // -------------




    public static Component getUpgradeComponent(String old, String _new, TextColor textColor) {
        return create(old + Symbols.RIGHT_ARROW, NamedTextColor.DARK_GRAY).append(create(_new, textColor));
    }

    public static Component getUpgradeComponent(Component old, Component _new) {
        return old.append(Component.text(Symbols.RIGHT_ARROW)).color(NamedTextColor.DARK_GRAY).append(_new);
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
