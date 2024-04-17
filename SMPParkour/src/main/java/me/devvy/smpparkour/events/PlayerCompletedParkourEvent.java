package me.devvy.smpparkour.events;

import me.devvy.smpparkour.player.ParkourPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCompletedParkourEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private ParkourPlayer player;
    private boolean practice;
    private long time;

    public PlayerCompletedParkourEvent(ParkourPlayer player) {
        this.player = player;
        this.practice = player.isPracticing();
        this.time = player.getTimer().elapsedTimeMs();
    }

    public ParkourPlayer getPlayer() {
        return player;
    }

    /**
     * Was this player practicing?
     *
     * @return true if they were practicing, false if this was a real run.
     */
    public boolean isPractice() {
        return practice;
    }

    /**
     * The player's time in milliseconds
     *
     * @return a long representing the time of this run in milliseconds
     */
    public long getTimeMs() {
        return time;
    }

    /**
     * The player's time in seconds
     *
     * @return a float representing the time of this run in seconds converted from getTimeMs()
     */
    public float getTimeSeconds() {
        return getTimeMs() / 1000f;
    }

    /**
     * Returns the 00:00.00 format of the time as a string
     *
     * @return a pretty formatted String of the time
     */
    public String getFormattedTime() {
        return getPlayer().getTimer().getTimeFormattedString();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }


}
