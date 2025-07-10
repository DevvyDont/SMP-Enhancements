package xyz.devvydont.smprpg.skills.utils;

import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillType;

import java.util.HashMap;
import java.util.Map;

/**
 * Used as a way to contain multiple sources of skill experience in one.
 */
public class SkillExperienceReward {

    public static SkillExperienceReward empty() {
        return new SkillExperienceReward();
    }

    /**
     * Call when you want a simple experience reward for one experience type.
     * @param type The type of skill experience.
     * @param amount The amount of skill experience.
     * @return A built skill experience object.
     */
    public static SkillExperienceReward of(SkillType type, int amount) {
        return new SkillExperienceReward().add(type, amount);
    }

    /**
     * Call when you want multiple skills to be awarded with the same amount.
     * @param amount The amount of skill experience.
     * @param types All the skills to get experience.
     * @return A built skill experience object.
     */
    public static SkillExperienceReward withMultipleSkills(int amount, SkillType...types) {
        var instance = new SkillExperienceReward();

        for (SkillType type : types)
            instance.add(type, amount);

        return instance;
    }

    private final Map<SkillType, Integer> amounts = new HashMap<>();

    public SkillExperienceReward add(SkillType type, int amount) {
        amounts.put(type, Math.max(0, amount));
        return this;
    }

    /**
     * Shortcut method to apply all the skill experience in this container to a player.
     * @param player The player to apply skill experience to. Can be called multiple times.
     */
    public void apply(LeveledPlayer player, SkillExperienceGainEvent.ExperienceSource source) {
        for (var skill : player.getSkills())
            if (amounts.containsKey(skill.getType()))
                skill.addExperience(amounts.get(skill.getType()), source);
    }

    public boolean isEmpty() {
        if (amounts.isEmpty())
            return true;

        for (var amount : amounts.values())
            if (amount > 0)
                return false;

        return true;
    }

    public void multiply(double multiplier) {
        for (var entry : amounts.entrySet())
            add(entry.getKey(), (int) (entry.getValue() * multiplier));
    }
}
