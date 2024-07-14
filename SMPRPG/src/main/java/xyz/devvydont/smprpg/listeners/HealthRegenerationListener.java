package xyz.devvydont.smprpg.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;

public class HealthRegenerationListener implements Listener {

    final SMPRPG plugin;

    public HealthRegenerationListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

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

        LeveledEntity entity = plugin.getEntityService().getEntityInstance((LivingEntity) event.getEntity());
        event.setAmount(entity.getRegenerationAmount(event.getRegainReason()));
    }
}
