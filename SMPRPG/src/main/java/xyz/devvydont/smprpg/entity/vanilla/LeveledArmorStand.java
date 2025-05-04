package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.ArmorStand;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledArmorStand extends VanillaEntity<ArmorStand> {

    public LeveledArmorStand(ArmorStand entity) {
        super(entity);
    }

    @Override
    public int getMinecraftExperienceDropped() {
        return 0;
    }

    @Override
    public int getCombatExperienceDropped() {
        return 0;
    }

    @Override
    public double getCombatExperienceMultiplier() {
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
