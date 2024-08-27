package xyz.devvydont.smprpg.util.animations.blockers;

/**
 * A blocker that waits for a certain amount of ticks.
 */
final class TickBlocker implements AnimationBlocker {
    private int ticksWaited;
    private final int ticksToWait;

    TickBlocker(int ticksToWait) {
        // handleTick is called right after the constructor.
        // So after the first handleTick call, we've not waited a frame.
        // To counter this we set ticksWaited to -1, so after the first call it's 0.
        this.ticksWaited = -1;
        this.ticksToWait = ticksToWait;
    }

    @Override
    public boolean handleTick() {
        this.ticksWaited++;
        return this.ticksWaited >= this.ticksToWait;
    }
}
