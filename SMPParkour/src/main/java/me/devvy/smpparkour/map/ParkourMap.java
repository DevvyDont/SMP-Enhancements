package me.devvy.smpparkour.map;


import me.devvy.smpparkour.checkpoints.Checkpoint;
import org.bukkit.Location;

/**
 * Represents a fully instantiated and valid parkour map.
 */
public class ParkourMap extends ParkourMapBase {

    private final String name;
    private final String path;
    private final Location leaderboardLocation;
    private final Checkpoint[] checkpoints;

    public ParkourMap(String name, String path, Location leaderboard, Checkpoint[] checkpoints) {
        this.name = name;
        this.path = path;
        this.leaderboardLocation = leaderboard;
        this.checkpoints = checkpoints;
    }

    @Override
    public String getMapName() {
        return name;
    }

    @Override
    public String getMapPath() {
        return path;
    }

    @Override
    public Location getLeaderboardLocation() {
        return leaderboardLocation;
    }

    @Override
    public Checkpoint[] getCheckpoints() {
        return checkpoints;
    }
}
