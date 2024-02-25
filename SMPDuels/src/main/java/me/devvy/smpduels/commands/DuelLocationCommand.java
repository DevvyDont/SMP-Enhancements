package me.devvy.smpduels.commands;

import me.devvy.smpduels.SMPDuels;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DuelLocationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!commandSender.isOp())
            return true;

        // Ignore console
        if (!(commandSender instanceof Player))
            return true;

        Player sender = (Player) commandSender;

        SMPDuels.getInstance().getConfig().set(SMPDuels.CONFIG_DUEL_ARENA_LOCATION, sender.getLocation());
        SMPDuels.getInstance().saveConfig();

        sender.sendMessage(
                Component.text("Set duel arena location!", TextColor.color(0, 200, 0))
        );
        sender.playSound(sender.getEyeLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1f, 1f);
        return true;

    }
}
