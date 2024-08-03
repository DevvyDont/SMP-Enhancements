package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledDragon extends VanillaEntity implements Listener {

    public LeveledDragon(SMPRPG plugin, LivingEntity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 50;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 2000;
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return 1.0;
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return 1.0;
    }

    @Override
    public double calculateBaseHealth() {
        return 300_000;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDragonBreathDamage(EntityDamageByEntityEvent e) {

        // Dragon breath damage is treated as a normal attack
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        // Check for area of effect damage
        if (!(e.getDamager() instanceof AreaEffectCloud cloud))
            return;

        // Check if the owner of the cloud matches this dragon
        if (!entity.getUniqueId().equals(cloud.getOwnerUniqueId()))
            return;

        // Set the damage
        e.setDamage(getBaseAttackDamage());
    }

}
