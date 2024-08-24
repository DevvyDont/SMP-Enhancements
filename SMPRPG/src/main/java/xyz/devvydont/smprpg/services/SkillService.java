package xyz.devvydont.smprpg.services;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.skills.listeners.*;
import xyz.devvydont.smprpg.skills.rewards.ProgressiveAttributeReward;
import xyz.devvydont.smprpg.skills.rewards.SkillReward;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

public class SkillService implements BaseService, Listener {

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
        for (int i = 1; i <= 99; i++) {
            int xp = SkillGlobals.getExperienceForLevel(i);
            sum += xp;
            plugin.getLogger().fine("Skill Requirement for Level " + i + ": " + xp + " (" + sum + ")");
        }

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

    public void syncSkillAttributes(LeveledPlayer player) {

        // Very hacky, but loops through every attribute type, generates a fake attribute reward for every skill type
        // and uses the key generated from that skill and attribute combo to remove any attribute modifiers
        // currently present. Afterwards, we can then safely apply attribute rewards
        for (AttributeWrapper attribute : AttributeWrapper.values()) {
            AttributeInstance attributeInstance = player.getPlayer().getAttribute(attribute.getAttribute());
            if (attributeInstance == null)
                continue;

            for (SkillType skillType : SkillType.values())
                attributeInstance.removeModifier(new ProgressiveAttributeReward(1, attribute, 1).getModifierKey(skillType));
        }

        // Loop through every skill, then every level they have in that skill, and all the rewards per level and re-apply it if it is an attribute reward
        for (SkillInstance skill : player.getSkills())
            for (int level = 0; level <= skill.getLevel(); level++)
                for (SkillReward reward : skill.getRewards(level))
                    if (reward instanceof ProgressiveAttributeReward)
                        reward.apply(player.getPlayer(), skill.getType());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        syncSkillAttributes(plugin.getEntityService().getPlayerInstance(event.getPlayer()));
    }
}
