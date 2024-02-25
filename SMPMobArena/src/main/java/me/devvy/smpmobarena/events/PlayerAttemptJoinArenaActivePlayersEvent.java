package me.devvy.smpmobarena.events;

import me.devvy.smpmobarena.player.ArenaPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAttemptJoinArenaActivePlayersEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private ArenaPlayer player;

    private boolean cancelled = false;


    public PlayerAttemptJoinArenaActivePlayersEvent(ArenaPlayer player){
        this.player = player;
    }


    public ArenaPlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
