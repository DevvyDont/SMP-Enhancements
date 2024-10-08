package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.EnemyEntity;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;

import java.util.Map;

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
        if (!(dead instanceof EnemyEntity enemy))
            return;

        // Calculate how much base experience to drop, if there is none don't do anything
        int experience = dead.getCombatExperienceDropped();
        if (experience <= 0)
            return;

        // Loop through everyone who helped kill this entity
        for (Map.Entry<Player, Integer> entry : enemy.getPlayerDamageTracker().entrySet()) {

            // Calculate a percentage of how much damage the player did to the entity
            float percentage = (float) Math.min(1.0f, entry.getValue() / dead.getMaxHp());
            // Take that percentage and multiply it by 5 for generosity
            int experienceCut = (int) Math.min(experience, experience * percentage * 5);

            LeveledPlayer player = plugin.getEntityService().getPlayerInstance(entry.getKey());
            player.getCombatSkill().addExperience(experienceCut, SkillExperienceGainEvent.ExperienceSource.KILL);
        }

    }
}
