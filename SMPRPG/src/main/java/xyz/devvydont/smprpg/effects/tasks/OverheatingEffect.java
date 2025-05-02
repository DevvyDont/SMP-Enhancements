package xyz.devvydont.smprpg.effects.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

/**
 * An effect that if is timed out, it will remove fire resistance from the entity and damage them
 * for half the health they currently have.
 */
public class OverheatingEffect extends SpecialEffectTask {

    public OverheatingEffect(SpecialEffectService service, Player player, int seconds) {
        super(service, player, seconds);
    }

    @Override
    public Component getExpiredComponent() {
        return ComponentUtils.create("SCORCHED!", NamedTextColor.DARK_RED);
    }

    @Override
    public Component getNameComponent() {
        return ComponentUtils.create("Overheating!", NamedTextColor.RED);
    }

    @Override
    public TextColor getTimerColor() {
        return NamedTextColor.RED;
    }

    @Override
    protected void tick() {

        if (_ticks % 10 != 0)
            return;

        if (seconds <= 0)
            return;

        _player.damage(_player.getHealth() / 10, DamageSource.builder(DamageType.MAGIC).build());
        _player.setNoDamageTicks(0);
        _player.getWorld().playSound(_player, Sound.ENTITY_BLAZE_HURT, 1, 2);
        _player.getWorld().spawnParticle(Particle.ASH, _player.getEyeLocation(), 10);
    }

    @Override
    protected void expire() {

        // When the timer runs out, remove fire resistance and damage them for quite a bit.
        var fireRes = _player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE);
        if (fireRes != null)
            _player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, fireRes.getDuration(), fireRes.getAmplifier()));
        _player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        _player.damage(_player.getHealth() / 2, DamageSource.builder(DamageType.MAGIC).build());
        _player.setNoDamageTicks(0);
        _player.getWorld().playSound(_player, Sound.ENTITY_BLAZE_DEATH, 1, 2);
        _player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, _player.getEyeLocation(), 25);
    }

    @Override
    public void removed() {
        // Nothing happens :)
    }
}
