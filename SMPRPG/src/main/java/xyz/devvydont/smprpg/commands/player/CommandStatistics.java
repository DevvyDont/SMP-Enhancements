package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.player.InterfaceStats;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class CommandStatistics extends PlayerCommandBase {
    public CommandStatistics(String name) {
        super(name);
    }

    @Override
    public Collection<String> getAliases() {
        return List.of("stats");
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        var targetEntity = args.length == 0 ? player : Bukkit.getPlayer(args[0]);
        if (targetEntity == null) {
            player.sendMessage(ComponentUtils.error(String.format("Could not find online player with name %s!", args[0])));
            return;
        }

        new InterfaceStats(SMPRPG.getInstance(), player, targetEntity).openMenu();
    }
}
