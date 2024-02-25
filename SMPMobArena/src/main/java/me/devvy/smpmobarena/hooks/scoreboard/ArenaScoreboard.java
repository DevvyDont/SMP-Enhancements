package me.devvy.smpmobarena.hooks.scoreboard;

import me.devvy.betterscoreboards.scoreboards.*;
import me.devvy.smpmobarena.arena.MobArena;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.List;

public class ArenaScoreboard extends GlobalTeamBetterScoreboard {

    private final MobArena arena;
    private final BetterScoreboardTeam playersTeam;
    private final BetterScoreboardTeam spectatorsTeam;

    public ArenaScoreboard(MobArena arena) {

        this.arena = arena;

        // Set default settings
        this.setRefreshRate(5);  // Four times a second
        this.setTitle(Component.text("Mob Arena", NamedTextColor.GOLD, TextDecoration.BOLD));


        this.playersTeam = this.createTeam("players-team");
        this.playersTeam.setColor(NamedTextColor.AQUA);
        this.playersTeam.setFriendlyFire(false);
        this.playersTeam.setCanSeeFriendlyInvisibles(true);

        this.spectatorsTeam = this.createTeam("spectators-team");
        this.spectatorsTeam.setColor(NamedTextColor.GRAY);
        this.spectatorsTeam.setFriendlyFire(false);
        this.spectatorsTeam.setCanSeeFriendlyInvisibles(true);
        this.spectatorsTeam.setPrefix(Component.text(("[SPEC] "), NamedTextColor.DARK_GRAY));
    }

    public MobArena getArena() {
        return arena;
    }

    public BetterScoreboardTeam getPlayersTeam() {
        return playersTeam;
    }

    public BetterScoreboardTeam getSpectatorsTeam() {
        return spectatorsTeam;
    }

    /**
     * Syncs scoreboard instance to the player, and adds them to the players team
     *
     * @param player
     */
    public void addToScoreboard(Player player) {
        this.addPlayer(player);
        setSpectator(player);
    }

    public void removeFromScoreboard(Player player) {
        this.removePlayer(player);
    }

    public void setSpectator(Player player) {
        this.playersTeam.removeMember(player);
        this.spectatorsTeam.addMember(player);
    }

    public void setPlayer(Player player) {
        this.spectatorsTeam.removeMember(player);
        this.playersTeam.addMember(player);
    }

    public void reconstructComponents() {

        List< ScoreboardComponent> components = this.getComponents();

        components.clear();

        components.add(new EmptyScoreboardComponent());
        components.add(new ArenaWaveComponent(arena));
        components.add(new EnemiesRemainingScoreboardComponent(arena));
        components.add(new EmptyScoreboardComponent());

        components.add(new StaticScoreboardComponent(Component.text("Players:", NamedTextColor.GRAY)));
        for (ArenaPlayer player : arena.getGameplayManager().getPlayers())
            components.add(new ArenaPlayerScoreboardComponent(player));

        components.add(new EmptyScoreboardComponent());
        components.add(new TimeScoreboardComponent(arena));
    }
}
