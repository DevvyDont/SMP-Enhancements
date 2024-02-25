package me.devvy.smpduels.events;

import me.devvy.smpduels.duels.DuelRequest;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DuelRequestTimeoutEvent extends Event {

    private DuelRequest duelRequest;

    public DuelRequestTimeoutEvent(DuelRequest duelRequest) {
        this.duelRequest = duelRequest;
    }

    public DuelRequest getDuelRequest() {
        return duelRequest;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
