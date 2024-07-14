package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;

public class FishingExperienceListener implements Listener {

    final SMPRPG plugin;

    public FishingExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(PlayerFishEvent event) {

        if (event.isCancelled())
            return;

        // todo expand on this system some more
        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        player.getFishingSkill().addExperience(event.getExpToDrop() * 20, SkillExperienceGainEvent.ExperienceSource.FISH);
    }


}
