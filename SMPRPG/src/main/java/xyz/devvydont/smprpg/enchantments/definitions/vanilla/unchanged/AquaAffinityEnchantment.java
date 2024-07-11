package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class AquaAffinityEnchantment extends UnchangedEnchantment {

    public AquaAffinityEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Improves ").color(NamedTextColor.GRAY)
                .append(Component.text("underwater").color(NamedTextColor.AQUA))
                .append(Component.text(" mining speed").color(NamedTextColor.GRAY));
    }
}
