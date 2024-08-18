package xyz.devvydont.smprpg.listeners;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

/**
 * Overrides all instances of vanilla damage that is desired to fit our new attribute logic
 */
public class DamageOverrideListener implements Listener {

    // How effective defense is. The lower the number, the better damage reduction from defense is.
    public static final int DEFENSE_FACTOR = 100;

    private NamespacedKey ARROW_DAMAGE_TAG;

    /**
     * Given a defense attribute value, return the multiplier of some damage to take.
     * For example, if we have 100 defense, we should only take 10% of the damage
     * If we have 10 defense, we should take only 50% of the damage, etc.
     *
     * @param defense
     * @return
     */
    public static double calculateDefenseDamageMultiplier(double defense) {
        return (double)DEFENSE_FACTOR / (Math.max(defense, 0) + DEFENSE_FACTOR);
    }

    /**
     * Given a defense attribute value, return the percentage of damage to negate.
     *
     * @param defense
     * @return
     */
    public static double calculateResistancePercentage(double defense) {

        // Since we know that the damage multiplier for defense is ALWAYS (0-1], we can reverse it
        return 1.0 - calculateDefenseDamageMultiplier(defense);
    }

    /**
     * Given a health and defense value, calculate the effective health of an entity.
     * Effective health can be calculated as HP/DEF% where DEF% is the percentage of damage something takes due to DEF.
     *
     * @param health
     * @param defense
     * @return
     */
    public static double calculateEffectiveHealth(double health, double defense) {
        return health / calculateDefenseDamageMultiplier(defense);
    }

    SMPRPG plugin;

    public DamageOverrideListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        ARROW_DAMAGE_TAG = new NamespacedKey(plugin, "arrow-damage");
    }

    /**
     * The difficulty of the world affects the damage multiplier in some cases, we should account for that
     *
     * @param world
     * @param damage
     * @return
     */
    private double getDifficultyAdjustedDamage(World world, double damage) {
        return switch (world.getDifficulty()) {
            case PEACEFUL -> 0.25 * damage;
            case EASY -> 0.5 * damage;
            case NORMAL -> 0.75 * damage;
            default -> damage;
        };
    }

    /**
     * Given an arrow entity, return its tagged modified damage. Returns 6 by default if this arrow is not tagged.
     *
     * @param arrow
     * @return
     */
    public double getBaseArrowDamage(Arrow arrow) {
        return arrow.getPersistentDataContainer().getOrDefault(ARROW_DAMAGE_TAG, PersistentDataType.DOUBLE, 6.0);
    }

    /**
     * Given an arrow and a damage value, set the arrow's base damage to deal upon impact.
     *
     * @param arrow
     * @param damage
     */
    public void setBaseArrowDamage(Entity arrow, double damage) {
        arrow.getPersistentDataContainer().set(ARROW_DAMAGE_TAG, PersistentDataType.DOUBLE, damage);
    }

    /**
     * Used to tag arrows with the proper damage value based on the attack damage stat of the player.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityShootBow(EntityShootBowEvent event) {

        AttributeInstance attackDamage = event.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        // This entity doesn't have an attack damage attribute, we can't do anything.
        if (attackDamage == null)
            return;

        // Using the attack damage of the entity, set a base damage value to use.
        double arrowDamage = attackDamage.getValue();

        // If this was a player, consider bow force.
        // Based on how charged the bow was, apply a percentage to the arrow's damage. (Fully charged = full damage)
        if (event.getEntity() instanceof Player)
            arrowDamage *= (event.getForce() / 3.0);
        // If this wasn't a player, consider difficulty and give the arrow a 2x boost to nullify velocity falloff since entities don't shoot arrows that travel at max speed
        else
            arrowDamage = getDifficultyAdjustedDamage(event.getEntity().getWorld(), arrowDamage) * 2;

        // Set the damage.
        setBaseArrowDamage(event.getProjectile(), arrowDamage);
    }

    /**
     * As soon as we possibly can, intercept events where an arrow is damaging an entity.
     * We need to set the base damage of this event to the arrow's base damage and apply
     * a multiplier on it based on arrow velocity.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onArrowDamageReceived(EntityDamageByEntityEvent event) {

        double MAX_ARROW_VELOCITY = 60.0;

        // Ignore events not involving an arrow harming something.
        if (!(event.getDamager() instanceof Arrow))
            return;

        // Retrieve the base damage of this arrow assuming this arrow is at max velocity.
        Arrow arrow = (Arrow) event.getDamager();
        double baseArrowDamage = getBaseArrowDamage(arrow);

        double arrowVelocity = arrow.getVelocity().length() * 20;
        double arrowForce = arrowVelocity / MAX_ARROW_VELOCITY;
        double newArrowDamage = baseArrowDamage * arrowForce;

        // Multiply the base damage of this event by how fast this arrow is going compared to the "max" velocity
        event.setDamage(newArrowDamage);
    }

    // Fixes sonic boom attack from wardens to use their attack damage stat rather than a fixed amount
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSonicBoom(EntityDamageByEntityEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.SONIC_BOOM))
            return;

        if (!(event.getDamager() instanceof Warden warden))
            return;

        AttributeInstance damage = warden.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        double adjustedDamage = getDifficultyAdjustedDamage(event.getEntity().getWorld(), damage != null ? damage.getValue() : 15);
        event.setDamage(adjustedDamage);
    }

    /**
     * Where most of the magic happens. This event is going to be called literally any time an entity deals damage
     * to another entity. When this happens, we simply just do the following:
     *
     *
     * @param event
     */
    @EventHandler
    public void onVanillaDamageEntityVsEntity(EntityDamageByEntityEvent event) {

        // Handle the case where the source is a projectile. If it is, there is potential that
        // we are getting shot by a bow.
        Entity damaged = event.getEntity();
        Entity dealer = event.getDamager();
        Projectile projectile = null;

        // Handle the case where a projectile was thrown by an entity and configure the correct damage dealer
        if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            projectile = (Projectile) dealer;

            // If this projectile sourced from something else, use that instead
            if (projectile.getShooter() instanceof LivingEntity) {
                dealer = (Entity) projectile.getShooter();
                LeveledEntity leveledDealer = plugin.getEntityService().getEntityInstance((LivingEntity) dealer);
                double damage = leveledDealer.getBaseAttackDamage();
                // If the shooter was NOT a player and the projectile was NOT an arrow, adjust the damage.
                // This is so things like blazes or snowmen etc can deal proper damage.
                if (!(dealer instanceof Player) && !(projectile instanceof Arrow))
                    event.setDamage(getDifficultyAdjustedDamage(dealer.getWorld(), damage));
            }
        }

        // Handle the case where the entity dealing damage doesn't even have the attack attribute, we have to set
        // the damage of the event manually
        if (dealer instanceof LivingEntity && ((LivingEntity) dealer).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) == null)
            event.setDamage(plugin.getEntityService().getEntityInstance(dealer).getBaseAttackDamage());

        CustomEntityDamageByEntityEvent eventWrapper = new CustomEntityDamageByEntityEvent(event, damaged, dealer, projectile);
        eventWrapper.callEvent();
        if (eventWrapper.isCancelled())
            return;

        event.setDamage(eventWrapper.getFinalDamage());
    }

    /**
     * Where most of the magic happens. This event is going to be called literally any time an entity deals damage
     * to another entity. When this happens, we simply just do the following:
     *
     *  - Find the attack damage of the damage dealer
     *  - Find the defense of the damage receiver
     *  - Change the damage by multiplying the damage by the resistance
     *  - Apply any post processing effects
     *
     * @param event
     */
    @EventHandler
    public void onEntityDamageEntity(CustomEntityDamageByEntityEvent event) {

        // Since we are only really overriding how armor and resistance works, let's use the raw damage
        double newDamage = event.getFinalDamage();

        // Using the defense of the receiver, reduce the damage
        if (event.getDamaged() instanceof Attributable) {
            Attributable receiver = (Attributable) event.getDamaged();

            // Apply the entity's defense attribute
            LeveledEntity leveledEntity = plugin.getEntityService().getEntityInstance(event.getDamaged());
            newDamage *= calculateDefenseDamageMultiplier(leveledEntity.getDefense());

            // Apply the entity's true defense attribute
            AttributeInstance armor = receiver.getAttribute(AttributeWrapper.ARMOR.getAttribute());
            if (armor != null)
                newDamage -= armor.getValue();
        }

        event.setFinalDamage(newDamage);
    }

    @EventHandler
    public void onMeleeDamageHoldingBow(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        // If two entities did direct damage to each other and one is holding a bow, we need to apply a penalty
        boolean holdingBow = false;

        ItemStack mainHand = living.getEquipment().getItemInMainHand();
        ItemStack offHand = living.getEquipment().getItemInOffHand();
        if (!mainHand.getType().equals(Material.AIR) && plugin.getItemService().getBlueprint(mainHand).getItemClassification().isBow())
            holdingBow = true;
        if (!offHand.getType().equals(Material.AIR) && plugin.getItemService().getBlueprint(offHand).getItemClassification().isBow())
            holdingBow = true;

        // 95% damage reduction
        if (holdingBow)
            event.setDamage(event.getFinalDamage() * .05);

    }

    /*
     * When players deal melee damage, we need to more aggressively decrease their damage output since Minecraft's
     * vanilla formula is not good enough for inflated damage numbers. The reasoning for this is to reward well-timed
     * hits and to prevent spam clicking DPS abuse on bosses since they don't have i-frames.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerAttackWhileOnCooldown(EntityDamageByEntityEvent event) {

        // Is the entity dealing damage a player? We only care about players.
        if (!(event.getDamager() instanceof Player player))
            return;

        float cooldown = player.getAttackCooldown();

        // Is the player on cooldown? Don't do anything if they are dealing a full damage hit.
        // If the player was *close enough* then allow vanilla Minecraft's damage rules for damage reduction.
        if (cooldown >= 0.9f)
            return;

        // This player was well below the threshold for us to consider dealing full damage, apply some math to reduce it.
        event.setDamage(Math.sqrt(event.getFinalDamage()));
    }

    /*
     * It is annoying that baby enemies deal the same damage as normal ones since they are annoying to hit.
     * Apply an -80% damage reduction if an entity is a baby.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBabyEntityDealtDamage(EntityDamageByEntityEvent event) {

        // Only look for entities that can be a baby
        if (!(event.getDamager() instanceof Ageable ageable))
            return;

        // Only look for babies
        if (ageable.isAdult())
            return;

        // Apply damage debuff
        event.setDamage(event.getFinalDamage() * .2);
    }

}
