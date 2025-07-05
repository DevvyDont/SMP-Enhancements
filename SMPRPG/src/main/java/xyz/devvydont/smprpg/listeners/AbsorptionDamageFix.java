package xyz.devvydont.smprpg.listeners;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.damage.AbsorptionDamageDealtEvent;

public class AbsorptionDamageFix implements Listener {

    final SMPRPG plugin;

    public AbsorptionDamageFix(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void crackEntityArmor(LivingEntity entity) {
        entity.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, entity.getEyeLocation(), 20);
        entity.getWorld().playSound(entity.getEyeLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1.5f);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        var entity = plugin.getEntityService().getEntityInstance(event.getEntity());

        // If they don't have absorption don't do anything
        if (entity.getAbsorptionHealth() <= 0)
            return;

        // Subtract absorption.
        entity.addAbsorptionHealth(-event.getDamage());

        // Check if they ran out.
        var cracked = entity.getAbsorptionHealth() <= 0;
        if (cracked)
            crackEntityArmor(living);

        // Make the original damage event do no damage.
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, 0.01);

        // Announce.
        var absorbDmgEvent = new AbsorptionDamageDealtEvent(living, event.getDamage(), cracked);
        absorbDmgEvent.callEvent();
    }

}
