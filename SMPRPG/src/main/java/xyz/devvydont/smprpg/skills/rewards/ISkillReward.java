package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import xyz.devvydont.smprpg.skills.SkillType;

/**
 * Represents a reward that can be obtained via leveling up a skill. Classes that implement this class should be able
 * to dynamically apply and remove itself from the player at any point, and generate a component to use in chat
 * when awarded.
 */
public interface ISkillReward {

    /**
     * Generates a chat component for a player when they obtain this reward.
     * @param player The player that earned this reward.
     * @return A component to use in chat.
     */
    Component generateRewardComponent(Player player);

    /**
     * Remove this reward from the player.
     * @param player The player to remove the reward from.
     * @param skill The type of skill this reward is associated with. This is necessary for stackable skill rewards.
     */
    void remove(Player player, SkillType skill);

    /**
     * Apply this reward to the player.
     * @param player The player to apply this reward to.
     * @param skill The type of skill this reward is associated with. This is necessary for stackable skill rewards.
     */
    void apply(Player player, SkillType skill);
}
