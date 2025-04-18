package xyz.devvydont.smprpg.commands.enchantments;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.enchantments.EnchantmentMenu;

import java.util.Collection;
import java.util.List;

public class CommandEnchantments extends PlayerCommandBase {

    public CommandEnchantments(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        new EnchantmentMenu(player).openMenu();
    }

    @Override
    public Collection<String> getAliases() {
        return List.of(
                "enchants",
                "listenchants",
                "listenchantments"
        );
    }
}
