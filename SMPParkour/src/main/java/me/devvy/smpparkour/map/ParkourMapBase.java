package me.devvy.smpparkour.map;

import me.devvy.smpparkour.checkpoints.Checkpoint;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Location;

/**
 * An interface that provides an outline for what methods a parkour map should have.
 */
public abstract class ParkourMapBase {

    public abstract String getMapName();
    public abstract String getMapPath();

    public abstract Location getLeaderboardLocation();


    public abstract Checkpoint[] getCheckpoints();

    /**
     * Checks how many checkpoints are registered for this map.
     *
     * @return an integer from 0->n where n is any positive number
     */
    public int getNumCheckpoints() {
        return getCheckpoints().length;
    }

    /**
     * Used to check if a map is allowed to function correctly.
     * A map may function as long as there are at least two checkpoints defined.
     *
     * @return true if this map is valid, false otherwise
     */
    public boolean valid() {
        return getNumCheckpoints() >= 2;
    }

    /**
     * Returns the first checkpoint of this map, in other words the start of the map
     * Will throw an exception if no checkpoints are defined
     *
     * @return A Checkpoint instance where a parkour map starts
     */
    public Checkpoint getStart() {
        return getCheckpoints()[0];
    }

    /**
     * Returns the last checkpoint of this map. The last checkpoint is essentially the finish line.
     * Will throw an exception if no checkpoints are defined
     *
     * @return A Checkpoint instance where the parkour map ends
     */
    public Checkpoint getFinish() {
        return getCheckpoints()[getNumCheckpoints() - 1];
    }

    /**
     * Depending on how far in a checkpoint index is, return a color to match it
     *
     * @return
     */
    public NamedTextColor getPercentageColor(int index) {

        float percentage = (float)(index+1) / (float)getNumCheckpoints();

        if (percentage < .20f)
            return NamedTextColor.GREEN;

        if (percentage < .40f)
            return NamedTextColor.YELLOW;

        if (percentage < .60f)
            return NamedTextColor.GOLD;

        if (percentage < .80f)
            return NamedTextColor.RED;

        if (percentage < 1f)
            return NamedTextColor.DARK_RED;

        return NamedTextColor.LIGHT_PURPLE;

    }
}
