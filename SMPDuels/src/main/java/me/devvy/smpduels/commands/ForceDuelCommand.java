package me.devvy.smpduels.commands;

import me.devvy.smpduels.SMPDuels;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ForceDuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Called by admin to force two players to duel
        if (!commandSender.isOp())
            return true;

        // if arg 1 is stop then stop the duel
        if (strings.length >= 1 && strings[0].equalsIgnoreCase("stop")) {
            try {
                SMPDuels.getInstance().getDuelManager().stopCurrentDuel();
            } catch (IllegalStateException e) {
                commandSender.sendMessage(e.getMessage());
            }
            return true;
        }

        // Has to be two args
        if (strings.length < 2) {
            commandSender.sendMessage("Must provide two args!");
            return true;
        }

        // Two players must be online
        String p1Name = strings[0];
        String p2Name = strings[1];

        if (p1Name.equalsIgnoreCase(p2Name)) {
            commandSender.sendMessage("Cannot be same player!");
            return true;
        }

        Player p1 = Bukkit.getPlayer(p1Name);
        Player p2 = Bukkit.getPlayer(p2Name);

        if (p1 == null) {
            commandSender.sendMessage("First player is not online!");
            return true;
        }

        if (p2 == null) {
            commandSender.sendMessage("Second player is not online!");
            return true;
        }

        try {
            SMPDuels.getInstance().getDuelManager().createDuel(Arrays.asList(p1, p2));
        } catch (IllegalStateException e) {
            commandSender.sendMessage(e.getMessage());
        }

        p1.setSaturation(10);
        p2.setSaturation(10);

        return true;

    }
}
