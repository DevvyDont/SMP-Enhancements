package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class LoyaltyEnchantment extends UnchangedEnchantment {

    public static Component getSpeedModifier(int level) {
        return switch (level) {
            case 0 -> Component.text("");
            case 1 -> Component.text("slowly").color(NamedTextColor.YELLOW);
            case 2 -> Component.text("quickly").color(NamedTextColor.GREEN);
            case 3 -> Component.text("very quickly").color(NamedTextColor.LIGHT_PURPLE);
            default -> Component.text("AT LIGHTSPEED").color(NamedTextColor.LIGHT_PURPLE);
        };
    }

    public LoyaltyEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Returns when thrown ").color(NamedTextColor.GRAY)
                .append(getSpeedModifier(getLevel()));
    }

    @Override
    public int getSkillRequirement() {
        return 48;
    }
}
