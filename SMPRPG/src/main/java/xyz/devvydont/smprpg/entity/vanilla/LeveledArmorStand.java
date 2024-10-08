package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Entity;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledArmorStand extends VanillaEntity {

    public LeveledArmorStand(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
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
        entity.setCustomNameVisible(false);
    }

    @Override
    public int getDefaultLevel() {
        return 1;
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public double calculateBaseHealth() {
        return 5;
    }
}
