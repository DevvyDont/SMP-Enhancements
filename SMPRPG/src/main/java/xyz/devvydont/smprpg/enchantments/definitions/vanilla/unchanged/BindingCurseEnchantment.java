package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class BindingCurseEnchantment extends UnchangedEnchantment {

    public BindingCurseEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public TextColor getEnchantColor() {
        return NamedTextColor.RED;
    }

    @Override
    public Component getDescription() {
        return Component.text("Cannot be ").color(NamedTextColor.GRAY)
                .append(Component.text("removed").color(NamedTextColor.DARK_RED))
                .append(Component.text(" once worn").color(NamedTextColor.GRAY));
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }

    @Override
    public int getSkillRequirementToAvoid() {
        return 10;
    }
}
