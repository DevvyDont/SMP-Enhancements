package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class PunchEnchantment extends UnchangedEnchantment {

    public static int getKnockbackPower(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 105;
            case 2 -> 190;
            default -> level * 100;
        };
    }

    public PunchEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases knockback by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getKnockbackPower(getLevel()) + "%").color(NamedTextColor.GREEN));
    }

    @Override
    public int getSkillRequirement() {
        return 40;
    }
}
