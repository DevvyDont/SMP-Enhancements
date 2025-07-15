package xyz.devvydont.smprpg.util.time;

/**
 * Helps make the idea of Time <---> Ticks easier to read. If you want to make a task that runs
 * code in 8 seconds, you can instead use TickTime.seconds(8) instead of 8L * 20L.
 */
public class TickTime {

    // The tick rate (ticks per second) of the server. Used for math conversions.
    public static final int TPS = 20;

    // Some useful sub second measurements that are commonly used in tasks.
    public static final long HALF_SECOND = 10;
    public static final long SECOND = seconds(1);
    public static final long TENTH_SECOND = 2;
    public static final long TICK = 1;

    // Used as a way to give off the message you want to execute a task instantly rather than just using 0.
    public static final long INSTANTANEOUSLY = 0;
    /**
     * Converts real life seconds to server ticks.
     * @param seconds The seconds to convert to ticks.
     * @return The amount of server ticks equivalent to real life seconds.
     */
    public static long seconds(long seconds) {
        return seconds * TPS;
    }

    /**
     * Converts real life minutes to server ticks.
     * @param minutes The seconds to convert to ticks.
     * @return The amount of server ticks equivalent to real life minutes.
     */
    public static long minutes(long minutes) {
        return seconds(60) * minutes;
    }

}
