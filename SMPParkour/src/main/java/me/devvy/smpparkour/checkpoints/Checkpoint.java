package me.devvy.smpparkour.checkpoints;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.events.PlayerEnteredCheckpointEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
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
