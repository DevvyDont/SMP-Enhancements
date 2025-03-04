package xyz.devvydont.smprpg.util.animations.iterators;

import xyz.devvydont.smprpg.util.animations.blockers.AnimationBlocker;

/**
 * A callback function which contains a frame of the animation.
 */
public interface AnimationFrame {
    /**
     * Called to run the logic of the animation frame and retrieve the blocker.
     *
     * @return The animation blocker for the frame.
     */
    AnimationBlocker performFrame();
}

