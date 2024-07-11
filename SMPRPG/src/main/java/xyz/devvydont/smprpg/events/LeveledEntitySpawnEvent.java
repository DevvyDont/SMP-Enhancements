package xyz.devvydont.smprpg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;

public class LeveledEntitySpawnEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private LeveledEntity entity;

    public LeveledEntitySpawnEvent(LeveledEntity entity) {
        this.entity = entity;
    }

    public LeveledEntity getEntity() {
        return entity;
    }
}
