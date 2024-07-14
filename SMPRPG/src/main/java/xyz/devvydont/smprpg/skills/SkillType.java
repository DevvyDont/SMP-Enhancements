package xyz.devvydont.smprpg.skills;

import org.bukkit.NamespacedKey;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public enum SkillType {

    COMBAT,       // strength, defense
    WOODCUTTING,  // strength, health
    MINING,       // defense,  strength
    MAGIC,        // defense,  luck
    FARMING,      // health,   strength
    FISHING,      // health,   luck

    ;

    private NamespacedKey key = null;

    public String getKey() {
        return "skill-" + this.name().toLowerCase();
    }

    public NamespacedKey getNamespacedKey() {
        if (key == null)
            key = new NamespacedKey(SMPRPG.getInstance(), getKey());

        return key;
    }

    public String getDisplayName() {
        return MinecraftStringUtils.getTitledString(this.name().toLowerCase());
    }

}
