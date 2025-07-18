package xyz.devvydont.smprpg.skills.rewards;

import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.skills.SkillGlobals;

import java.util.*;

public abstract class SkillRewardContainer {

    private final Map<Integer, List<ISkillReward>> skillRewards = new HashMap<>();

    public abstract void initializeRewards();

    public Collection<ISkillReward> getRewardsForLevel(int level) {
        return skillRewards.getOrDefault(level, new ArrayList<>());
    }

    public Collection<ISkillReward> getRewardsForLevels(int start, int end) {
        ArrayList<ISkillReward> result = new ArrayList<>();
        for (int i = start; i <= end; i++)
            result.addAll(getRewardsForLevel(i));
        return result;
    }

    public Collection<ISkillReward> getRewards() {
        return getRewardsForLevels(1, SkillGlobals.getMaxSkillLevel());
    }

    protected void addReward(int level, ISkillReward reward) {
        List<ISkillReward> rewards = skillRewards.getOrDefault(level, new ArrayList<>());
        rewards.add(reward);
        skillRewards.put(level, rewards);
    }

    /**
     * Shortcut method to add an attribute that linearly adds up depending on the level.
     * @param attribute The attribute to award.
     * @param operation The attribute operation to use.
     * @param amountPerLevel The linear amount of the attribute to add per level.
     */
    protected void addAttributeRewardEveryLevel(AttributeWrapper attribute, AttributeModifier.Operation operation, double amountPerLevel) {
        for (var i = 1; i <= SkillGlobals.getMaxSkillLevel(); i++)
            addReward(i, new AttributeReward(
                    attribute,
                    operation,
                    amountPerLevel * i,
                    amountPerLevel * (i-1)
            ));
    }

    /**
     * Shortcut method to add an attribute that linearly adds up depending on the level.
     * @param attribute The attribute to award.
     * @param operation The attribute operation to use.
     * @param amountPerLevel The linear amount of the attribute to add per level.
     * @param x The every xth level you want to add the attribute to.
     */
    protected void addAttributeRewardEveryXLevels(AttributeWrapper attribute, AttributeModifier.Operation operation, double amountPerLevel, int x) {
        for (var i = x; i <= SkillGlobals.getMaxSkillLevel(); i+=x)
            addReward(i, new AttributeReward(
                    attribute,
                    operation,
                    amountPerLevel * ((double) i /x),
                    amountPerLevel * ((double) (i-x) /x)
            ));
    }

    /**
     * Shortcut method to add a scaling attribute to every level of this container.
     * @param attribute The attribute you want to modify.
     * @param operation The operation you want to use.
     * @param amount The base stat amount. Keep in mind this scales as you get higher.
     */
    protected void addScalingAttributeRewardEveryLevel(AttributeWrapper attribute, AttributeModifier.Operation operation, double amount) {
        for (var i = 1; i <= SkillGlobals.getMaxSkillLevel(); i++)
            addReward(i, new AttributeReward(
                    attribute,
                    operation,
                    SkillGlobals.getScalingStatPerLevel(amount, i),
                    SkillGlobals.getScalingStatPerLevel(amount, i-1)
            ));
    }

    /**
     * Shortcut method to add a scaling attribute to every certain level of this container.
     * @param attribute The attribute you want to modify.
     * @param operation The operation you want to use.
     * @param amount The base stat amount. Keep in mind this scales as you get higher.
     * @param x How many levels to skip per entry. If x is 4, the attribute will be rewarded every 4 levels starting at 4.
     */
    protected void addScalingAttributeRewardEveryXLevels(AttributeWrapper attribute, AttributeModifier.Operation operation, double amount, int x) {
        for (var i = x; i <= SkillGlobals.getMaxSkillLevel(); i += x)
            addReward(i, new AttributeReward(
                    attribute,
                    operation,
                    SkillGlobals.getScalingStatPerXLevel(amount, x, i),
                    SkillGlobals.getScalingStatPerXLevel(amount, x, i-x)
            ));
    }

    /**
     * Shortcut method to add a coin reward for every level. Currently, this is the same for every skill, it just needs
     * to be called.
     */
    protected void addCoinsEveryLevel() {
        for (var i = 1; i <= SkillGlobals.getMaxSkillLevel(); i++)
            addReward(i, new CoinReward(SkillGlobals.getCoinRewardForLevel(i)));
    }
}
