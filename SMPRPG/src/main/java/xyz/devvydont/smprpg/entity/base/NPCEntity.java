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
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;

public abstract class NPCEntity extends CustomEntityInstance implements Listener {

    public NPCEntity(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    public abstract void handleInteract(Player player);

    @Override
    public void setup() {
        super.setup();
        if (entity instanceof LivingEntity living)
            living.setAI(false);
        entity.setInvulnerable(true);
        entity.setPersistent(true);
        entity.setSilent(true);
        brightenNametag();
    }

    /**
     * NPCs must have a name override.
     *
     * @return
     */
    @Override
    public abstract String getDefaultName();

    @Override
    public TextColor getEntityNametagColor() {
        return NamedTextColor.WHITE;
    }

    /**
     * NPCs don't have power levels.
     *
     * @return
     */
    @Override
    public Component getNametagPowerComponent() {
        return Component.text("[").color(NamedTextColor.GRAY)
                .append(Component.text("NPC").color(NamedTextColor.YELLOW))
                .append(Component.text("] ").color(NamedTextColor.GRAY));
    }

    /**
     * NPCs don't have health displays.
     *
     * @return
     */
    @Override
    public Component getHealthNametagComponent() {
        return Component.empty();
    }

    @EventHandler
    public void onInteractWithNPC(PlayerInteractEntityEvent event) {

        // Only listen if it is this entity
        if (!entity.equals(event.getRightClicked()))
            return;

        event.setCancelled(true);
        handleInteract(event.getPlayer());
    }
}
