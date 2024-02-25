package me.devvy.stimmys.commands;

import me.devvy.stimmys.util.ConfigManager;
import me.devvy.stimmys.Stimmys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandStimmy implements CommandExecutor, TabCompleter {

    public static final String[] OP_SUBCOMMANDS = {"add", "set", "dump"};
    public static final String[] NORMAL_SUBCOMMANDS = {"shop", "redeem"};

    public static final Component NO_ARG_MESSAGE = Component.text("No arguments! /stimmy < redeem | shop >", NamedTextColor.RED);

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        // No args? Just say usage
        if (strings.length == 0) {
            sender.sendMessage(NO_ARG_MESSAGE);
            return true;
        }

        // 1+ arg?
        try {

            String cmd = strings[0].toLowerCase();

            // Adding? (OP command)
            if (sender.isOp() && cmd.equals("add"))
                return handleAddPoints(sender, Arrays.copyOfRange(strings, 1, strings.length));

            // Setting? (OP command)
            if (sender.isOp() && cmd.equals("set"))
                return handleSetPoints(sender, Arrays.copyOfRange(strings, 1, strings.length));

            // Dumping? (OP command)
            if (sender.isOp() && cmd.equals("dump"))
                return handleDump(sender);

            // Shop?
            if (cmd.equals("shop"))
                return handleOpenShop(sender);

            // Redeem?
            if (cmd.equals("redeem"))
                return handleRedeem(sender);

        } catch (IllegalStateException | NumberFormatException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
            return true;
        }


        return false;
    }

    private boolean handleAddPoints(Player sender, String[] strings) {
        if (strings.length < 2)
            throw new IllegalStateException("Must provide integer amount followed by players!");

        int amount = Integer.parseInt(strings[0]);

        for (OfflinePlayer p : getPlayersFromArguments(Arrays.copyOfRange(strings, 1, strings.length)))
            if (p != null)
                Stimmys.getInstance().getStimmyCurrencyManager().addStimmyPoints(p, amount);

        sender.sendMessage(Component.text("Added " + strings[0] + " stimmies to " + (strings.length-1) + " players!"));
        return true;
    }

    private boolean handleSetPoints(Player sender, String[] strings) {

        if (strings.length < 2)
            throw new IllegalStateException("Must provide integer amount followed by players!");

        int amount = Integer.parseInt(strings[0]);

        for (OfflinePlayer p : getPlayersFromArguments(Arrays.copyOfRange(strings, 1, strings.length)))
            if (p != null)
                Stimmys.getInstance().getStimmyCurrencyManager().setStimmyPoints(p, amount);

        sender.sendMessage(Component.text("Set " + strings[0] + " stimmies on " + (strings.length-1) + " players!"));
        return true;
    }

    private boolean handleDump(Player sender) {
        ConfigManager.dumpStimmyConfig(sender);
        return true;
    }

    private boolean handleOpenShop(Player sender) {
        Stimmys.getInstance().getStimmyShopGUI().open(sender);
        return true;
    }

    private boolean handleRedeem(Player sender) {
        Stimmys.getInstance().getStimmyCurrencyManager().redeemStimmyPoints(sender);
        return true;
    }

    private Collection<OfflinePlayer> getPlayersFromArguments(String[] names) {

        List<OfflinePlayer> players = new ArrayList<>();

        for (String name : names) {
            OfflinePlayer offP = Bukkit.getOfflinePlayer(name);
            if (!offP.hasPlayedBefore())
                throw new IllegalStateException("Player with name " + name + " was not detected as a player of this server. Nothing has been changed.");

            players.add(offP);
        }

        return players;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        List<String> validArgs = new ArrayList<>();

        if (strings.length == 0)
            return null;

        if (strings.length == 1) {

            String soFar = strings[0];
            List<String> availableCommands = new ArrayList<>(List.of(NORMAL_SUBCOMMANDS));
            if (commandSender.isOp())
                availableCommands.addAll(List.of(OP_SUBCOMMANDS));

            for (String availCmd : availableCommands)
                if (availCmd.toLowerCase().contains(soFar.toLowerCase()))
                    validArgs.add(availCmd);

            return validArgs;

        }

        // Usually always a number
        if (strings.length == 2)
            return validArgs;

        // Usually a player
        return null;
    }
}
