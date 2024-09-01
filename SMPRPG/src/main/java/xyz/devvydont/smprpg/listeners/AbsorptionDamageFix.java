package xyz.devvydont.smprpg.listeners;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.util.formatting.DamagePopupUtil;

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

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity living = (LivingEntity) event.getEntity();
        LeveledEntity entity = plugin.getEntityService().getEntityInstance(event.getEntity());

        // If they don't have absorption don't do anything
        if (entity.getAbsorptionHealth() <= 0)
            return;

        DamagePopupUtil.spawnTextPopup(living.getEyeLocation(), (int) event.getDamage(), DamagePopupUtil.PopupType.DAMAGE_ARMOR);
        entity.updateAbsorptionHealth(-event.getDamage());
        if (entity.getAbsorptionHealth() <= 0)
            crackEntityArmor(living);
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, 0.01);
    }

}
