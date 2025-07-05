package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

/**
 * Provides simple one time defined classes that perform simple decoration procedures on strings to "decorate" them.
 * A simple example of this could be a "critical" decoration, that applies a gradient and surrounds text w/ symbols.
 * The idea is when you want to create a single, reusable instance.
 */
public interface ComponentDecorator {

    /**
     * Takes any text as input, and applies decoration to it to stylize it.
     * @param text Simple text to decorate.
     * @return A formatted component that can be used with the Adventure API.
     */
    Component decorate(String text);

    /**
     * Returns a decorator that will apply a text color to a message.
     * @param color The static color to apply.
     * @return A re-usable component decorator.
     */
    static ComponentDecorator color(final TextColor color) {
        return text -> ComponentUtils.create(text, color);
    }

    /**
     * Returns a decorator that will apply a gradient to a message.
     * @param start The starting color in the gradient.
     * @param end The ending color in the gradient.
     * @return A re-usable component decorator.
     */
    static ComponentDecorator gradient(final TextColor start, final TextColor end) {
        return text -> ComponentUtils.gradient(text, start, end);
    }

    /**
     * Returns a decorator that will surround text with symbols.
     * @param symbol The symbol to surround the text in.
     * @return A re-usable component decorator.
     */
    static ComponentDecorator symbolized(final String symbol) {
        return text -> ComponentUtils.create(String.format("%s%s%s", symbol, text, symbol));
    }

    /**
     * Returns a decorator that will surround text with symbols and apply a gradient to a message.
     * @param symbol The symbol to surround the text in.
     * @param start The starting color in the gradient.
     * @param end The ending color in the gradient.
     * @return A re-usable component decorator.
     */
    static ComponentDecorator symbolizedGradient(final String symbol, final TextColor start, final TextColor end) {
        return text -> ComponentUtils.gradient(String.format("%s%s%s", symbol, text, symbol), start, end);
    }

}
