package xyz.devvydont.smprpg.events.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;

public class SkillExperienceGainEvent extends Event implements Cancellable {

    public enum ExperienceSource {

        KILL,

        ORE,
        SMELT,

        HARVEST,
        TAME,
        FEED,
        LOOT,

        FISH,

        ENCHANT,
        BREW,
        FORGE,
        XP,

        UNKNOWN
    }

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private boolean cancelled = false;
    private ExperienceSource source;
    private int experienceEarned;
    private final SkillInstance skill;

    public SkillExperienceGainEvent(ExperienceSource source, int experienceEarned, SkillInstance skill) {
        this.source = source;
        this.experienceEarned = experienceEarned;
        this.skill = skill;
    }

    private void validateExperience() {
        experienceEarned = Math.max(0, experienceEarned);
    }


    public Player getPlayer() {
        return getSkill().getOwner();
    }

    public ExperienceSource getSource() {
        return source;
    }

    public SkillType getSkillType() {
        return skill.getType();
    }

    public void setSource(ExperienceSource source) {
        this.source = source;
    }

    public int getExperienceEarned() {
        return experienceEarned;
    }

    public void setExperienceEarned(int experienceEarned) {
        this.experienceEarned = experienceEarned;
        validateExperience();
    }

    public void addExperienceEarned(int experience) {
        experienceEarned += experience;
        validateExperience();
    }

    public void multiplyExperienceEarned(double multiplier) {
        setExperienceEarned((int) (getExperienceEarned() * multiplier));
        validateExperience();
    }

    public SkillInstance getSkill() {
        return skill;
    }

    public boolean isLevelUp() {
        return skill.willLevelUp(getExperienceEarned());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }




}
