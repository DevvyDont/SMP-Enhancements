package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class FireAspectEnchantment extends UnchangedEnchantment {

    public static int getSecondsOfBurn(int level) {
        return level * 4;
    }

    public FireAspectEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Ignites enemies for ").color(NamedTextColor.GRAY)
                .append(Component.text(getSecondsOfBurn(getLevel()) + "s").color(NamedTextColor.GOLD));
    }
}
