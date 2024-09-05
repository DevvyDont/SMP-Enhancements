package xyz.devvydont.smprpg.util.animations.iterators;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Plays an animation a set number of times.
 */
public final class NumberedIterator implements AnimationIterator {
    private int currentFrameIndex = -1;
    private int performedIterations;
    private final int targetIterations;
    private final List<@NotNull AnimationFrame> frames;

    public NumberedIterator(@NotNull List<@NotNull AnimationFrame> frames, int iterations) {
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("Frame list cannot be empty");
        }

        this.frames = frames;
        this.targetIterations = iterations;
    }

    @Override
    public @Nullable AnimationFrame getNextFrame() {
        // Move the frame pointer to the next frame.
        this.currentFrameIndex++;

        // Check to see if we're at the end of the animation.
        var atEndOfIteration = this.frames.size() <= this.currentFrameIndex;
        if (atEndOfIteration) {
            // End the animation if we're finished all iterations.
            this.performedIterations++;
            if (this.targetIterations <= this.performedIterations) {
                return null;
            }

            // Otherwise move back to the first frame
            this.currentFrameIndex = 0;
        }

        // Return the next frame
        return this.frames.get(this.currentFrameIndex);
    }
}
