package xyz.devvydont.smprpg.util.misc;

import org.bukkit.Location;

/**
 * Simple delegate that takes in some function that can return true or false based on the context of a {@link Location}.
 */
public interface ILocationPredicate {

    /**
     * Checks this predicate against a {@link Location}.
     * @return True if passed, false otherwise.
     */
    boolean check(Location location);
}
