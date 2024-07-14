package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledElderGuardian extends VanillaEntity implements Listener {

    public LeveledElderGuardian(SMPRPG plugin, LivingEntity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 30;
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return 1.0;
    }

    @Override
    public double calculateBaseHealth() {
        return 25000;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 400;
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return 1.0;
    }

    @Override
    public TextColor getEntityNametagColor() {
        return NamedTextColor.DARK_PURPLE;
    }
}
