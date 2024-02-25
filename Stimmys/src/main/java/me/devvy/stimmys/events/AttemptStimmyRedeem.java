package me.devvy.stimmys.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AttemptStimmyRedeem extends Event implements Cancellable {


    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled = false;
    private Player player;
    private String reason = "You cannot redeem Stimmies right now!";

    public AttemptStimmyRedeem(Player player){
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Player getPlayer() {
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
