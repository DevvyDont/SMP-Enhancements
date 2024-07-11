package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class FrostWalkerEnchantment extends UnchangedEnchantment {

    public FrostWalkerEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Freezes water ").color(NamedTextColor.GRAY)
                .append(Component.text(getLevel()+1).color(NamedTextColor.GREEN))
                .append(Component.text(" blocks away when walked on").color(NamedTextColor.GRAY));
    }

}
