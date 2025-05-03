package xyz.devvydont.smprpg.entity.interfaces;

import xyz.devvydont.smprpg.entity.components.DamageTracker;

/**
 * Represents a class that has a damage tracker component.
 */
public interface IDamageTrackable {

    /**
     * Retrieve the damage tracker associated with this instance.
     * @return A damage tracker that tracks damage sources.
     */
    DamageTracker getDamageTracker();

}
