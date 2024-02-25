package me.devvy.smpparkour.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class ParkourTimer extends BukkitRunnable {

    public static DecimalFormat PLAYER_TIMER_DECIMAL_FORMAT = new DecimalFormat("00.000");

    private enum TimerState {
        WAITING,  // Timer is in 'reset' position, initial gray 00:00.0
        RUNNING,  // Timer is keeping track of time
        PRACTICING,  // Timer is keeping track of time, but we are practicing
        FINISHED;  // Timer finished and is displaying the final time
    }

    private ParkourPlayer parkourPlayer;
    private long startTimestamp = 0;
    private long endTimestamp = 0;
    private TimerState state = TimerState.WAITING;

    public ParkourTimer(ParkourPlayer parkourPlayer) {
        this.parkourPlayer = parkourPlayer;
    }

    /**
     * Call to reset timer to display gray 0:00
     */
    public void reset() {
        this.state = TimerState.WAITING;
        this.startTimestamp = 0;
        this.endTimestamp = 0;
    }

    /**
     * Call to 'finish' the timer, basically when we finish the course
     */
    public void stop() {
        this.state = TimerState.FINISHED;
        this.endTimestamp = System.currentTimeMillis();
    }

    /**
     * Call to start the timer from 0
     */
    public void start() {
        this.state = TimerState.RUNNING;
        startTimestamp = System.currentTimeMillis();
        endTimestamp = 0;
    }

    public void startPractice() {
        start();
        state = TimerState.PRACTICING;
    }

    public float elapsedTimeSeconds() {

        // If we are waiting to start the timer, then 0
        if (state == TimerState.WAITING)
            return 0;

        // If we finished, display the different in start and end
        if (state == TimerState.FINISHED)
            return (endTimestamp - startTimestamp) / 1000f;

        // If we are ticking, display current time - start
        return (System.currentTimeMillis() - startTimestamp) / 1000.0f;
    }

    public long elapsedTimeMs() {

        // If we are waiting to start the timer, then 0
        if (state == TimerState.WAITING)
            return 0;

        // If we finished, display the different in start and end
        if (state == TimerState.FINISHED)
            return endTimestamp - startTimestamp;

        // If we are ticking, display current time - start
        return System.currentTimeMillis() - startTimestamp;
    }

    public String getTimeFormattedString() {
        float totalElapsedTime = elapsedTimeSeconds();
        return String.format("%d:%s", (int)totalElapsedTime / 60, PLAYER_TIMER_DECIMAL_FORMAT.format(totalElapsedTime % 60));
    }

    public TextColor getTimerColor() {

        // If we are waiting to start the timer, then gray
        if (state == TimerState.WAITING)
            return NamedTextColor.DARK_GRAY;

        // If we finished, then gold
        if (state == TimerState.FINISHED)
            return NamedTextColor.GOLD;

        if (state == TimerState.PRACTICING)
            return NamedTextColor.GRAY;

        // If we are ticking, aqua
        return NamedTextColor.AQUA;
    }

    public void display() {

        Component display = Component.empty();

        if (state == TimerState.WAITING)
            display = display.append(Component.text("PAUSED ", NamedTextColor.GRAY, TextDecoration.BOLD));

        if (state == TimerState.FINISHED)
            display = display.append(Component.text("FINISHED! ", NamedTextColor.GREEN, TextDecoration.BOLD));

        display = display.append(Component.text(getTimeFormattedString(), getTimerColor()));
        parkourPlayer.getPlayer().sendActionBar(display);
    }

    @Override
    public void run() {
        display();
    }
}
