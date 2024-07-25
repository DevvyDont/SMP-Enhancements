package xyz.devvydont.smprpg.util.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class AbilityUtil {

    public static Component getAbilityComponent(String ability) {
        return Component.text("ABILITY ").decorate(TextDecoration.BOLD).append(Component.text(ability).decoration(TextDecoration.BOLD, false)).color(NamedTextColor.GOLD);
    }

    public static Component getCooldownComponent(String cooldown) {
        return Component.text("(" + cooldown + " cooldown)").color(NamedTextColor.DARK_GRAY);
    }

}
