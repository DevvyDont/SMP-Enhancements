package xyz.devvydont.smprpg.util.animations.utils;

import xyz.devvydont.smprpg.util.animations.containers.AnimationFrame;

import java.util.ArrayList;

/**
 * A utility to create animations using the builder pattern.
 */
public final class AnimationBuilder {
    private final ArrayList<AnimationFrame> frames = new ArrayList<>();

    /**
     * Adds a new frame to the animation.
     *
     * @param frame The frame callback function.
     * @return The builder for chaining.
     */
    public AnimationBuilder addFrame(AnimationFrame frame) {
        frames.add(frame);
        return this;
    }

    /**
     * Builds the frames into a list.
     *
     * @return The animation frames as a list.
     */
    public ArrayList<AnimationFrame> build() {
        return frames;
    }
}
