package xyz.devvydont.smprpg.skills;

import org.bukkit.NamespacedKey;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.skills.rewards.*;
import xyz.devvydont.smprpg.skills.rewards.definitions.*;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public enum SkillType {

    COMBAT,       // STR, HP (+300%, +200)
    WOODCUTTING,  // STR, HP (+300%, +200)
    MINING,       // HP,  DEF (+200, +200)
    MAGIC,        // DEF, LUCK(+350, +50)
    FARMING,      // HP,  STR (+200, +200%)
    FISHING,      // DEF, LUCK(+350, +50)

    ;

    private NamespacedKey key = null;
    private final SkillRewardContainer rewards;

    SkillType() {
        this.rewards = generateRewardContainer(this);
    }

    public SkillRewardContainer getRewards() {
        return rewards;
    }

    public String getKey() {
        return "skill-" + this.name().toLowerCase();
    }

    public NamespacedKey getNamespacedKey() {
        if (key == null)
            key = new NamespacedKey(SMPRPG.getInstance(), getKey());

        return key;
    }

    public String getDisplayName() {
        return MinecraftStringUtils.getTitledString(this.name());
    }
    
    private static SkillRewardContainer generateRewardContainer(SkillType type) {
        SkillRewardContainer container = switch (type) {
            case COMBAT -> new CombatSkillRewards();
            case WOODCUTTING -> new WoodcuttingSkillRewards();
            case MINING -> new MiningSkillRewards();
            case MAGIC -> new MagicSkillRewards();
            case FARMING -> new FarmingSkillRewards();
            case FISHING -> new FishingSkillRewards();
        };
        container.initializeRewards();
        return container;
    }

}
