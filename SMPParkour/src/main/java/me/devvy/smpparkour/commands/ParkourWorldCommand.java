package me.devvy.smpparkour.commands;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.player.ParkourPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ParkourWorldCommand implements CommandExecutor, TabCompleter {

    public static final String[] OP_SUBCOMMANDS = {"visualizecheckpoints"};
    public static final String[] NORMAL_SUBCOMMANDS = {};

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        // If the player is OP then they can use the OP subcommands, check for that
        if (sender.isOp()) {

            // If there are arguments, check if the first one matches a subcommand
            if (strings.length > 0) {

                String subcommand = strings[0].toLowerCase();

                // If the first argument matches the subcommand, run it
                if (subcommand.equals("visualizecheckpoints")) {
                    SMPParkour.getInstance().getMapManager().toggleCheckpointVisualization();
                    sender.sendMessage(Component.text("Toggled checkpoint visualization!", NamedTextColor.GREEN));
                    return true;

                }
            }

        }

        // If they are at the world, leave otherwise send them there
        ParkourPlayer pp = SMPParkour.getInstance().getPlayerManager().getParkourPlayer(sender);

        try {
            if (pp != null)
                SMPParkour.getInstance().getPlayerManager().warpOutParkourWorld(sender);
            else
                SMPParkour.getInstance().getPlayerManager().warpToParkourWorld(sender);
        } catch (IllegalStateException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        }

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
