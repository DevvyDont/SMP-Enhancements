package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class PiercingEnchantment extends UnchangedEnchantment {

    public PiercingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Pierces through ").color(NamedTextColor.GRAY)
                .append(Component.text(getLevel()).color(NamedTextColor.GREEN))
                .append(Component.text(" enemy(s)").color(NamedTextColor.GRAY));
    }

    @Override
    public int getSkillRequirement() {
        return 52;
    }
}
