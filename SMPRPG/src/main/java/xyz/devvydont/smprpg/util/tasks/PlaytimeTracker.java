package xyz.devvydont.smprpg.util.tasks;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.time.TickTime;

/**
 * A simple task that increments all players' playtime by 1 minute every minute.
 */
public class PlaytimeTracker extends BukkitRunnable {

    public static final NamespacedKey PLAYTIME_KEY = new NamespacedKey("smprpg", "playtime");
    public static final NamespacedKey FIRST_SEEN_KEY = new NamespacedKey("smprpg", "first_seen");

    // We never want more than one of these instantiated ever. Keep track of the sole instance.
    private static PlaytimeTracker task = null;

    /**
     * Starts a new playtime tracking task. The task is returned, but it does not need to be managed since it is global.
     * @return A PlaytimeTracker instance. If you want to cancel it, you can either call cancel() on the returned task
     * or simply call PlaytimeTracker.stop()
     */
    public static PlaytimeTracker start() {
        stop();
        task = new PlaytimeTracker();
        task.runTaskTimer(SMPRPG.getInstance(), TickTime.INSTANTANEOUSLY, TickTime.minutes(1));
        return task;
    }

    /**
     * Stops the globally running playtime tracking task if it is running.
     */
    public static void stop() {
        if (task == null)
            return;

        task.cancel();
        task = null;
    }

    /**
     * Gets the play time of a player in minutes.
     * @param player The player in question.
     * @return How many minutes they have played.
     */
    public static int getPlaytime(Player player) {
        return player.getPersistentDataContainer().getOrDefault(PLAYTIME_KEY, PersistentDataType.INTEGER, 0);
    }

    /**
     * Gets the very first time a player joined the server. We store this as a timestamp on the player when they join
     * for the first time.
     * @param player The player to query join timestamp for.
     * @return The timestamp that is attached to the player. Keep in mind that System.currentTimeMillis() is used to store timestamps.
     */
    public static long getFirstSeen(Player player) {
        return player.getPersistentDataContainer().getOrDefault(FIRST_SEEN_KEY, PersistentDataType.LONG, System.currentTimeMillis());
    }

    /**
     * Simply adds the data necessary to track when this player first joined the server if they don't have it.
     * All you need to do is call this method quite literally anywhere in the plugin with zero checking,
     * and tracking will work as expected.
     * @param player The player to store a timestamp on if not present yet.
     */
    public static void setFirstSeenIfNotPresent(Player player) {
        // If they already have a timestamp set, don't do anything.
        if (player.getPersistentDataContainer().has(FIRST_SEEN_KEY, PersistentDataType.LONG))
            return;

        // Set it.
        player.getPersistentDataContainer().set(FIRST_SEEN_KEY, PersistentDataType.LONG, System.currentTimeMillis());
    }

    /**
     * Ran every minute. Simply just increment all play times by 1 for whoever is online.
     */
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getPersistentDataContainer().set(PLAYTIME_KEY, PersistentDataType.INTEGER, getPlaytime(player) + 1);
        }
    }
}
