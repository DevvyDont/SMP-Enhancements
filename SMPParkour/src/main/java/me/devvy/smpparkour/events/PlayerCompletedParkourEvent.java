package me.devvy.smpparkour.events;

import me.devvy.smpparkour.player.ParkourPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCompletedParkourEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private ParkourPlayer player;

    public PlayerCompletedParkourEvent(ParkourPlayer player) {
        this.player = player;
    }

    public ParkourPlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }


}
