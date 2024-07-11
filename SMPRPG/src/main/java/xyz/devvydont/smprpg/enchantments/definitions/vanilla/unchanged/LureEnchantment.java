package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class LureEnchantment extends UnchangedEnchantment {

    public static int getDecreaseTime(int level) {
        return level * 5;
    }

    public LureEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Decreases catch time by ").color(NamedTextColor.GRAY)
                .append(Component.text("-" + getDecreaseTime(getLevel()) + "s").color(NamedTextColor.GREEN));
    }
}
