package xyz.devvydont.smprpg.effects.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.particles.ParticleUtil;

public class TetheredEffect extends SpecialEffectTask implements Listener {

    // How many ticks do we need to break LOS to break the tether?
    public static final int LOS_BREAK_TICKS = 10;

    private final LivingEntity tetheredTo;
    private int currentlyBrokenLOSTicks = 0;

    public TetheredEffect(SpecialEffectService service, Player player, LivingEntity tetheredTo, int seconds) {
        super(service, player, seconds);
        this.tetheredTo = tetheredTo;
    }

    @Override
    public Component getExpiredComponent() {
        return ComponentUtils.create("SEVERED!", NamedTextColor.RED);
    }

    @Override
    public Component getNameComponent() {
        return ComponentUtils.create("Tethered!", NamedTextColor.GOLD);
    }

    @Override
    public TextColor getTimerColor() {
        return NamedTextColor.RED;
    }

    @Override
    protected void tick() {

        // If we break LOS, then we add to the counter of broken LOS ticks.
        if (!tetheredTo.hasLineOfSight(player))
            currentlyBrokenLOSTicks++;

        // If we have enough of those ticks, we can remove the effect.
        if (currentlyBrokenLOSTicks >= LOS_BREAK_TICKS) {
            service.removeEffect(player, this.getClass());
            return;
        }

        ParticleUtil.spawnParticlesBetweenTwoPoints(Particle.FLAME, player.getWorld(), player.getEyeLocation().toVector(), tetheredTo.getEyeLocation().toVector(), 30);
        player.setFireTicks(20);
    }

    /*
     * When this effect expires, launch the player towards the entity we are tethered to.
     */
    @Override
    protected void expire() {
        @NotNull Vector dir = tetheredTo.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
        dir.multiply(5);
        dir.add(new Vector(0, 2, 0));
        player.setVelocity(dir);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1.25f);
        ParticleUtil.spawnParticlesBetweenTwoPoints(Particle.SOUL_FIRE_FLAME, player.getWorld(), player.getEyeLocation().toVector(), tetheredTo.getEyeLocation().toVector(), 30);
    }

    @Override
    public void removed() {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1.9f);
        ParticleUtil.spawnParticlesBetweenTwoPoints(Particle.END_ROD, player.getWorld(), player.getEyeLocation().toVector(), tetheredTo.getEyeLocation().toVector(), 30);
        player.setFireTicks(0);
    }

    /*
     * If the entity that we are tethered to dies, remove the tether.
     */
    @EventHandler
    public void onTetherEntityDied(EntityDeathEvent event) {

        if (!event.getEntity().equals(tetheredTo))
            return;

        service.removeEffect(player, this.getClass());
    }
}
