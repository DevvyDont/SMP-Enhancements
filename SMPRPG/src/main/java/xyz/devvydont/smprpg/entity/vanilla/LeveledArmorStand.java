package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.ArmorStand;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.skills.utils.SkillExperienceReward;

public class LeveledArmorStand extends VanillaEntity<ArmorStand> {

    public LeveledArmorStand(ArmorStand entity) {
        super(entity);
    }

    @Override
    public int getMinecraftExperienceDropped() {
        return 0;
    }

    @Override
    public SkillExperienceReward generateSkillExperienceReward() {
        return SkillExperienceReward.empty();
    }

    @Override
    public double getSkillExperienceMultiplier() {
        return 0;
    }

    @Override
    public void updateNametag() {
        super.updateNametag();
        _entity.setCustomNameVisible(false);
    }

    @Override
    public void setup() {
        super.setup();
    }

}
