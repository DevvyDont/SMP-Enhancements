package xyz.devvydont.smprpg.util.animations.blockers;

import java.time.Duration;

/**
 * A blocker which waits for a time duration to pass.
 */
final class TimeBlocker implements AnimationBlocker {
    private final long endTimestamp;

    TimeBlocker(Duration durationToWait) {
        this.endTimestamp = System.currentTimeMillis() + durationToWait.toMillis();
    }

    @Override
    public boolean handleTick() {
        return System.currentTimeMillis() >= endTimestamp;
    }
}
