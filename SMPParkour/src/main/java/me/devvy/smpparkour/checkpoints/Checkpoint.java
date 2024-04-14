package me.devvy.smpparkour.checkpoints;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.events.PlayerEnteredCheckpointEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class Checkpoint implements Listener {

    private final Location spawn;
    private final CheckpointRegion region;
    private final int index;

    private final String name;

    private ArmorStand spawnMarkerEntity = null;

    public Checkpoint(Location spawn, CheckpointRegion region, int index, String name) {
        this.spawn = spawn;
        this.region = region;
        this.index = index;
        this.name = name;
    }

    public void enable() {
        SMPParkour.getInstance().getServer().getPluginManager().registerEvents(this, SMPParkour.getInstance());
    }

    public void disable() {
        HandlerList.unregisterAll(this);
    }

    public Location getSpawn() {
        return spawn;
    }

    public CheckpointRegion getRegion() {
        return region;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    /**
     * Call to show an armor stand that represents the respawn location of this checkpoint
     */
    public void visualizeSpawnLocation() {
        if (spawnMarkerEntity != null)
            return;

        spawnMarkerEntity = getSpawn().getWorld().spawn(getSpawn(), ArmorStand.class);
        spawnMarkerEntity.setCustomNameVisible(true);

        Component name = Component.text(getName(), SMPParkour.getInstance().getMapManager().getMap().getPercentageColor(getIndex()));
        name = name.append(Component.text(String.format(" [%s]", getIndex()+1), NamedTextColor.GRAY));
        spawnMarkerEntity.customName(name);

        spawnMarkerEntity.setInvulnerable(true);
        spawnMarkerEntity.setMarker(true);
        spawnMarkerEntity.setInvisible(true);

        spawnMarkerEntity.teleport(getSpawn().add(0, 2, 0));
    }

    /**
     * Call to stop showing an armor stand that represents the respawn location of this checkpoint
     */
    public void stopVisualizingSpawnLocation() {
        if (spawnMarkerEntity == null)
            return;

        spawnMarkerEntity.remove();
        spawnMarkerEntity = null;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerEnteredCheckpoint(PlayerMoveEvent event) {

        // If position has not changed we do not care
        if (!event.hasChangedPosition())
            return;

        // If we are moving to this bounding box then trigger an event, we don't care for exiting only entering
        if (!getRegion().contains(event.getFrom()) && getRegion().contains(event.getTo()))
            new PlayerEnteredCheckpointEvent(event.getPlayer(), this).callEvent();

    }

}
