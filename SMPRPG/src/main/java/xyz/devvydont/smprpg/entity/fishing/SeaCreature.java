package xyz.devvydont.smprpg.entity.fishing;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.LivingEntity;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.skills.utils.SkillExperienceReward;

public class SeaCreature<T extends LivingEntity> extends CustomEntityInstance<T> {

    public static final TextColor NAME_COLOR = TextColor.color(0x3FD6FF);

    /**
     * An unsafe constructor to use to allow dynamic creation of custom entities.
     * This is specifically used as a casting hack for the CustomEntityType enum in order to dynamically create
     * entities.
     *
     * @param entity     The entity that should map the T type parameter.
     * @param entityType The entity type.
     */
    public SeaCreature(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public TextColor getNameColor() {
        return NAME_COLOR;
    }

    @Override
    public double getSkillExperienceMultiplier() {
        return 1.0;
    }

    @Override
    public SkillExperienceReward generateSkillExperienceReward() {
        return SkillExperienceReward.of(SkillType.FISHING, (int) (getLevel() * 500 * getSkillExperienceMultiplier()));
    }
}
