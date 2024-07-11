package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class RespirationEnchantment extends UnchangedEnchantment {

    public static int getAdditionalBreath(int level) {
        return level * 15;
    }

    public RespirationEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases lung capacity by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getAdditionalBreath(getLevel()) + "s").color(NamedTextColor.GREEN));
    }
}
