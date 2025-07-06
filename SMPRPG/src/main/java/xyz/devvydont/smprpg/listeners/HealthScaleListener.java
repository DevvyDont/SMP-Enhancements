package xyz.devvydont.smprpg.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.skills.SkillLevelUpEvent;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;
import xyz.devvydont.smprpg.util.time.TickTime;

/**
 * Health scale is managed manually by us. Every time a player's health scale needs to be recalculated due to their
 * max HP potentially changing, update their health scale.
 */
public class HealthScaleListener extends ToggleableListener {

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        var plugin = SMPRPG.getInstance();
        var player = SMPRPG.getService(EntityService.class).getPlayerInstance(event.getPlayer());
        new BukkitRunnable() {
            public void run() {
                event.getPlayer().setHealthScale(player.getHealthScale());
            }
        }.runTaskLater(plugin, TickTime.INSTANTANEOUSLY);
    }

    @EventHandler
    public void onSkillLevelUp(SkillLevelUpEvent event) {
        var plugin = SMPRPG.getInstance();
        var player = SMPRPG.getService(EntityService.class).getPlayerInstance(event.getPlayer());
        event.getPlayer().setHealthScale(player.getHealthScale());
    }

}
