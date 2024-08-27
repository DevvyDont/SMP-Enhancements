package xyz.devvydont.smprpg.util.animations.blockers;

/**
 * Blocks an animation for a set period of time.
 */
public interface AnimationBlocker {
    /**
     * Called each tick to check if the animation should move onto the next frame.
     *
     * @return True if the animation should progress, otherwise false.
     */
    boolean handleTick();
}
