package me.devvy.smpduels.commands;

import me.devvy.smpduels.SMPDuels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player player))
            return true;

        if (strings.length == 0) {
            commandSender.sendMessage(Component.text("Must provide a player to duel!", NamedTextColor.RED));
            return true;
        }

        Player personToDuel = Bukkit.getPlayer(strings[0]);
        if (personToDuel == null) {
            player.sendMessage(Component.text("Player is not online!", NamedTextColor.RED));
            return true;
        }

        // We cannot duel ourselves
        if (personToDuel.equals(player)) {
            player.sendMessage(Component.text("You cannot duel yourself!", NamedTextColor.RED));
            return true;
        }

        try {
            SMPDuels.getInstance().getDuelManager().processDuelRequest(player, personToDuel);
        } catch (IllegalStateException e) {
            player.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        }
        return true;
    }

}
