package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.services.EntityService;

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
        var player = SMPRPG.getService(EntityService.class).getPlayerInstance(event.getPlayer());
        player.getFishingSkill().addExperience(event.getExpToDrop() * 20, SkillExperienceGainEvent.ExperienceSource.FISH);
    }


}
