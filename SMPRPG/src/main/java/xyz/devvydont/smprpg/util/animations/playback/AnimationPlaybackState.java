package xyz.devvydont.smprpg.util.animations.playback;

/**
 * The current state of the animation playback.
 */
public enum AnimationPlaybackState {
    /**
     * The animation is actively running through its frames.
     */
    PLAYING,

    /**
     * The animation has been paused.
     */
    PAUSED,

    /**
     * The animation was canceled or has finished.
     */
    FINISHED
}
