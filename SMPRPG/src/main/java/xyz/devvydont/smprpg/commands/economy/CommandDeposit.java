package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.economy.DepositMenu;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

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
        new DepositMenu(SMPRPG.getInstance(), player).openMenu();
    }
}
