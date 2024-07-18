package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import xyz.devvydont.smprpg.skills.SkillType;

public abstract class SkillReward {

    private final int level;

    public SkillReward(int level) {
        this.level = level;
    }

    public abstract Component getDisplayName();

    public int getLevel() {
        return level;
    }

    public void remove(Player player, SkillType skill) {

    }

    public void apply(Player player, SkillType skill) {

    }
}
