package xyz.devvydont.smprpg.entity.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public abstract class NPCEntity<T extends LivingEntity> extends CustomEntityInstance<T> implements Listener {

    public NPCEntity(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public NPCEntity(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public abstract void handleInteract(Player player);

    @Override
    public void setup() {
        super.setup();
        getEntity().setAI(false);
        _entity.setInvulnerable(true);
        _entity.setPersistent(true);
        _entity.setSilent(true);
        brightenNametag();
    }

    /**
     * NPCs must have a name override.
     *
     * @return
     */
    @Override
    public abstract String getEntityName();

    @Override
    public TextColor getNameColor() {
        return NamedTextColor.WHITE;
    }

    /**
     * NPCs don't have power levels.
     *
     * @return
     */
    @Override
    public Component getPowerComponent() {
        return ComponentUtils.EMPTY
            .append(ComponentUtils.SYMBOL_BRACKET_LEFT)
            .append(ComponentUtils.create("NPC", NamedTextColor.YELLOW))
            .append(ComponentUtils.SYMBOL_BRACKET_RIGHT);
    }

    /**
     * NPCs don't have health displays.
     *
     * @return
     */
    @Override
    public Component getHealthComponent() {
        return ComponentUtils.EMPTY;
    }

    @EventHandler
    public void onInteractWithNPC(PlayerInteractEntityEvent event) {

        // Only listen if it is this entity
        if (!_entity.equals(event.getRightClicked()))
            return;

        event.setCancelled(true);
        handleInteract(event.getPlayer());
    }
}
