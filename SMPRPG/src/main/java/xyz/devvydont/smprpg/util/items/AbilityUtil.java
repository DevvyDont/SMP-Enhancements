package xyz.devvydont.smprpg.util.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

public class AbilityUtil {

    public static Component getAbilityComponent(String ability) {
        return ComponentUtils.create("ABILITY ", TextDecoration.BOLD).append(ComponentUtils.create(ability, NamedTextColor.GOLD).decoration(TextDecoration.BOLD, false));
    }

    public static Component getCooldownComponent(String cooldown) {
        return ComponentUtils.create("(" + cooldown + " cooldown)", NamedTextColor.DARK_GRAY);
    }

    public static Component getHealthCostComponent(int hp) {
        return ComponentUtils.create("Usage cost: ").append(ComponentUtils.create(hp + Symbols.HEART, NamedTextColor.RED));
    }

}
