package xyz.devvydont.smprpg.util.animations.iterators;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Plays an animation endlessly.
 */
public final class InfiniteIterator implements AnimationIterator {
    private int currentFrameIndex = -1;
    private final @NotNull List<@NotNull AnimationFrame> frames;

    public InfiniteIterator(@NotNull List<@NotNull AnimationFrame> frames) {
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("Frame list cannot be empty");
        }

        this.frames = frames;
    }

    @Override
    public @NotNull AnimationFrame getNextFrame() {
        // Move the frame pointer to the next frame.
        this.currentFrameIndex++;

        // Check to see if we're at the end of the animation.
        if (this.frames.size() <= this.currentFrameIndex) {
            this.currentFrameIndex = 0;
        }

        // Return the next frame
        return this.frames.get(this.currentFrameIndex);
    }
}
