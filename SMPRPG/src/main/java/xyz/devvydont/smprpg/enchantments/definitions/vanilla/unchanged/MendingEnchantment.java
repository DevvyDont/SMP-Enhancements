package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class MendingEnchantment extends UnchangedEnchantment {


    public MendingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Repaired when ").color(NamedTextColor.GRAY)
                .append(Component.text("earning experience").color(NamedTextColor.LIGHT_PURPLE));
    }

    @Override
    public int getSkillRequirement() {
        return 60;
    }
}
