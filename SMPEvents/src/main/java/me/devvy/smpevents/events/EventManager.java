package me.devvy.smpevents.events;

public class EventManager {


    private Event currentEvent;

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public boolean createEvent() {

        if (currentEvent != null)
            return false;

        currentEvent = new Event();
        return true;
    }

    public boolean startTickingEvent() {

        if (currentEvent == null)
            return false;

        currentEvent.countdown();
        return true;
    }

    public boolean stopEvent() {

        if (currentEvent == null)
            return false;

        currentEvent.end();
        currentEvent = null;
        return true;
    }

}
