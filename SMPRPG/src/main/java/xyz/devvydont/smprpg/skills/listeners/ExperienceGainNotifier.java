package xyz.devvydont.smprpg.skills.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.skills.SkillExperiencePostGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillLevelUpEvent;
import xyz.devvydont.smprpg.skills.SkillReward;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

public class ExperienceGainNotifier implements Listener {

    final SMPRPG plugin;

    public ExperienceGainNotifier(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLevelUpSkill(SkillLevelUpEvent event) {

        SkillType type = event.getSkillType();

        String oldLevel = String.valueOf(event.getOldLevel());
        String newLevel = String.valueOf(event.getNewLevel());
        event.getPlayer().sendMessage(Component.empty());
        event.getPlayer().sendMessage(ComponentUtil.getAlertMessage(ComponentUtil.getColoredComponent("SKILL LEVEL UP!!!", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true), NamedTextColor.AQUA));
        event.getPlayer().sendMessage(ComponentUtil.getDefaultText("--------------------------"));
        event.getPlayer().sendMessage(ComponentUtil.getColoredComponent("   " + type.getDisplayName() + " ", NamedTextColor.AQUA).append(ComponentUtil.getUpgradeComponent(oldLevel, newLevel, NamedTextColor.AQUA)));
        event.getPlayer().sendMessage(Component.empty());
        event.getPlayer().sendMessage(ComponentUtil.getColoredComponent("   Rewards:", NamedTextColor.GREEN));
        for (SkillReward reward : event.getSkill().getRewards(event.getNewLevel()))
            event.getPlayer().sendMessage(ComponentUtil.getDefaultText("    " + Symbols.POINT + " ").append(reward.getDisplayName()));
        event.getPlayer().sendMessage(ComponentUtil.getDefaultText("--------------------------"));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGainExperience(SkillExperiencePostGainEvent event) {

        // Send the player an action bar of their experience progress
        event.getPlayer().sendActionBar(
                ComponentUtil.getColoredComponent(event.getSkillType().getDisplayName() + " " + event.getSkill().getLevel(), NamedTextColor.AQUA)
                        .append(ComponentUtil.getDefaultText(" | "))
                        .append(Component.text(event.getSkill().getExperienceProgress()).color(NamedTextColor.GREEN))
                        .append(Component.text("/" + event.getSkill().getNextExperienceThreshold()).color(NamedTextColor.DARK_GRAY))
                        .append(Component.text(" (+" + event.getSkill().getCombo() + ")").color(NamedTextColor.GOLD))
        );
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .15f, 2);
    }

}
