package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class WindBurstEnchantment extends UnchangedEnchantment {

    public WindBurstEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Propel upwards ").color(NamedTextColor.GRAY)
                .append(Component.text(getLevel() * 5)).color(NamedTextColor.GREEN)
                .append(Component.text(" blocks when dealing damage").color(NamedTextColor.GRAY));
    }

    @Override
    public int getSkillRequirement() {
        return 78;
    }
}
