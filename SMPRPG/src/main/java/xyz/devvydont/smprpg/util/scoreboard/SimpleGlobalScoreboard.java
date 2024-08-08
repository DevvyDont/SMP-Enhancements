package xyz.devvydont.smprpg.util.scoreboard;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleGlobalScoreboard {

    private Scoreboard scoreboard;
    Objective display;
    private List<Player> players = new ArrayList<>();

    public SimpleGlobalScoreboard(Scoreboard scoreboard, Component title) {
        this.scoreboard = scoreboard;
        display = scoreboard.registerNewObjective("boss", Criteria.DUMMY, title);
        display.setAutoUpdateDisplay(true);
        display.setDisplaySlot(DisplaySlot.SIDEBAR);
        display.numberFormat(NumberFormat.blank());
    }

    public void display(Player player) {
        player.setScoreboard(scoreboard);
        display.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (!players.contains(player))
            players.add(player);
    }

    public void hide(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        players.remove(player);
    }

    public void cleanup() {
        for (Player player : players.stream().toList())
            hide(player);
        players.clear();
    }

    /**
     * The scoreboard will display from 15,14,13, etc to 1.
     *
     * @param realIndex
     * @return
     */
    private int convertRealIndexToScoreIndex(int realIndex) {
        return 15-realIndex;
    }

    private Team getLine(int index) {
        String id = '§' + Integer.toHexString(index);
        Team team = scoreboard.getTeam(id);
        if (team == null) {
            team = scoreboard.registerNewTeam(id);
            team.addEntry(id);
        }
        return team;
    }

    public void setLines(Component...lines) {
        setLines(Arrays.stream(lines).toList());
    }

    public void setLines(List<Component> lines) {
        for (int i = 0; i < 16; i++) {

            Team line = getLine(i);
            Score score = display.getScore(line.getName());

            if (i >= lines.size()) {
                score.resetScore();
                continue;
            }

            score.setScore(convertRealIndexToScoreIndex(i));
            line.prefix(lines.get(i));
        }
    }

    public boolean showing(Player player) {
        return players.contains(player);
    }
}
