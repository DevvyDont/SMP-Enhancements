package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;

public class CombatExperienceListener implements Listener {

    final SMPRPG plugin;

    public CombatExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.isCancelled())
            return;

        // This entity did not have a player killer
        if (event.getEntity().getKiller() == null)
            return;

        LeveledEntity dead = plugin.getEntityService().getEntityInstance(event.getEntity());
        LeveledPlayer killer = plugin.getEntityService().getPlayerInstance(event.getEntity().getKiller());
        SkillInstance combat = killer.getCombatSkill();

        // Calculate how much base experience to drop, if there is none don't do anything
        int experience = dead.getCombatExperienceDropped();
        if (experience <= 0)
            return;

        // Add the experience
        combat.addExperience(experience, SkillExperienceGainEvent.ExperienceSource.KILL);
    }
}
