package xyz.devvydont.smprpg.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillLevelUpEvent;

public class HealthScaleListener implements Listener {

    private final SMPRPG plugin;

    public HealthScaleListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        new BukkitRunnable() {
            public void run() {
                event.getPlayer().setHealthScale(player.getHealthScale());
            }
        }.runTaskLater(plugin, 0);
    }

    @EventHandler
    public void onSkillLevelUp(SkillLevelUpEvent event) {
        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        event.getPlayer().setHealthScale(player.getHealthScale());
    }

}
