package xyz.devvydont.treasureitems.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GiveCustomItemsCommand implements BaseCommand {

    @Override
    public String getName() {
        return "givetreasure";
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {

        CommandSender commandSender = commandSourceStack.getSender();

        if (!(commandSender instanceof Player)) {
            System.out.println("Only players can use this!");
            return;
        }

        if (!commandSender.isOp()) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return;
        }

        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Please provide an argument! /givetreasure <item> [amount]");
            return;
        }

        String name = args[0].toLowerCase();
        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "Please provide a number!");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            amount = 1;
        }

        CustomItemBlueprint customItemType;

        try {
            customItemType = xyz.devvydont.treasureitems.TreasureItems.getInstance().getCustomItemManager().getBlueprint(name);
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(ChatColor.RED + "That is not a valid custom item!");
            return;
        }

        Player player = (Player) commandSender;
        ItemStack newItem = customItemType.get();
        newItem.setAmount(amount);
        player.getInventory().addItem(newItem);
        commandSender.sendMessage(ChatColor.GREEN + "Gave you " + amount + " " + customItemType.key() + "!");
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        ArrayList<String> options = new ArrayList<>();

        if (args.length != 1)
            return options;

        String soFar = args[0];

        for (CustomItemBlueprint customItemType : xyz.devvydont.treasureitems.TreasureItems.getInstance().getCustomItemManager().getBlueprints()) {
            if (customItemType.key().contains(soFar.toLowerCase()))
                options.add(customItemType.key());
        }
        return options;
    }

}
