package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class QuickChargeEnchantment extends UnchangedEnchantment {

    public static int getChargePercentageReduction(int level) {
        return level * 25;
    }

    public QuickChargeEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Sets loading time to ").color(NamedTextColor.GRAY)
                .append(Component.text("-" + getChargePercentageReduction(getLevel()) + "%").color(getLevel() >= 5 ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.GREEN));
    }
}
