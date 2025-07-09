package xyz.devvydont.smprpg.skills;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillExperiencePostGainEvent;
import xyz.devvydont.smprpg.events.skills.SkillLevelUpEvent;
import xyz.devvydont.smprpg.skills.rewards.ISkillReward;

import java.util.Collection;

/**
 * Skill instances are helper instances to interface with skill modification on entities.
 * They handle things such as skill experience combos, and provide useful helper methods for
 * common experience calculations that can make recursive experience formulas easier to work with.
 * In most instances, you only really have to call {@link SkillInstance#addExperience(int, SkillExperienceGainEvent.ExperienceSource)}
 */
public class SkillInstance {

    private final Player owner;
    private final SkillType type;

    // Combo related things, certain effects could use these but mainly used as a display thing
    int combo = 0;
    long expireComboAt = 0;
    long comboLengthMs = 2000L;

    public SkillInstance(Player owner, SkillType type) {
        this.owner = owner;
        this.type = type;
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

    /**
     * Query how much total cumulative experience the owning player has.
     * @return The amount of experience.
     */
    public int getExperience() {
        return owner.getPersistentDataContainer().getOrDefault(type.getNamespacedKey(), PersistentDataType.INTEGER, 0);
    }

    /**
     * Set the total cumulative experience the owning player has.
     * @param experience The amount of experience to set.
     */
    public void setExperience(int experience) {
        owner.getPersistentDataContainer().set(type.getNamespacedKey(), PersistentDataType.INTEGER, experience);
    }

    /**
     * Add experience to the owning player. Automatically handles level up and event calling logic for you.
     * @param experience The amount of experience to add.
     * @param source The source of the experience.
     */
    public void addExperience(int experience, SkillExperienceGainEvent.ExperienceSource source) {

        // Construct an experience gain event. This allows listeners to modify the experience we are gaining.
        SkillExperienceGainEvent event = new SkillExperienceGainEvent(source, experience, this);
        event.callEvent();
        if (event.isCancelled() || event.getExperienceEarned() <= 0)
            return;

        // Add the experience and take note of what level we are before and after
        int oldLevel = getLevel();
        int expCap = SkillGlobals.getTotalExperienceCap();
        int newExp = getExperience() + event.getExperienceEarned();
        setExperience(Math.min(expCap, newExp));
        int newLevel = getLevel();

        // Combo increasing
        checkValidCombo();
        increaseCombo(event.getExperienceEarned());

        // Another event for post experience gains where no modifications can be made, but we have real XP values
        boolean leveledUp = oldLevel < newLevel;
        new SkillExperiencePostGainEvent(source, event.getExperienceEarned(), this, leveledUp).callEvent();

        // If we happened to increase our level, call another event for listeners to react to
        if (leveledUp)
            new SkillLevelUpEvent(this, oldLevel).callEvent();
    }

    /**
     * Add experience to the owning player. Automatically handles level up and event calling logic for you.
     * @param experience The amount of experience to add.
     */
    public void addExperience(int experience) {
        addExperience(experience, SkillExperienceGainEvent.ExperienceSource.UNKNOWN);
    }

    /**
     * Check what skill level the owning player is for this skill.
     * @return The skill level.
     */
    public int getLevel() {
        return SkillGlobals.getLevelForExperience(getExperience());
    }

    /**
     * Check what skill level is next. This is effectively the same as {@link SkillInstance#getLevel()} + 1.
     * @return The next level.
     */
    public int getNextLevel() {
        return getLevel() + 1;
    }

    /**
     * Calculates the experience gained for ONLY the current level. Any cumulative experience gained
     * from previous levels is ignored.
     * @return The amount of experience gained only for the current level.
     */
    public int getExperienceProgress() {
        return getExperience() - SkillGlobals.getCumulativeExperienceForLevel(getLevel());
    }

    /**
     * Check the cumulative experience threshold to reach the next level. This is a total experience check.
     * @return The amount of experience required (total!) to level up.
     */
    public int getNextExperienceThreshold() {
        return SkillGlobals.getExperienceForLevel(getNextLevel());
    }

    /**
     * Returns the amount of experience necessary to cause a level up.
     * @return An amount of experience.
     */
    public int getExperienceForNextLevel() {
        return SkillGlobals.getCumulativeExperienceForLevel(getNextLevel()) - getExperience();
    }

    /**
     * Checks if a certain experience reward will trigger a level up.
     * @param experience The experience to check.
     * @return True if this player will level up from the experience.
     */
    public boolean willLevelUp(int experience) {
        return experience >= getExperienceForNextLevel();
    }

    /**
     * Retrieve the rewards that a certain skill level will grant.
     * @param level The level to check rewards for.
     * @return A collection of rewards.
     */
    public Collection<ISkillReward> getRewards(int level) {
        return getType().getRewards().getRewardsForLevel(level);
    }

}
