package xyz.devvydont.smprpg.skills.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillExperiencePostGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillLevelUpEvent;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.rewards.SkillReward;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

public class ExperienceGainNotifier implements Listener {

    final SMPRPG plugin;

    public ExperienceGainNotifier(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void messagePlayerSkillLevelUp(Player player, SkillInstance skill, int newLevel) {
        SkillType type = skill.getType();
        String oldLevel = String.valueOf(newLevel-1);
        String newLevelStr = String.valueOf(newLevel);
        player.sendMessage(Component.empty());
        player.sendMessage(ComponentUtils.getAlertMessage(ComponentUtils.getColoredComponent("SKILL LEVEL UP!!!", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true), NamedTextColor.AQUA));
        player.sendMessage(ComponentUtils.getDefaultText("--------------------------"));
        player.sendMessage(ComponentUtils.getColoredComponent("   " + type.getDisplayName() + " ", NamedTextColor.AQUA).append(ComponentUtils.getUpgradeComponent(oldLevel, newLevelStr, NamedTextColor.AQUA)));
        player.sendMessage(Component.empty());
        player.sendMessage(ComponentUtils.getColoredComponent("   Rewards:", NamedTextColor.GREEN));
        for (SkillReward reward : skill.getRewards(newLevel))
            player.sendMessage(ComponentUtils.getDefaultText("    " + Symbols.POINT + " ").append(reward.getDisplayName()));
        player.sendMessage(ComponentUtils.getDefaultText("--------------------------"));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
    }

    @EventHandler
    public void onLevelUpSkill(SkillLevelUpEvent event) {

        // Award the skills
        for (SkillReward reward : event.getSkill().getType().getRewards().getRewardsForLevels(event.getOldLevel()+1, event.getNewLevel()))
            reward.apply(event.getPlayer(), event.getSkillType());

        // Tell the player all their level ups
        int delay = 0;
        for (int level = event.getOldLevel()+1; level <= event.getNewLevel(); level++) {
            final int iter = level;
            new BukkitRunnable() {
                @Override
                public void run() {
                    messagePlayerSkillLevelUp(event.getPlayer(), event.getSkill(), iter);
                }
            }.runTaskLater(SMPRPG.getInstance(), delay);
            delay += 10;
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGainExperience(SkillExperiencePostGainEvent event) {

        if (event.getSource().equals(SkillExperienceGainEvent.ExperienceSource.XP))
            return;

        Component component = ComponentUtils.getColoredComponent(event.getSkillType().getDisplayName() + " " + event.getSkill().getLevel(), NamedTextColor.AQUA)
                .append(ComponentUtils.getDefaultText(" | "))
                .append(Component.text(MinecraftStringUtils.formatNumber(event.getSkill().getExperienceProgress())).color(NamedTextColor.GREEN))
                .append(Component.text("/" + MinecraftStringUtils.formatNumber(event.getSkill().getNextExperienceThreshold())).color(NamedTextColor.DARK_GRAY))
                .append(Component.text(" (+" + MinecraftStringUtils.formatNumber(event.getSkill().getCombo()) + ")").color(NamedTextColor.GOLD));

        // Send the player an action bar of their experience progress
        plugin.getActionBarService().addActionBarComponent(event.getPlayer(), ActionBarService.ActionBarSource.SKILL, component, 5);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .15f, 2);
    }

}
