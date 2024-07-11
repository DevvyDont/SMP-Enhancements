package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.InterfaceWithdrawal;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;

public class CommandWithdrawal extends CommandBase {


    public CommandWithdrawal(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatUtil.getErrorMessage("You are the console! You cannot do this!"));
            return;
        }

        Player player = (Player) commandSender;

        InterfaceWithdrawal gui = new InterfaceWithdrawal(SMPRPG.getInstance(), player);
        gui.open();

        player.sendMessage(ChatUtil.getSuccessMessage("Success!"));
    }
}
