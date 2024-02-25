package me.devvy.smpmobarena.player;

import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.arena.MobArena;
import me.devvy.smpmobarena.events.PlayerAttemptLeaveArenaActivePlayersEvent;
import me.devvy.smpmobarena.util.ComponentDefinitions;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all the players in the arena and their states via the ArenaPlayer class
 */
public class ArenaPlayerManager implements Listener {

    private final Map<UUID, ArenaPlayer> arenaPlayers = new HashMap<>();

    private final MobArena arena;

    public ArenaPlayerManager(MobArena arena) {
        SMPMobArena.getInstance().getServer().getPluginManager().registerEvents(this, SMPMobArena.getInstance());
        this.arena = arena;
    }

    public MobArena getArena() {
        return arena;
    }

    /**
     * Retrieve an arena player by their UUID
     * can be null if they are not in the arena
     *
     * @param playerID The player's UUID
     * @return The arena player
     */
    public ArenaPlayer getArenaPlayer(UUID playerID) {
        return arenaPlayers.get(playerID);
    }

    /**
     * Call when a player moves into the arena
     *
     * @param player The player to add
     */
    public void addArenaPlayer(Player player) {
        ArenaPlayer arenaPlayer = new ArenaPlayer(player);
        ArenaPlayer old = arenaPlayers.put(player.getUniqueId(), arenaPlayer);
        if (old != null)
            old.leave();

        arenaPlayer.enter();
        arena.getScoreboard().addToScoreboard(player);
    }

    /**
     * Call when a player moves out of the arena
     *
     * @param player The player to remove
     */
    public void removeArenaPlayer(Player player) {
        ArenaPlayer removed = arenaPlayers.remove(player.getUniqueId());
        // If the player was not in the arena, ignore
        if (removed == null)
            return;

        // If the player was an active player, tell the server that they are quitting
        if (arena.getGameplayManager().isActivePlayer(removed))
            new PlayerAttemptLeaveArenaActivePlayersEvent(removed).callEvent();

        // Now we should be safe to remove the player and restore their inventory and such
        removed.leave();
        arena.getScoreboard().removeFromScoreboard(player);
    }

    public Collection<ArenaPlayer> getAllPlayersInArena() {
        return arenaPlayers.values();
    }

    public void broadcast(Component component) {
        Component msg = ComponentDefinitions.PREFIX.append(component);
        for (ArenaPlayer arenaPlayer : arenaPlayers.values())
            arenaPlayer.getPlayer().sendMessage(msg);
    }

    public void cleanup() {

        for (ArenaPlayer arenaPlayer : arenaPlayers.values())
            arenaPlayer.leave();

        arenaPlayers.clear();

        HandlerList.unregisterAll(this);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {

        // If the player did not move their location, ignore
        if (!event.hasChangedPosition())
            return;

        // If the player went from out of the arena to in the arena, add them
        if (!SMPMobArena.getInstance().getArena().isInArena(event.getFrom()) && SMPMobArena.getInstance().getArena().isInArena(event.getTo()))
            addArenaPlayer(event.getPlayer());
        // If the player went from in the arena to out of the arena, remove them
        else if (SMPMobArena.getInstance().getArena().isInArena(event.getFrom()) && !SMPMobArena.getInstance().getArena().isInArena(event.getTo()))
            removeArenaPlayer(event.getPlayer());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeArenaPlayer(event.getPlayer());
    }




}
