package me.devvy.smpevents.events;

import org.bukkit.scheduler.BukkitRunnable;

public class EventTimerTask extends BukkitRunnable {

    public static final int PERIOD = 20 * 60;  // runs once a minute

    private int minutesLeft;
    private Event event;

    public EventTimerTask(Event event, int minutesToStart) {
        this.minutesLeft = minutesToStart;
        this.event = event;
    }

    @Override
    public void run() {

        // Out of time? Start the event and cancel
        if (minutesLeft <= 0) {
            event.start();
            cancel();
            return;
        }

        // Announce the event so people have the oppurtunity to join
        if (shouldAnnounce())
            event.announce(String.format("%s min", minutesLeft));

        minutesLeft--;
    }

    private boolean shouldAnnounce() {
        if (minutesLeft <= 5)
            return true;

        if (minutesLeft % 5 == 0)
            return true;

        return false;
    }
}
