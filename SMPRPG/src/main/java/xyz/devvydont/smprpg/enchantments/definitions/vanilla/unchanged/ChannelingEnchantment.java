package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class ChannelingEnchantment extends UnchangedEnchantment {

    public ChannelingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Summon ").color(NamedTextColor.GRAY)
                .append(Component.text("lightning").color(NamedTextColor.YELLOW))
                .append(Component.text(" during ").color(NamedTextColor.GRAY))
                .append(Component.text("thunderstorms").color(NamedTextColor.AQUA));
    }
}
