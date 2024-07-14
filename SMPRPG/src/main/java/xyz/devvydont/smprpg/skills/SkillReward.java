package xyz.devvydont.smprpg.skills;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class SkillReward {

    private final int level;

    public SkillReward(int level) {
        this.level = level;
    }

    public Component getDisplayName() {
        return ComponentUtil.getDefaultText("todo reward ").append(ComponentUtil.getUpgradeComponent(String.valueOf(level-1), String.valueOf(level), NamedTextColor.GREEN));
    }

}
