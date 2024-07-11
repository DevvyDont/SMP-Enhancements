package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class UnbreakingEnchantment extends UnchangedEnchantment {

    public UnbreakingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDescription() {
        int chance = (int) ((1-(1.0/(getLevel()+1)))*100);
        return Component.text("Durability is ignored ").color(NamedTextColor.GRAY)
                .append(Component.text(chance + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" of the time when used").color(NamedTextColor.GRAY));
    }
}
