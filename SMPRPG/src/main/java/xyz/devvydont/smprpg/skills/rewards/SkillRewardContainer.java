package xyz.devvydont.smprpg.skills.rewards;

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


}
