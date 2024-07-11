package xyz.devvydont.smprpg.commands.player;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.InterfaceStats;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;

import java.util.Collection;

public class CommandStatistics extends CommandBase {

    public CommandStatistics(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatUtil.getErrorMessage("You cannot do that as the console!"));
            return;
        }

        if (strings.length >= 1) {
            player = Bukkit.getPlayer(strings[0]);
            if (player == null) {
                commandSender.sendMessage(ChatUtil.getErrorMessage(String.format("Could not find online player with name %s!", strings[0])));
                return;
            }
        }

        InterfaceStats gui = new InterfaceStats(SMPRPG.getInstance(), (Player) commandSender, player);
        gui.open();
        commandSender.sendMessage(ChatUtil.getSuccessMessage("Opened statistics!"));
    }
}
