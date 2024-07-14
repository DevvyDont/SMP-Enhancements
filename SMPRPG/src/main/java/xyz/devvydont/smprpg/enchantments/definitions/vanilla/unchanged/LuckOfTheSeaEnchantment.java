package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class LuckOfTheSeaEnchantment extends UnchangedEnchantment {


    public LuckOfTheSeaEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases chance of treasure by ").color(NamedTextColor.GRAY)
                .append(Component.text((2*getLevel()) + "%").color(NamedTextColor.GREEN));
    }

    @Override
    public int getSkillRequirement() {
        return 50;
    }
}
