package me.devvy.smpbuildworld.player;

import me.devvy.smpbuildworld.SMPBuildWorld;
import me.devvy.smpbuildworld.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.*;

public class PlayerManager implements Listener {

    Map<UUID, Location> playerLocationCache = new HashMap<>();

    public PlayerManager() {
        SMPBuildWorld.getInstance().getServer().getPluginManager().registerEvents(this, SMPBuildWorld.getInstance());
    }

    public void toBuildWorld(Player player) {

        if (!SMPBuildWorld.getInstance().isBuildWorldEnabled())
            throw new IllegalStateException("Build World is not enabled!");

        if (playerLocationCache.containsKey(player.getUniqueId()))
            throw new IllegalStateException("Player is already at build world!");

        playerLocationCache.put(player.getUniqueId(), player.getLocation());
        player.teleport(SMPBuildWorld.getInstance().getBuildWorld().getSpawnLocation());
        player.setInvulnerable(true);

        player.sendMessage(
                ComponentUtil.getEventPrefix()
                        .append(Component.text("Welcome to ", TextColor.color(200, 200, 200)))
                        .append(Component.text("Build World!", TextColor.color(255, 180, 0), TextDecoration.BOLD))
        );
        player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

    }

    public void exitBuildWorld(Player player) {

        if (!SMPBuildWorld.getInstance().isBuildWorldEnabled())
            throw new IllegalStateException("Build World is not enabled!");

        if (!playerLocationCache.containsKey(player.getUniqueId()))
            throw new IllegalStateException("Player is not at build world!");

        player.teleport(playerLocationCache.get(player.getUniqueId()));
        player.setInvulnerable(false);
        playerLocationCache.remove(player.getUniqueId());

        player.sendMessage(
                ComponentUtil.getEventPrefix()
                        .append(Component.text("Going back to survival! Come back any time with ", ComponentUtil.GRAY))
                        .append(Component.text("/buildworld warp", ComponentUtil.GREEN, TextDecoration.BOLD))
                        .append(Component.text("!", ComponentUtil.GRAY))
        );
        player.playSound(player.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }

    public boolean inBuildWorld(Player player) {
        return playerLocationCache.containsKey(player.getUniqueId());
    }

    public void cleanup() {

        List<UUID> pplInBuildWorld = new ArrayList<>(playerLocationCache.keySet());

        if (SMPBuildWorld.getInstance().isBuildWorldEnabled())
            for (UUID pid : pplInBuildWorld)
                exitBuildWorld(Bukkit.getPlayer(pid));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        // If people quit in build world tp them out
        if (inBuildWorld(event.getPlayer()))
            exitBuildWorld(event.getPlayer());

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        // If people die? in build world tp them to build world spawn
        if (inBuildWorld(event.getPlayer()))
            event.setRespawnLocation(SMPBuildWorld.getInstance().getBuildWorld().getSpawnLocation());

    }

}
