package me.devvy.smpparkour.util;

import me.devvy.smpparkour.SMPParkour;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class MinecraftScoreboardUtil {

    private Scoreboard scoreboard;
    private Team parkourTeam;

    public MinecraftScoreboardUtil() {
        this.scoreboard = SMPParkour.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        parkourTeam = scoreboard.registerNewTeam("parkour-team");

        parkourTeam.setAllowFriendlyFire(false);
        parkourTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        parkourTeam.color(NamedTextColor.GRAY);
    }

    /**
     * Adds a player to this scoreboard to enable anti collisions and stuff
     *
     * @param player
     */
    public void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
        parkourTeam.addPlayer(player);
    }

    /**
     * Removes a player from this scoreboard
     *
     * @param player
     */
    public void removePlayer(Player player) {
        parkourTeam.removePlayer(player);
    }
}
