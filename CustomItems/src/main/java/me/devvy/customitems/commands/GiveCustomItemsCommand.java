package me.devvy.customitems.commands;

import me.devvy.customitems.CustomItems;
import me.devvy.customitems.blueprints.CustomItemBlueprint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveCustomItemsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            System.out.println("Only players can use this!");
            return true;
        }

        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Please provide an argument! /givetreasure <item> [amount]");
            return true;
        }

        String name = strings[0].toLowerCase();
        int amount;

        try {
            amount = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "Please provide a number!");
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            amount = 1;
        }

        CustomItemBlueprint customItemType;

        try {
            customItemType = CustomItems.getInstance().getCustomItemManager().getBlueprint(name);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(ChatColor.RED + "That is not a valid custom item!");
            return true;
        }

        Player player = (Player) commandSender;
        ItemStack newItem = customItemType.get();
        newItem.setAmount(amount);
        player.getInventory().addItem(newItem);
        commandSender.sendMessage(ChatColor.GREEN + "Gave you " + amount + " " + customItemType.key() + "!");
        return true;

    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        ArrayList<String> options = new ArrayList<>();

        if (strings.length != 1)
            return options;

        String soFar = strings[0];

        for (CustomItemBlueprint customItemType : CustomItems.getInstance().getCustomItemManager().getBlueprints()) {
            if (customItemType.key().contains(soFar.toLowerCase()))
                options.add(customItemType.key());
        }
        return options;


    }


}
