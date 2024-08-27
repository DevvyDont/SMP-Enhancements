package xyz.devvydont.smprpg.util.animations.containers;


import xyz.devvydont.smprpg.util.animations.blockers.AnimationBlocker;

import java.util.List;

/**
 * Contains and manages the playback of a single animation.
 */
public final class AnimationContainer implements AnimationHandle {
    private int activeFrameIndex;
    private AnimationState playbackState;
    private AnimationBlocker activeBlocker;
    private final List<AnimationFrame> frames;

    public AnimationContainer(List<AnimationFrame> animationFrames) {
        if (animationFrames.isEmpty())
            throw new IllegalStateException("Provided frame list is empty");

        this.frames = animationFrames;
        this.playbackState = AnimationState.PLAYING;
        this.activeBlocker = animationFrames.get(activeFrameIndex).performFrame();
    }

    @Override
    public AnimationState getPlaybackStatus() {
        return playbackState;
    }

    @Override
    public void pause() {
        if (playbackState == AnimationState.PLAYING)
            playbackState = AnimationState.PAUSED;
    }

    @Override
    public void resume() {
        if (playbackState == AnimationState.PAUSED)
            playbackState = AnimationState.PLAYING;
    }

    @Override
    public void stop() {
        playbackState = AnimationState.FINISHED;
    }

    /**
     * Progress the animation by a single game tick.
     */
    public void handleTick() {
        var isStopped = playbackState != AnimationState.PLAYING;
        if (isStopped) {
            return;
        }

        var readyForNextFrame = activeBlocker.handleTick();
        if (!readyForNextFrame) {
            return;
        }

        activeFrameIndex++;
        var nextFrameExists = activeFrameIndex < frames.size();
        if (!nextFrameExists) {
            playbackState = AnimationState.FINISHED;
            return;
        }

        // Load the next frame
        activeBlocker = frames.get(activeFrameIndex).performFrame();
    }
}
