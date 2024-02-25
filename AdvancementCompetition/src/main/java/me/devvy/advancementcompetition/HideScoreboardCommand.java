package me.devvy.advancementcompetition;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class HideScoreboardCommand implements CommandExecutor {

    public static final NamespacedKey HIDE_SCOREBOARD_SETTING_KEY = new NamespacedKey(AdvancementCompetition.getInstance(), "hide-scoreboard");


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player) commandSender;

        // Get their current setting, then flip it
        boolean shouldHide = player.getPersistentDataContainer().getOrDefault(HIDE_SCOREBOARD_SETTING_KEY, PersistentDataType.BOOLEAN, false);
        player.getPersistentDataContainer().set(HIDE_SCOREBOARD_SETTING_KEY, PersistentDataType.BOOLEAN, !shouldHide);
        shouldHide = !shouldHide;

        if (shouldHide)
            player.sendMessage(AdvancementCompetition.prefix.append(Component.text("Hiding the scoreboard!", TextColor.color(0, 200, 0))));
        else
            player.sendMessage(AdvancementCompetition.prefix.append(Component.text("Showing the scoreboard!", TextColor.color(0, 200, 0))));

        AdvancementCompetition.getInstance().updatePlayerScoreboard(player);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

        return true;

    }
}
