package xyz.devvydont.smprpg.util.animations.blockers;

import java.time.Duration;

/**
 * A collection of Animation Blocker presets.
 */
public final class WaitFor {
    /**
     * Creates a blocker that waits for the specified amount of milliseconds.
     *
     * @param milliseconds The amount of milliseconds to wait for.
     * @return The animation blocker.
     */
    public static AnimationBlocker milliseconds(long milliseconds) {
        return duration(Duration.ofMillis(milliseconds));
    }

    /**
     * Creates a blocker that waits for the specified amount of seconds.
     *
     * @param seconds The amount of seconds to wait for.
     * @return The animation blocker.
     */
    public static AnimationBlocker seconds(long seconds) {
        return duration(Duration.ofSeconds(seconds));
    }

    /**
     * Creates a blocker that waits for the specified amount of minutes.
     *
     * @param minutes The amount of minutes to wait for.
     * @return The animation blocker.
     */
    public static AnimationBlocker minutes(long minutes) {
        return duration(Duration.ofMinutes(minutes));
    }

    /**
     * Creates a blocker that waits for the specified duration.
     *
     * @param duration The amount of time to wait for.
     * @return The animation blocker.
     */
    public static AnimationBlocker duration(Duration duration) {
        return new TimeBlocker(duration);
    }

    /**
     * Creates a blocker that waits for the specified amount of ticks.
     *
     * @param ticks The amount of ticks to wait for.
     * @return The animation blocker.
     */
    public static AnimationBlocker ticks(int ticks) {
        return new TickBlocker(ticks);

    }

    /**
     * Creates a blocker that waits until the next tick.
     *
     * @return The animation blocker.
     */
    public static AnimationBlocker nextTick() {
        return new TickBlocker(1);
    }

    /**
     * Creates a blocker that immediately unblocks.
     *
     * @return The animation blocker.
     */
    public static AnimationBlocker nothing() {
        return new TickBlocker(-1);
    }
}
