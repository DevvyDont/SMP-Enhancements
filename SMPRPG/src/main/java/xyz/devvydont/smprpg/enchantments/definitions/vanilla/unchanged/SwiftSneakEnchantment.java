package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class SwiftSneakEnchantment extends UnchangedEnchantment {

    public static int getSneakPercent(int level) {
        return level * 15 + 30;
    }

    public SwiftSneakEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Sneak speed is ").color(NamedTextColor.GRAY)
                .append(Component.text(getSneakPercent(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" of walk speed").color(NamedTextColor.GRAY));
    }
}
