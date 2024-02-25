package me.devvy.smpenhancements.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ProtectDeathDropsEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled = false;
    private final PlayerDeathEvent playerDeathEvent;

    public ProtectDeathDropsEvent(PlayerDeathEvent playerDeathEvent) {
        this.playerDeathEvent = playerDeathEvent;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public PlayerDeathEvent getPlayerDeathEvent() {
        return playerDeathEvent;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
