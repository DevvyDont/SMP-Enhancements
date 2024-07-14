package xyz.devvydont.smprpg.events.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;

public class SkillExperiencePostGainEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private boolean cancelled = false;
    private SkillExperienceGainEvent.ExperienceSource source;
    private int experienceEarned;
    private final SkillInstance skill;

    private final boolean leveledUp;

    public SkillExperiencePostGainEvent(SkillExperienceGainEvent.ExperienceSource source, int experienceEarned, SkillInstance skill, boolean leveledUp) {
        this.source = source;
        this.experienceEarned = experienceEarned;
        this.skill = skill;
        this.leveledUp = leveledUp;
    }

    public Player getPlayer() {
        return getSkill().getOwner();
    }

    public SkillExperienceGainEvent.ExperienceSource getSource() {
        return source;
    }

    public SkillType getSkillType() {
        return skill.getType();
    }

    public int getExperienceEarned() {
        return experienceEarned;
    }

    public SkillInstance getSkill() {
        return skill;
    }

    public boolean isLevelUp() {
        return leveledUp;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
