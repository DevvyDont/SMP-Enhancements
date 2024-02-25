package me.devvy.smpbuildworld.tasks;

import me.devvy.smpbuildworld.SMPBuildWorld;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DelayEventWarpTask extends BukkitRunnable {

    public static final int PERIOD = 20;  // 20 ticks/1s cycles
    public static final int COUNTDOWN = 7;  // 7 seconds until tp

    // Players waiting to teleport, used to prevent dupe tp tasks
    private static final Set<UUID> warpingPlayers = new HashSet<>();

    public static boolean playerIsMidWarp(Player player) {
        return warpingPlayers.contains(player.getUniqueId());
    }

    private int ticksLeft = COUNTDOWN;  // When this hits 0, then we perform the teleport
    private final Player player;
    private final Location invokeLocation;

    public DelayEventWarpTask(Player player) {
        this.player = player;
        this.invokeLocation = player.getLocation().clone();
        warpingPlayers.add(player.getUniqueId());
    }

    @Override
    public void run() {

        // Sanity checks, if we move or log out cancel the task
        if (Bukkit.getPlayer(player.getUniqueId()) == null || !player.getLocation().getBlock().equals(invokeLocation.getBlock())) {
            player.sendActionBar(
                    Component.text("Teleport canceled! Please don't move!", TextColor.color(200, 0, 0))
            );
            player.clearTitle();
            player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            this.cancel();
            return;
        }

        // If we are out of time, perform the teleport and cancel
        if (ticksLeft <= 0) {
            SMPBuildWorld.getInstance().getPlayerManager().toBuildWorld(player);
            player.sendActionBar(
                    Component.text("Teleporting!", TextColor.color(0, 200, 0))
            );
            player.clearTitle();
            this.cancel();
            return;
        }

        // Otherwise, say how much time we have left
        player.showTitle(Title.title(
                Component.text(ticksLeft, TextColor.color(200, 200, 0)),
                Component.empty()
        ));

        player.sendActionBar(
                Component.text("Teleporting to ", TextColor.color(200, 200, 200))
                        .append(Component.text("Build World!", TextColor.color(255, 165, 0), TextDecoration.BOLD))
        );

        float pitch = (float) (.5f + Math.pow((COUNTDOWN - ticksLeft)/7f, 3));
        player.playSound(player.getEyeLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .35f, pitch);
        ticksLeft--;

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        warpingPlayers.remove(player.getUniqueId());
        super.cancel();
    }
}
