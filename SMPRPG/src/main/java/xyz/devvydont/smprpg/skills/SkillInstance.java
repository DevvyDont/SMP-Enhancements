package xyz.devvydont.smprpg.skills;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillExperiencePostGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillLevelUpEvent;

import java.util.Collection;
import java.util.List;

public class SkillInstance {

    private Player owner;
    private SkillType type;

    // Combo related things, certain effects could use these but mainly used as a display thing
    int combo = 0;
    long expireComboAt = 0;
    long comboLengthMs = 2000L;

    public SkillInstance(Player owner, SkillType type) {
        this.owner = owner;
        this.type = type;
    }

    public void expireCombo() {
        combo = 0;
    }

    public void checkValidCombo() {

        // Combo is valid if the expiry time is larger than the current time
        if (expireComboAt > System.currentTimeMillis())
            return;

        combo = 0;
    }

    public void increaseCombo(int amount) {
        combo += amount;
        expireComboAt = System.currentTimeMillis() + comboLengthMs;
    }

    public int getCombo() {
        checkValidCombo();
        return combo;
    }

    public Player getOwner() {
        return owner;
    }

    public SkillType getType() {
        return type;
    }

    public int getExperience() {
        return owner.getPersistentDataContainer().getOrDefault(type.getNamespacedKey(), PersistentDataType.INTEGER, 0);
    }

    public void setExperience(int experience) {
        owner.getPersistentDataContainer().set(type.getNamespacedKey(), PersistentDataType.INTEGER, experience);
    }

    public void addExperience(int experience, SkillExperienceGainEvent.ExperienceSource source) {

        // Construct an experience gain event. This allows listeners to modify the experience we are gaining.
        SkillExperienceGainEvent event = new SkillExperienceGainEvent(source, experience, this);
        event.callEvent();
        if (event.isCancelled() || event.getExperienceEarned() <= 0)
            return;

        // Add the experience and take note of what level we are before and after
        int oldLevel = getLevel();
        setExperience(getExperience() + event.getExperienceEarned());
        int newLevel = getLevel();

        // Combo increasing
        checkValidCombo();
        increaseCombo(event.getExperienceEarned());

        // Another event for post experience gains where no modifications can be made but we have real XP values
        boolean leveledUp = oldLevel < newLevel;
        new SkillExperiencePostGainEvent(source, event.getExperienceEarned(), this, leveledUp).callEvent();

        // If we happened to increase our level, call another event for listeners to react to
        if (leveledUp)
            new SkillLevelUpEvent(this).callEvent();
    }

    public void addExperience(int experience) {
        addExperience(experience, SkillExperienceGainEvent.ExperienceSource.UNKNOWN);
    }

    public int getLevel() {
        return SkillGlobals.getLevelForExperience(getExperience());
    }

    public int getNextLevel() {
        return getLevel() + 1;
    }

    /**
     * Gets the experience progress for this level. Ignores experience gained for prior levels
     *
     * @return
     */
    public int getExperienceProgress() {
        return getExperience() - SkillGlobals.getCumulativeExperienceForLevel(getLevel());
    }

    /**
     * Use this to determine how much experience is needed to level up, but do not use the experience from prior levels
     *
     * @return
     */
    public int getNextExperienceThreshold() {
        return SkillGlobals.getExperienceForLevel(getNextLevel());
    }

    /**
     * Returns the experience required to level up to the next level
     *
     * @return
     */
    public int getExperienceForNextLevel() {
        return SkillGlobals.getCumulativeExperienceForLevel(getNextLevel()) - getExperience();
    }

    /**
     * Given an amount of experience, determine if this will be enough to level up
     *
     * @param experience
     * @return
     */
    public boolean willLevelUp(int experience) {
        return experience >= getExperienceForNextLevel();
    }

    public Collection<SkillReward> getRewards(int level) {
        return List.of(new SkillReward(level), new SkillReward(level));  // todo implement
    }




}
