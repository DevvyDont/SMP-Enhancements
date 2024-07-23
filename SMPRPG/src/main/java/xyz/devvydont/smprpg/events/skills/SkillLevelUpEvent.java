package xyz.devvydont.smprpg.events.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.skills.SkillType;

public class SkillLevelUpEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    private SkillExperienceGainEvent.ExperienceSource source;
    private final SkillInstance skill;
    private final int oldLevel;

    public SkillLevelUpEvent(SkillInstance skill, int oldLevel) {
        this.source = source;
        this.skill = skill;
        this.oldLevel = oldLevel;
    }

    public Player getPlayer() {
        return getSkill().getOwner();
    }

    public SkillType getSkillType() {
        return skill.getType();
    }

    public SkillInstance getSkill() {
        return skill;
    }

    public int getNewLevel() {
        return skill.getLevel();
    }

    public int getOldLevel() {
        return oldLevel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }


}
