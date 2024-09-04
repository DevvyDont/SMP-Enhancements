package xyz.devvydont.smprpg.util.animations.utils;

import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.util.animations.iterators.AnimationFrame;
import xyz.devvydont.smprpg.util.animations.iterators.AnimationIterator;
import xyz.devvydont.smprpg.util.animations.iterators.InfiniteIterator;
import xyz.devvydont.smprpg.util.animations.iterators.NumberedIterator;

import java.util.ArrayList;

/**
 * A utility to create animations using the builder pattern.
 */
public final class AnimationBuilder {
    private AnimationIterator iterator;
    private final ArrayList<AnimationFrame> frames = new ArrayList<>();

    /**
     * Adds a new frame to the animation.
     *
     * @param frame The frame callback function.
     * @return The builder for chaining.
     */
    public AnimationBuilder addFrame(@NotNull AnimationFrame frame) {
        if (this.iterator != null) {
            throw new IllegalStateException("Frames can not be added after setting an iterator preference.");
        }

        this.frames.add(frame);
        return this;
    }

    /**
     * Sets this animation to play a set amount of times.
     *
     * @param iterations The amount of times the animation should play.
     * @return The builder for chaining.
     */
    public AnimationBuilder setLoopIterations(int iterations) {
        this.iterator = new NumberedIterator(this.frames, iterations);
        return this;
    }

    /**
     * Sets this animation to play endlessly.
     *
     * @return The builder for chaining.
     */
    public AnimationBuilder setLoopInfinite() {
        this.iterator = new InfiniteIterator(this.frames);
        return this;
    }

    /**
     * Returns the animation iterator.
     *
     * @return The animation iterator to provide to the animation service.
     */
    public AnimationIterator build() {
        if (this.iterator == null) {
            throw new IllegalStateException("No iterator preference was set.");
        }

        return this.iterator;
    }
}
