package xyz.devvydont.smprpg.commands.inventory;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.gui.player.MenuInventoryPeek;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CommandPeek extends PlayerCommandBase {
    public CommandPeek(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            player.sendMessage(ComponentUtils.error("Please specify a player you want to peek!"));
            return;
        }

        var targetName = args[0];
        var targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(ComponentUtils.error("Could not find player with the name " + targetName + ". Please try again"));
            return;
        }

        new MenuInventoryPeek(player, targetPlayer).openMenu();
    }
}
