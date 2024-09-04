package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.economy.DepositMenu;
import xyz.devvydont.smprpg.gui.economy.WithdrawMenu;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public final class CommandWithdrawal extends PlayerCommandBase {
    public CommandWithdrawal(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        new WithdrawMenu(SMPRPG.getInstance(), player).openMenu();
    }
}
