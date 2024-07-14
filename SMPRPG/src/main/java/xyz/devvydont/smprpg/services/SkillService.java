package xyz.devvydont.smprpg.services;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.skills.listeners.*;

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
        return false;
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

}
