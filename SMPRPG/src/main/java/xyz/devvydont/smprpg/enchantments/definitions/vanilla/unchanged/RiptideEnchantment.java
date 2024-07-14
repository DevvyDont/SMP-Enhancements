package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class RiptideEnchantment extends UnchangedEnchantment {

    public static int getBlocksThrown(int level) {
        return (4 * level) + 3;
    }


    public RiptideEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Propels you ").color(NamedTextColor.GRAY)
                .append(Component.text(getBlocksThrown(getLevel())).color(NamedTextColor.GREEN))
                .append(Component.text(" blocks when used").color(NamedTextColor.GRAY));
    }

    @Override
    public int getSkillRequirement() {
        return 58;
    }
}
