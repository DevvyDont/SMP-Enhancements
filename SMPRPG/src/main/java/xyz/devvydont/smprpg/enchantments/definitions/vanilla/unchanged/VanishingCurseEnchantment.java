package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class VanishingCurseEnchantment extends UnchangedEnchantment {

    public VanishingCurseEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public TextColor getEnchantColor() {
        return NamedTextColor.RED;
    }

    @Override
    public Component getDescription() {
        return Component.text("Vanishes when ").color(NamedTextColor.GRAY)
                .append(Component.text("dropped from death").color(NamedTextColor.DARK_RED));
    }
}
