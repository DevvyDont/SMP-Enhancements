package xyz.devvydont.smprpg.util.animations.iterators;

import org.jetbrains.annotations.Nullable;

/**
 * Controls the frame output of an animation.
 */
public interface AnimationIterator {
    /**
     * Gets the next frame in the animation.
     *
     * @return The next animation frame, or null if the animation has finished.
     */
    @Nullable AnimationFrame getNextFrame();
}
