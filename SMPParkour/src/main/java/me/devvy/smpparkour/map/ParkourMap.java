package me.devvy.smpparkour.map;

import me.devvy.smpparkour.checkpoints.Checkpoint;
import me.devvy.smpparkour.checkpoints.CheckpointRegion;
import org.bukkit.Location;

import java.util.List;

public interface ParkourMap {

    Location getStart();
    Checkpoint[] getCheckpoints();
    Checkpoint getFinish();

}
