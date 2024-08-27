package xyz.devvydont.smprpg.util.animations.containers;

/**
 * The current state of the animation playback.
 */
public enum AnimationState {
    /**
     * The animation is actively running through its frames.
     */
    PLAYING,

    /**
     * The animation has been paused by something.
     */
    PAUSED,

    /**
     * The animation has finished or was canceled.
     */
    FINISHED
}
