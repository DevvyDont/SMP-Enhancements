package xyz.devvydont.smprpg.services;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.skills.listeners.*;
import xyz.devvydont.smprpg.skills.rewards.AttributeReward;
import xyz.devvydont.smprpg.skills.rewards.ISkillReward;

public class SkillService implements IService, Listener {

    private final SMPRPG plugin;

    public SkillService(SMPRPG plugin) {
        this.plugin = plugin;

        new CombatExperienceListener(plugin);
        new MiningExperienceListener(plugin);
        new ForagingExperienceListener(plugin);
        new FarmingExperienceListener(plugin);
        new MagicExperienceListener(plugin);
        new FishingExperienceListener(plugin);

        new ExperienceGainNotifier(plugin);
    }

    @Override
    public boolean setup() {

        int sum = 0;
        for (int i = 1; i <= SkillGlobals.getMaxSkillLevel(); i++) {
            int xp = SkillGlobals.getExperienceForLevel(i);
            sum += xp;
            plugin.getLogger().fine("Skill Requirement for Level " + i + ": " + xp + " (" + sum + ")");
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        return true;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public boolean required() {
        return false;
    }

    public SkillInstance getNewSkillInstance(Player player, SkillType type) {
        return new SkillInstance(player, type);
    }

    private void removeAttributeSkillRewards(LeveledPlayer player) {
        // Remove every skill reward that is an attribute skill.
        for (SkillInstance skill : player.getSkills())
            for (int level = 0; level <= SkillGlobals.getMaxSkillLevel(); level++)
                for (ISkillReward reward : skill.getRewards(level))
                    if (reward instanceof AttributeReward)
                        reward.remove(player.getPlayer(), skill.getType());
    }

    private void applyAttributeSkillRewards(LeveledPlayer player) {
        for (SkillInstance skill : player.getSkills())
            for (int level = 0; level <= skill.getLevel(); level++)
                for (ISkillReward reward : skill.getRewards(level))
                    if (skill.getLevel() >= level && reward instanceof AttributeReward)
                        reward.apply(player.getPlayer(), skill.getType());
    }

    public void syncSkillAttributes(LeveledPlayer player) {

        // We want to maintain their HP% when we perform this.
        double hpPercent = Math.max(.01, player.getHealthPercentage());

        removeAttributeSkillRewards(player);

        // Loop through every skill, then every level they have in that skill, and apply the reward.
        applyAttributeSkillRewards(player);

        // Reset their health.
        if (!player.getPlayer().isDead())
            player.setHealthPercentage(hpPercent);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onPlayerJoin(PlayerJoinEvent event) {
        syncSkillAttributes(plugin.getEntityService().getPlayerInstance(event.getPlayer()));
    }
}
