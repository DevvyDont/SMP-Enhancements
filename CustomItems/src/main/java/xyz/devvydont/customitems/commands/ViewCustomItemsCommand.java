package me.devvy.customitems.commands;

import me.devvy.customitems.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewCustomItemsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        // Open the custom item viewer
         CustomItems.getInstance().getGuiManager().getRatesMainMenuGUI().open(sender);

        return true;
    }
}
