package xyz.devvydont.smprpg.util.animations.playback;

/**
 * A control handle into an animation container.
 */
public interface AnimationHandle {
    /**
     * Gets the current playback status of the animation.
     *
     * @return The current playback status.
     */
    AnimationPlaybackState getPlaybackStatus();

    /**
     * Pauses this animation, if it's playing.
     */
    void pause();

    /**
     * Resumes this animation, if it was paused.
     */
    void resume();

    /**
     * Stops the animation, if it's playing or paused.
     */
    void stop();
}
