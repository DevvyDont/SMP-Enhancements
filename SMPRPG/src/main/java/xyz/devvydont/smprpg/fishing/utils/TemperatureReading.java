package xyz.devvydont.smprpg.fishing.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.block.Block;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.Objects;

/**
 * Vanilla Minecraft has a "Temperature" mechanic that the game uses to calculate things for various world behaviors.
 * It is a very underutilized mechanic, but fishing relies on it.
 * Temperatures can range from -0.5 -> 2.0, and the changes are pretty drastic and volatile. Because of this, we are
 * going to assign temperature ranges to capture significant thresholds.
 */
public enum TemperatureReading {
    FREEZING(ComponentUtils.gradient(Symbols.SNOWFLAKE + "FREEZING" + Symbols.SNOWFLAKE, Objects.requireNonNull(TextColor.fromHexString("#00E5FF")), Objects.requireNonNull(TextColor.fromHexString("#0026FF")))),
    COLD(ComponentUtils.create("Cold", NamedTextColor.AQUA)),
    TEMPERATE(ComponentUtils.create("Temperate", NamedTextColor.GREEN)),
    WARM(ComponentUtils.create("Warm", NamedTextColor.YELLOW)),
    SCORCHING(ComponentUtils.gradient(Symbols.FIRE + "SCORCHING" + Symbols.FIRE, Objects.requireNonNull(TextColor.fromHexString("#FF0000")), Objects.requireNonNull(TextColor.fromHexString("#FFEA98")))),
    ;

    public final Component Component;

    TemperatureReading(Component component) {
        Component = component;
    }

    /**
     * Temperature calculation logic. No need to overcomplicate this.
     * @param value The temperature value, most commonly retrieved from {@link Block#getTemperature()}.
     * @return The temperature group.
     */
    public static TemperatureReading fromValue(double value) {

        // Absolutely freezing. Very rarely temperatures get this low.
        if (value <= -0.25f) return FREEZING;

        // Chilly, able to snow or almost can snow.
        if (value <= 0.5f) return COLD;

        // Not too hot, not too cold.
        if (value <= 0.85f) return TEMPERATE;

        // Warm, but not scorching.
        if (value <= 1.75f) return WARM;

        // It's hot.
        return SCORCHING;

    }
}
