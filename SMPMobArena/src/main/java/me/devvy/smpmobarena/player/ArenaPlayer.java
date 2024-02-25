package me.devvy.smpmobarena.player;

import me.devvy.smpmobarena.events.PlayerEnteredMobArenaEvent;
import me.devvy.smpmobarena.events.PlayerLeftMobArenaEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Wrapper class for a player in the arena
 */
public class ArenaPlayer {

    public enum ArenaPlayerState {
        PLAYING,     // Playing the game, has not died this wave
        SPECTATING,  // Not participating in the arena
        DEAD         // Is playing the game, but has died this wave
    }

    private UUID playerID;
    private ArenaPlayerState state = ArenaPlayerState.SPECTATING;
    private PlayerStateSnapshot snapshot;

    private Location safeLocation;  // Location that we know is safe for teleporting when dying and ending games

    public ArenaPlayer(Player player) {
        this.playerID = player.getUniqueId();
    }

    public Location getSafeLocation() {
        return safeLocation;
    }

    public void setSafeLocation(Location safeLocation) {
        this.safeLocation = safeLocation;
    }

    public ArenaPlayerState getState() {
        return state;
    }

    public void setState(ArenaPlayerState state) {
        this.state = state;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    // Currently, we do not support players rejoining so this will never be null
    public Player getPlayer() {
        return Bukkit.getPlayer(playerID);
    }

    // Method that should always be called when we enter the arena area (even as a spectator)
    public void enter() {

        Player player = getPlayer();

        // Create a snapshot for this player and clear their inventory afterward
        this.snapshot = new PlayerStateSnapshot(player);
        player.getInventory().clear();
        player.sendMessage(Component.text("Your inventory has been stowed!", NamedTextColor.YELLOW));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);

        setState(ArenaPlayerState.SPECTATING);
        setSpectatingLoadout();

        new PlayerEnteredMobArenaEvent(player).callEvent();
    }

    // Method that should always be called when we leave the arena area (even as a spectator)
    public void leave() {

        Player player = getPlayer();
        player.getInventory().clear();

        // Restore the player's inventory and snapshot
        snapshot.restore(player);
        player.sendMessage(Component.text("Your inventory has been restored!", NamedTextColor.GREEN));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

        new PlayerLeftMobArenaEvent(player).callEvent();
    }

    public void setSpectatingLoadout() {
        getPlayer().getInventory().clear();
    }

}
