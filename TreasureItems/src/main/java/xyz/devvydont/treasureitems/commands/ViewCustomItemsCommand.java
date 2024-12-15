package xyz.devvydont.treasureitems.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.devvydont.treasureitems.TreasureItems;

public class ViewCustomItemsCommand implements BaseCommand {

    @Override
    public String getName() {
        return "treasure";
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {

        // Ignore console
        if (!(commandSourceStack.getSender() instanceof Player player))
            return;

        // Open the custom item viewer
        TreasureItems.getInstance().getGuiManager().getRatesMainMenuGUI().open(player);
    }

}
