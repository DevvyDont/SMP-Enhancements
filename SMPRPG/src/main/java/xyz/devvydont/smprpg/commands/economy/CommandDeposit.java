package xyz.devvydont.smprpg.commands.economy;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.CommandBase;
import xyz.devvydont.smprpg.gui.InterfaceDeposit;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;


public class CommandDeposit extends CommandBase {


    public CommandDeposit(String name) {
        super(name);
    }

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] strings) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ComponentUtils.error("You are the console! You cannot do this!"));
            return;
        }

        Player player = (Player) commandSender;

        InterfaceDeposit gui = new InterfaceDeposit(SMPRPG.getInstance(), player);
        gui.open();

        player.sendMessage(ComponentUtils.success("Success!"));
    }

    @Override
    public Collection<String> getAliases() {
        return List.of(
                "sell",
                "quicksell"
        );
    }
}
