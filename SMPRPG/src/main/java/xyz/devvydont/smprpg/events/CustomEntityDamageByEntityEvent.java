package xyz.devvydont.smprpg.events;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomEntityDamageByEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final EntityDamageByEntityEvent originalEvent;

    private final Entity damaged;
    private final Entity dealer;
    private final Projectile projectile;

    private final Audience audience;

    private double originalDamage;

    private double additiveDamage = 0;
    private double scalarDamage = 1.0;
    private double multiplicativeDamage = 1.0;

    public CustomEntityDamageByEntityEvent(EntityDamageByEntityEvent originalEvent, Entity damaged, Entity dealer, @Nullable Projectile projectile) {
        this.originalEvent = originalEvent;
        this.originalDamage = originalEvent.getDamage();
        this.damaged = damaged;
        this.dealer = dealer;
        this.projectile = projectile;
        audience = Audience.audience(damaged, dealer);
    }

    public EntityDamageEvent.DamageCause getVanillaCause() {
        return originalEvent.getCause();
    }

    public EntityDamageByEntityEvent getOriginalEvent() {
        return originalEvent;
    }

    public Entity getDamaged() {
        return damaged;
    }

    public Entity getDealer() {
        return dealer;
    }

    @Nullable
    public Projectile getProjectile() {
        return projectile;
    }

    public boolean isProjectile() {
        return projectile != null;
    }

    public double getOriginalDamage() {
        return originalDamage;
    }

    public void addDamage(double damage) {
        additiveDamage += damage;
    }

    public void removeDamage(double damage) {
        additiveDamage -= damage;
    }

    public void addScalarDamage(double multiplier) {
        scalarDamage += multiplier;
    }

    public void removeScalarDamage(double multiplier) {
        scalarDamage -= multiplier;
    }

    public void multiplyDamage(double multiplier) {
        multiplicativeDamage *= multiplier;
    }

    public double getFinalDamage() {

        double damage = originalDamage;
        damage += additiveDamage;
        damage *= scalarDamage;
        damage *= multiplicativeDamage;
        return damage;
    }

    public Audience getAudience() {
        return audience;
    }

    @Override
    public boolean isCancelled() {
        return originalEvent.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        originalEvent.setCancelled(true);
    }
}
