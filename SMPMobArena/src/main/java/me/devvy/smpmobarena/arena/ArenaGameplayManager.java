package me.devvy.smpmobarena.arena;

import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.events.PlayerAttemptJoinArenaActivePlayersEvent;
import me.devvy.smpmobarena.events.PlayerAttemptLeaveArenaActivePlayersEvent;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ArenaGameplayManager implements Listener {

    public enum ArenaState {
        WAITING_FOR_PLAYERS,  // Nobody is playing, we are waiting
        STARTING,             // Counting down to start the game
        WAVE_IN_PROGRESS,     // Mobs are waiting to be killed by players
        BUY_IN_PROGRESS,      // Intermission between waves, allow players to upgrade
        ENDING,               // Either game over or win, countdown to send players back to lobby
        FAILED,               // End condition, players failed, set to this to break out of the loop of wave -> buy -> wave
        SUCCESS;              // End condition, players won, set to this to break out of the loop of wave -> buy -> wave

        /**
         * Given a current arena state, figure out which state should be next
         *
         * @param prev Current state
         * @return Next state
         */
        public static ArenaState nextState(ArenaState prev) {
            return switch (prev) {
                case WAITING_FOR_PLAYERS -> STARTING;                // After we wait for players, we go to start countdown
                case STARTING, WAVE_IN_PROGRESS -> BUY_IN_PROGRESS;  // After start countdown or a wave, go to buy phase for next wave
                case BUY_IN_PROGRESS -> WAVE_IN_PROGRESS;            // After a buy phase, go to next wave
                case ENDING -> WAITING_FOR_PLAYERS;                  // After ending countdown, go back to waiting for players
                case FAILED, SUCCESS -> ENDING;                      // After a win or fail, go to ending countdown
            };
        }

        public static boolean allowJoins(ArenaState state) {
            return switch (state) {
                case WAITING_FOR_PLAYERS, STARTING -> true;
                case WAVE_IN_PROGRESS, ENDING, BUY_IN_PROGRESS, FAILED, SUCCESS -> false;
            };
        }
    }

    private final float tickSpeed = 5.0f;  // Debug value for speeding up ticks, set to 1.0f for normal speed

    private ArenaState state;
    private MobArena arena;

    private ArenaWaveManager waveManager;

    private Set<ArenaPlayer> players = new HashSet<>();

    private ArenaTimerTask currentTask;

    private long startTimeMillis = 0;

    public ArenaGameplayManager(MobArena arena) {
        this.arena = arena;
        this.state = ArenaState.WAITING_FOR_PLAYERS;
        this.waveManager = new ArenaWaveManager(arena);

        SMPMobArena.getInstance().getServer().getPluginManager().registerEvents(this, SMPMobArena.getInstance());
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;

        // Edge case, are there no players? If so, next state should always be waiting for players
        if (players.isEmpty())
            this.state = ArenaState.WAITING_FOR_PLAYERS;

        // Since we changed state we need to handle it
        handleStateChange();
    }

    public MobArena getArena() {
        return arena;
    }

    public ArenaWaveManager getWaveManager() {
        return waveManager;
    }

    public Collection<ArenaPlayer> getPlayers() {
        return players;
    }

    public long getElapsedTimeMillis() {
        // If the game hasn't started yet, return 0
        if (startTimeMillis == 0)
            return 0;

        return System.currentTimeMillis() - startTimeMillis;
    }

    public void addPlayer(ArenaPlayer player) {
        players.add(player);
    }

    public void removePlayer(ArenaPlayer player) {
        boolean removed = players.remove(player);
        if (removed)
            player.setSpectatingLoadout();
    }

    public boolean isActivePlayer(ArenaPlayer player) {
        return players.contains(player);
    }

    /**
     * Call to start a game with whatever players are active
     */
    public void startGame() {

        // If the state is not waiting for players, we can't start the game
        if (state != ArenaState.WAITING_FOR_PLAYERS)
            return;

        // If there are no players, we can't start the game
        if (players.isEmpty())
            return;

        // Good to start
        startTimeMillis = System.currentTimeMillis();
        nextState();
    }

    public void cancelCurrentTask() {
        if (currentTask != null)
            currentTask.cancel();
    }

    /**
     * Call to move to the next state
     * Usually called from a Bukkit Runnable
     */
    public void nextState() {
        // Progress to the next state
        setState(ArenaState.nextState(this.state));
    }

    /**
     * Internal method only called from setState() to handle state changes
     */
    private void handleStateChange() {

        cancelCurrentTask();

        switch (state) {
            case WAITING_FOR_PLAYERS -> {
                // Code for sending players back to lobby and performing cleanup from a previous game

                endGame();  // Simply end the game and wait for players to start up again

            } case STARTING -> {
                // Code for starting the game starting countdown (30s to join before actual start)

                currentTask = new PregameCountdownTask(arena);
                currentTask.runTaskTimer(SMPMobArena.getInstance(), 0, (long)(PregameCountdownTask.PERIOD / tickSpeed));

            } case WAVE_IN_PROGRESS -> {
                // Code for starting a new wave

                currentTask = new WaveCountdownTask(arena);
                currentTask.runTaskTimer(SMPMobArena.getInstance(), 0, (long)(WaveCountdownTask.PERIOD / tickSpeed));

            } case BUY_IN_PROGRESS -> {
                // Code for starting a buy phase

                // For all players, set them to playing, if they are dead, teleport them to the arena origin
                for (ArenaPlayer player : players) {

                    if (player.getState() == ArenaPlayer.ArenaPlayerState.DEAD) {
                        player.getPlayer().spigot().respawn();
                        player.getPlayer().teleport(arena.getArenaOrigin());
                    }

                    player.setState(ArenaPlayer.ArenaPlayerState.PLAYING);
                    player.getPlayer().setInvulnerable(false);
                }

                currentTask = new BuyPhaseCountdownTask(arena);
                currentTask.runTaskTimer(SMPMobArena.getInstance(), 0, (long)(BuyPhaseCountdownTask.PERIOD / tickSpeed));

            } case ENDING -> {
                // Code for when the game is over (10s countdown to waiting phase)

                currentTask = new GameEndingCountdownTask(arena);
                currentTask.runTaskTimer(SMPMobArena.getInstance(), 0, (long)(GameEndingCountdownTask.PERIOD / tickSpeed));

            } case FAILED -> {
                // Code for when the players have lost


                arena.getPlayerManager().broadcast(Component.text("The players have failed!").color(NamedTextColor.RED));
                nextState();  // Go to ending state

            } case SUCCESS -> {
                // Code for when the players have won


                arena.getPlayerManager().broadcast(Component.text("The players have won!").color(NamedTextColor.GREEN));
                nextState();  // Go to ending state

            }
        }
    }

    /**
     * Called when a player dies in the arena during a wave
     * we can guarantee that this player is in fact playing
     *
     * @param ap
     */
    private void handlePlayerDeath(ArenaPlayer ap) {

        // Broadcast the death
        arena.getPlayerManager().broadcast(ap.getPlayer().displayName().append(Component.text(" has died!").color(NamedTextColor.RED)));

        // Set their state
        ap.setState(ArenaPlayer.ArenaPlayerState.DEAD);

        // Last check, is everyone dead?
        for (ArenaPlayer player : players) {
            if (player.getState() != ArenaPlayer.ArenaPlayerState.DEAD)
                return;
        }

        // If we get here, everyone is dead
        arena.getPlayerManager().broadcast(Component.text("Everyone has died!").color(NamedTextColor.RED));
        setState(ArenaState.FAILED);
    }

    /**
     * Call to end the game by force, or when the players have lost or won after endgame countdown
     * This should also support being called if a game has not run yet during uptime
     */
    public void endGame() {

        startTimeMillis = 0;

        // Teleport all active players to their safe location and clear their progress (todo)
        for (ArenaPlayer player : new ArrayList<>(players)) {
            player.getPlayer().teleport(player.getSafeLocation());
            removePlayer(player);
        }

        getArena().getScoreboard().reconstructComponents();

    }

    /**
     * Call when the plugin is shutting down
     */
    public void cleanup() {
        endGame();
        HandlerList.unregisterAll(this);
    }

    // EVENTS TO MANAGE PLAYERS LEAVING AND JOINING ACTIVE PLAYERS TO START

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAttemptJoinActivePlayers(PlayerAttemptJoinArenaActivePlayersEvent event) {

        // Unless we are allowing joins, we can't join
        if (!ArenaState.allowJoins(state)) {
            event.setCancelled(true);
            return;
        }

        // If this player is already in the arena, we can't join again
        if (players.contains(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        // If we made it this far we should allow the join (no other plugin canceled, and we are waiting for players)
        addPlayer(event.getPlayer());
        arena.getPlayerManager().broadcast(event.getPlayer().getPlayer().displayName().append(Component.text(" has joined the arena!", NamedTextColor.GREEN)));
        event.getPlayer().getPlayer().playSound(event.getPlayer().getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
        event.getPlayer().setSafeLocation(event.getPlayer().getPlayer().getLocation());

        // Attempt to start countdown, if we are already counting down this will do nothing
        startGame();

        // Loop through all players in arena, if they are actively playing set their scoreboard state to playing
        for (ArenaPlayer player : getArena().getPlayerManager().getAllPlayersInArena()) {
            if (isActivePlayer(player))
                getArena().getScoreboard().setPlayer(player.getPlayer());
            else
                getArena().getScoreboard().setSpectator(player.getPlayer());
        }
        getArena().getScoreboard().reconstructComponents();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAttemptLeaveActivePlayers(PlayerAttemptLeaveArenaActivePlayersEvent event) {

        // First check if this player is even in the arena
        if (!players.contains(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        // Unless another plugin prevents it, there should be nothing stopping us from leaving
        removePlayer(event.getPlayer());
        arena.getPlayerManager().broadcast(event.getPlayer().getPlayer().displayName().append(Component.text(" has left the arena!", NamedTextColor.RED)));

        // If no players are here, force the arena to go back to waiting for players
        if (players.isEmpty())
            nextState();

        // Loop through all players in arena, if they are actively playing set their scoreboard state to playing
        for (ArenaPlayer player : getArena().getPlayerManager().getAllPlayersInArena()) {
            if (isActivePlayer(player))
                getArena().getScoreboard().setPlayer(player.getPlayer());
            else
                getArena().getScoreboard().setSpectator(player.getPlayer());
        }
        getArena().getScoreboard().reconstructComponents();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {

        // If this player is not in the arena, we don't care
        ArenaPlayer ap = arena.getPlayerManager().getArenaPlayer(event.getPlayer().getUniqueId());
        if (ap == null)
            return;

        // If this player is not an active player we don't care
        if (!isActivePlayer(ap))
            return;

        // If this player is not in the PLAYING state, we don't care
        if (ap.getState() != ArenaPlayer.ArenaPlayerState.PLAYING)
            return;

        // This player has died mid round, we need to handle this
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.deathMessage(Component.empty());
        handlePlayerDeath(ap);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        // If this player is not in the arena, we don't care
        ArenaPlayer ap = arena.getPlayerManager().getArenaPlayer(event.getPlayer().getUniqueId());
        if (ap == null)
            return;

        // Let them respawn safely
        event.setRespawnLocation(ap.getSafeLocation());
        event.getPlayer().setInvulnerable(true);
    }


}
