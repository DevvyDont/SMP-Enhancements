package xyz.devvydont.smprpg.skills;

import xyz.devvydont.smprpg.skills.rewards.SkillReward;

import java.util.*;

public abstract class SkillRewardContainer {

    private final Map<Integer, List<SkillReward>> skillRewards = new HashMap<>();

    public abstract void initializeRewards();

    public Collection<SkillReward> getRewardsForLevel(int level) {
        return skillRewards.getOrDefault(level, new ArrayList<>());
    }

    public Collection<SkillReward> getRewardsForLevels(int start, int end) {
        ArrayList<SkillReward> result = new ArrayList<>();
        for (int i = start; i <= end; i++)
            result.addAll(getRewardsForLevel(i));
        return result;
    }

    public Collection<SkillReward> getRewards() {
        return getRewardsForLevels(1, 99);
    }

    protected void addReward(SkillReward reward) {
        List<SkillReward> rewards = skillRewards.getOrDefault(reward.getLevel(), new ArrayList<>());
        rewards.add(reward);
        skillRewards.put(reward.getLevel(), rewards);
    }


}
