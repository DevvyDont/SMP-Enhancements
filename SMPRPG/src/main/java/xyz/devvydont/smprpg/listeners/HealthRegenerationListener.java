package xyz.devvydont.smprpg.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

/**
 * Makes health regeneration work correctly. In normal minecraft, HP regen is usually always 1 hp. We need regeneration
 * to scale based on how much health someone has. As a bonus, we also utilize a new "regeneration" attribute to
 * determine its effectiveness.
 */
public class HealthRegenerationListener extends ToggleableListener {

    public boolean isNaturalRegeneration(EntityRegainHealthEvent.RegainReason regainReason) {
        return switch (regainReason) {
            case REGEN, SATIATED, MAGIC_REGEN -> true;
            default -> false;
        };
    }

    @EventHandler
    public void onNaturalRegeneration(EntityRegainHealthEvent event) {

        if (!isNaturalRegeneration(event.getRegainReason()))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        var entity = SMPRPG.getService(EntityService.class).getEntityInstance(event.getEntity());
        event.setAmount(entity.getRegenerationAmount(event.getRegainReason()));
    }
}
