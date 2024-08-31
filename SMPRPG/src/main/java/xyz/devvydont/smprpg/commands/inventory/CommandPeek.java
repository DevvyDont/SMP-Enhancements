package xyz.devvydont.smprpg.commands.inventory;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.InterfaceInventoryPeek;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CommandPeek extends CommandBase {

    public CommandPeek(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        Player player;
        if (!(commandSourceStack.getSender() instanceof Player sender))
            return;

        if (args.length == 0) {
            sender.sendMessage(ComponentUtils.getErrorMessage("Please specify a player you want to peek!"));
            return;
        }

        String name = args[0];
        player = Bukkit.getPlayer(name);
        if (player == null) {
            sender.sendMessage(ComponentUtils.getErrorMessage("Could not find player with the name " + name + ". Please try again"));
            return;
        }

        sender.sendMessage(ComponentUtils.getSuccessMessage("You are now peeking into " + player.getName() + "'s inventory!"));
        var gui = new InterfaceInventoryPeek(SMPRPG.getInstance(), sender);
        gui.open();
        gui.showPlayer(player);
    }

}
