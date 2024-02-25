package me.devvy.smpparkour.events;

import me.devvy.smpparkour.checkpoints.Checkpoint;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEnteredCheckpointEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private Checkpoint checkpoint;

    public PlayerEnteredCheckpointEvent(Player player, Checkpoint checkpoint) {
        this.player = player;
        this.checkpoint = checkpoint;
    }

    public Player getPlayer() {
        return player;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
