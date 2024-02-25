package me.devvy.dynamicdifficulty.commands;

import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DifficultyCommand implements CommandExecutor, TabCompleter {

    public static final String[] NORMAL_SUBCOMMANDS = {"menu", "easy", "normal", "hard"};

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        // No args? Open the gui to select a difficulty
        if (strings.length == 0 || strings[0].equalsIgnoreCase("menu")) {
            DynamicDifficulty.getInstance().getDifficultyPreferenceGUI().open(sender);
            return true;
        }

        // 1 arg?
        if (strings.length == 1) {
            // Attempt to find parse difficulty

            Difficulty difficulty;
            try {
                difficulty = Difficulty.valueOf(strings[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Invalid difficulty! Valid difficulties are: " + ChatColor.AQUA + "easy, normal, hard");
                return true;
            }

            // Edge case don't allow peaceful for now
            if (difficulty.equals(Difficulty.PEACEFUL)) {
                sender.sendMessage(ChatColor.RED + "You cannot set the difficulty to peaceful!");
                return true;
            }

            // Update their preferred difficulty
            DynamicDifficulty.getInstance().getDifficultyPreferenceStorageManager().setPreferredDifficulty(sender, difficulty);
            sender.sendMessage(ChatColor.GRAY + "Updated your preferred difficulty to " + DynamicDifficulty.getInstance().difficultyToColor(difficulty) + difficulty + ChatColor.GREEN + "!");
            DynamicDifficulty.getInstance().checkForNewDifficulty();
            return true;
        }

        // Dont allow more than 1 arg
        sender.sendMessage(ChatColor.RED + "Too many arguments!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        List<String> validArgs = new ArrayList<>();

        if (strings.length == 0)
            return null;

        if (strings.length == 1) {

            String soFar = strings[0];
            List<String> availableCommands = new ArrayList<>(List.of(NORMAL_SUBCOMMANDS));

            for (String availCmd : availableCommands)
                if (availCmd.toLowerCase().contains(soFar.toLowerCase()))
                    validArgs.add(availCmd);

            return validArgs;

        }

        return null;

    }
}
