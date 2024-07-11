package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class MultishotEnchantment extends UnchangedEnchantment {


    public MultishotEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        return Component.text("Fires ").color(NamedTextColor.GRAY)
                .append(Component.text("multiple arrows").color(NamedTextColor.GOLD))
                .append(Component.text(" when shot").color(NamedTextColor.GRAY));
    }
}
