package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.economy.MenuDeposit;

import java.util.Collection;
import java.util.List;


public final class CommandDeposit extends PlayerCommandBase {
    public CommandDeposit(String name) {
        super(name);
    }

    @Override
    public Collection<String> getAliases() {
        return List.of("sell", "quicksell");
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        new MenuDeposit(SMPRPG.getInstance(), player).openMenu();
    }
}
