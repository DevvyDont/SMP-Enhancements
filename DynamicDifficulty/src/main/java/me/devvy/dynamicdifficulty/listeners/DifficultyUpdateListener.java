package me.devvy.dynamicdifficulty.listeners;

import me.devvy.dynamicdifficulty.DynamicDifficulty;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DifficultyUpdateListener implements Listener {

    public DifficultyUpdateListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DynamicDifficulty.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DynamicDifficulty.getInstance().checkForNewDifficulty();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                DynamicDifficulty.getInstance().checkForNewDifficulty();
            }
        }.runTaskLater(DynamicDifficulty.getInstance(), 1);
    }

}
