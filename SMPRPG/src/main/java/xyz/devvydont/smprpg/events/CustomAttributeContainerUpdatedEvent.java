package xyz.devvydont.smprpg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.CustomAttributeContainer;

public class CustomAttributeContainerUpdatedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private final PersistentDataHolder holder;
    private final CustomAttributeContainer container;

    public CustomAttributeContainerUpdatedEvent(PersistentDataHolder holder, CustomAttributeContainer container) {
        this.holder = holder;
        this.container = container;
    }

    public PersistentDataHolder getHolder() {
        return holder;
    }

    public CustomAttributeContainer getContainer() {
        return container;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }


}
