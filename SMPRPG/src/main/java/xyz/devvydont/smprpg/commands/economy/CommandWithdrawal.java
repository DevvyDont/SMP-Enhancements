package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.economy.MenuWithdraw;

public final class CommandWithdrawal extends PlayerCommandBase {
    public CommandWithdrawal(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        new MenuWithdraw(SMPRPG.getInstance(), player).openMenu();
    }
}
