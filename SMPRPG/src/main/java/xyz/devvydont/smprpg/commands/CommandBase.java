package xyz.devvydont.smprpg.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CommandBase implements BasicCommand {

    private String name;

    public CommandBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return "No description was provided for this command";
    }

    /**
     * A list of aliases that will also execute this command
     *
     * @return A collection of alternative ways to execute this command when using /
     */
    public Collection<String> getAliases() {
        return List.of();
    }

    public Collection<String> generateArgumentCollection(String input, Collection<String> choices) {
        List<String> arguments = new ArrayList<>();
        for (String choice : choices)
            if (choice.toLowerCase().contains(input.toLowerCase()))
                arguments.add(choice);
        return arguments;
    }

    /**
     * Utility method to match an argument with a pool of choices
     *
     * @param input The argument that is being typed so far
     * @param choices A pool of choices for the argument
     * @return A collection of strings representing auto completed versions of the input
     */
    protected Collection<String> generateArgumentCollection(String input, String...choices) {
        return generateArgumentCollection(input, List.of(choices));
    }

    /**
     * Generates a collection of strings to be used for auto completing player names as an argument in a command.
     * Typically, this is default behavior if we don't know what to do with an argument.
     *
     * @param input The input string that is typed so far for an argument to match for player names
     * @return A collection a strings representing player names of players who are online
     */
    protected Collection<String> determinePlayerSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (onlinePlayer.getName().toLowerCase().contains(input.toLowerCase()))
                suggestions.add(onlinePlayer.getName());
        return suggestions;
    }

    /**
     * Determine a suggestion given an input so far and the argument of the index.
     *
     * @param argumentIndex The index of the argument the player is currently typing
     * @param input The input the player has so far for this argument
     * @return A collection of strings to suggest to the player as arguments for auto completion
     */
    protected Collection<String> determineSuggestions(int argumentIndex, String input){
        return determinePlayerSuggestions(input);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {

        // If we don't have args default to player fill-ins.
        if (args.length == 0)
            return determinePlayerSuggestions("");

        // Decide what to do with this argument
        return determineSuggestions(args.length-1, args[args.length-1]);
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return BasicCommand.super.canUse(sender) || sender.isOp();
    }
}
