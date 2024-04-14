package me.devvy.smpparkour.map;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.checkpoints.Checkpoint;
import me.devvy.smpparkour.events.PlayerCompletedParkourEvent;
import me.devvy.smpparkour.events.PlayerEnteredCheckpointEvent;
import me.devvy.smpparkour.player.ParkourPlayer;
import me.devvy.smpparkour.util.Fireworks;
import me.devvy.smpparkour.util.HolographicLeaderboard;
import me.devvy.smpparkour.util.MapYamlUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class MapManager implements Listener {

    private final ParkourMapBase map;
    private final HolographicLeaderboard leaderboard;

    private final BukkitRunnable updateTask;

    private BukkitRunnable checkpointVisualizationTask;

    public MapManager() {

        map = MapYamlUtil.loadFromYaml(new File(Bukkit.getWorldContainer() + "/" + SMPParkour.PARKOUR_WORLD_FOLDER_NAME, "parkour.yml"));
        MapYamlUtil.saveToYaml(map);

        SMPParkour.getInstance().getServer().getPluginManager().registerEvents(this, SMPParkour.getInstance());

        // Enable the checkpoint regions
        for (Checkpoint cp : map.getCheckpoints())
            cp.enable();

        map.getFinish().enable();

        this.leaderboard = new HolographicLeaderboard(map);
        this.leaderboard.update();

        // Run a task every minute to update the scoreboard
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                leaderboard.update();
            }
        };
        updateTask.runTaskTimer(SMPParkour.getInstance(), 20*60*5, 20*60*5);
    }

    public ParkourMapBase getMap() {
        return map;
    }

    public void handlePassthroughCheckpoint(Player player, Checkpoint section) {

        ParkourPlayer pkp = SMPParkour.getInstance().getPlayerManager().getParkourPlayer(player);
        if (pkp == null)
            return;

        Checkpoint playerCheckpoint = pkp.getCurrentCheckpoint();
        if (playerCheckpoint == null)
            return;

        // If the checkpoint they passed is higher index than current, progress them otherwise ignore
        if (section.getIndex() <= playerCheckpoint.getIndex())
            return;

        // At this point we have progressed in the course
        Component checkpointName = Component.text(section.getName(), getMap().getPercentageColor(section.getIndex()));
        player.showTitle(Title.title(
                Component.empty(),
                checkpointName.append(Component.text(String.format(" [%s]", section.getIndex()+1), NamedTextColor.GRAY))
        ));

        // Spawn fireworks if this is not spawn
        if (section.getIndex() > 0)
            Fireworks.spawnFireworksInstantly(player.getEyeLocation().subtract(0, .5, 0), Color.GREEN);

        // Have we finished?
        if (map.getFinish() == section)
            pkp.finishParkour();

        pkp.setCurrentCheckpoint(section);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
    }

    /**
     * Toggles the visibility of the checkpoint regions
     */
    public void toggleCheckpointVisualization() {

        // If we're currently visualizing, end
        if (isVisualizingCheckpoints()) {
            checkpointVisualizationTask.cancel();
            checkpointVisualizationTask = null;

            // Loop through the checkpoints and stop visualizing them
            for (Checkpoint cp : map.getCheckpoints())
                cp.stopVisualizingSpawnLocation();

            return;
        }

        // Loop through the checkpoints and start visualizing them
        for (Checkpoint cp : map.getCheckpoints())
            cp.visualizeSpawnLocation();

        checkpointVisualizationTask = new BukkitRunnable() {
            @Override
            public void run() {

                // Loop through the checkpoint regions and visualize them
                for (Checkpoint cp : map.getCheckpoints())
                    cp.getRegion().visualize();
            }
        };
        // Run a few times a second
        checkpointVisualizationTask.runTaskTimer(SMPParkour.getInstance(), 0, 2);
    }

    public boolean isVisualizingCheckpoints() {
        return checkpointVisualizationTask != null;
    }

    public void cleanup() {

        if (isVisualizingCheckpoints())
            toggleCheckpointVisualization();

        updateTask.cancel();
        this.leaderboard.delete();
    }

    @EventHandler
    public void onPassthroughCheckpoint(PlayerEnteredCheckpointEvent event) {
        handlePassthroughCheckpoint(event.getPlayer(), event.getCheckpoint());
    }

    @EventHandler
    public void onPlayerFinishParkour(PlayerCompletedParkourEvent event) {

        new BukkitRunnable() {
            @Override
            public void run() {
                leaderboard.update();
            }
        }.runTaskLater(SMPParkour.getInstance(), 20*3);

    }

    // Protect the map from being broken
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (!event.getPlayer().isOp() && event.getPlayer().getWorld() == map.getStart().getSpawn().getWorld())
            event.setCancelled(true);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (!event.getPlayer().isOp() && event.getPlayer().getWorld() == map.getStart().getSpawn().getWorld())
            event.setCancelled(true);
    }

}
