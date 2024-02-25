package me.devvy.deathdrops.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ProtectDeadDropItemsEvent extends Event implements Cancellable {


    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled = false;
    private PlayerDeathEvent playerDeathEvent;

    public ProtectDeadDropItemsEvent(PlayerDeathEvent event){
        this.playerDeathEvent = event;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public PlayerDeathEvent getPlayerDeathEvent() {
        return playerDeathEvent;
    }
}
