package xyz.devvydont.smprpg.util.animations.playback;


import xyz.devvydont.smprpg.util.animations.blockers.AnimationBlocker;
import xyz.devvydont.smprpg.util.animations.iterators.AnimationIterator;

/**
 * Manages the playback of a single animation.
 */
public final class AnimationPlayer implements AnimationHandle {
    private AnimationBlocker activeBlocker;
    private AnimationPlaybackState playbackState;
    private final AnimationIterator iterator;

    public AnimationPlayer(AnimationIterator iterator) {
        this.iterator = iterator;
        this.playbackState = AnimationPlaybackState.PLAYING;
    }

    @Override
    public AnimationPlaybackState getPlaybackStatus() {
        return this.playbackState;
    }

    @Override
    public void pause() {
        if (this.playbackState == AnimationPlaybackState.PLAYING)
            this.playbackState = AnimationPlaybackState.PAUSED;
    }

    @Override
    public void resume() {
        if (this.playbackState == AnimationPlaybackState.PAUSED)
            this.playbackState = AnimationPlaybackState.PLAYING;
    }

    @Override
    public void stop() {
        this.playbackState = AnimationPlaybackState.FINISHED;
    }

    /**
     * Progress the animation by a single tick.
     */
    public void handleTick() {
        var isStopped = this.playbackState != AnimationPlaybackState.PLAYING;
        if (isStopped) {
            return;
        }

        var isFirstFrame = this.activeBlocker == null;
        var waitForNextFrame = !isFirstFrame && !this.activeBlocker.handleTick();
        if (waitForNextFrame) {
            return;
        }

        var nextFrame = this.iterator.getNextFrame();
        var reachedEndOfAnimation = nextFrame == null;
        if (reachedEndOfAnimation) {
            this.playbackState = AnimationPlaybackState.FINISHED;
            return;
        }

        this.activeBlocker = nextFrame.performFrame();
    }
}
