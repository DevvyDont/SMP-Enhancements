package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledWither extends VanillaEntity implements Listener {

    public LeveledWither(SMPRPG plugin, LivingEntity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 50;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 450;
    }

    @Override
    public double calculateBaseHealth() {
        return 40000;
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return 1.0;
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return 1.0;
    }
}
