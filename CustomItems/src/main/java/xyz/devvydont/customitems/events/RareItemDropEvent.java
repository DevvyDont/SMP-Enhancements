package me.devvy.customitems.events;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RareItemDropEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Item item;
    private final float chance;
    private String reason;

    public RareItemDropEvent(Player player, Item item, float chance, String reason) {
        this.player = player;
        this.item = item;
        this.chance = chance;
        this.reason = reason;
    }

    public Player getPlayer() {
        return player;
    }

    public float getChance() {
        return chance;
    }

    public Item getItem() {
        return item;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
