package me.devvy.smpparkour.map;


import me.devvy.smpparkour.checkpoints.Checkpoint;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

// A wrapper class used for parkour map building, allows partial construction of a parkour map
// where it can then be built by calling ParkourMapBuilder.build()
public class ParkourMapBuilder {

    private String name = "Unknown Parkour Map";
    private String path = "world_templateparkourmap";
    private Location leaderboardLocation = null;
    private final List<Checkpoint> checkpoints = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Location getLeaderboardLocation() {
        return leaderboardLocation;
    }

    public void setLeaderboardLocation(Location location) {
        this.leaderboardLocation = location;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }

    public void insertCheckpoint(Checkpoint checkpoint, int index) {
        checkpoints.add(index, checkpoint);
    }

    public void removeCheckpoint(int index) {
        checkpoints.remove(index);
    }

    public void popCheckpoint() {
        checkpoints.remove(checkpoints.size()-1);
    }

    public void clearCheckpoints() {
        checkpoints.clear();
    }

    public ParkourMap build() {
        ParkourMap finishedMap = new ParkourMap(name, path, leaderboardLocation, checkpoints.toArray(new Checkpoint[0]));

        if (!finishedMap.valid())
            throw new IllegalStateException("Map " + finishedMap.getMapName() + " is not complete! Please make sure it has at least two checkpoints.");

        return finishedMap;
    }

}
