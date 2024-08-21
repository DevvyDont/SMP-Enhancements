package xyz.devvydont.smprpg.listeners;

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
import org.bukkit.event.entity.*;
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

    // How much to decrease damage by depending on the difficulty.
    public static final float EASY_DAMAGE_MULTIPLIER = .25f;
    public static final float NORMAL_DAMAGE_MULITPLIER = .5f;
    public static final float HARD_DAMAGE_MULTIPLIER = 1f;

    // How effective defense is. The lower the number, the better damage reduction from defense is.
    public static final int DEFENSE_FACTOR = 100;

    // What should be the attack cooldown threshold to use vanilla's logic?
    private final static float COOLDOWN_FORGIVENESS_THRESHOLD = 0.6f;

    // Used to store the amount of damage an arrow entity should do.
    private final NamespacedKey PROJECTILE_DAMAGE_TAG;
    // The base damage of an arrow if it was not assigned one when it is launched. This could happen in the event
    // that we do not set a damage value on an arrow entity when it is fired from any source.
    private final static double BASE_ARROW_DAMAGE = 5;
    // The force factor to apply to an arrow's force to decide if this arrow was a fully charged
    // arrow shot. In vanilla minecraft, fully charged bow shots have a force of 3.0
    private final static double BOW_FORCE_FACTOR = 3.0;
    // The force factor to assume for entities. Entities do not have "bow force" in the same way
    // that players do, so we can make one up here. They also typically do not shoot as forceful as players.
    private final static double AI_BOW_FORCE_FACTOR = 1.75;
    // The velocity an arrow needs to be travelling to deal "maximum" damage. Arrows traveling this fast
    // will deal exactly the amount of damage of the attack damage stat that was applied to it when it was shot.
    private final static double MAX_ARROW_DAMAGE_VELOCITY = 60.0;

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

        PROJECTILE_DAMAGE_TAG = new NamespacedKey(plugin, "arrow-damage");
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
            case PEACEFUL -> EASY_DAMAGE_MULTIPLIER * damage * .5;
            case EASY -> EASY_DAMAGE_MULTIPLIER * damage;
            case NORMAL -> NORMAL_DAMAGE_MULITPLIER * damage;
            case HARD -> HARD_DAMAGE_MULTIPLIER * damage;
        };
    }

    /**
     * Given an arrow entity, return its tagged modified damage. Returns 5 by default if this arrow is not tagged.
     *
     * @param projectile
     * @return
     */
    public double getBaseProjectileDamage(Projectile projectile) {
        return projectile.getPersistentDataContainer().getOrDefault(PROJECTILE_DAMAGE_TAG, PersistentDataType.DOUBLE, BASE_ARROW_DAMAGE);
    }

    /**
     * Given an arrow and a damage value, set the arrow's base damage to deal upon impact.
     *
     * @param projectile
     * @param damage
     */
    public void setBaseProjectileDamage(Entity projectile, double damage) {
        projectile.getPersistentDataContainer().set(PROJECTILE_DAMAGE_TAG, PersistentDataType.DOUBLE, damage);
    }

    /**
     * Used to tag arrows with the proper damage value based on the attack damage stat of the shooter.
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
            arrowDamage *= (event.getForce() / BOW_FORCE_FACTOR);
        // If this wasn't a player, consider difficulty and give the arrow a 2x boost to nullify velocity falloff since entities don't shoot arrows that travel at max speed
        else
            arrowDamage = getDifficultyAdjustedDamage(event.getEntity().getWorld(), arrowDamage) * AI_BOW_FORCE_FACTOR;

        // Set the damage.
        setBaseProjectileDamage(event.getProjectile(), arrowDamage);
    }

    /**
     * As soon as we possibly can, intercept events where an arrow is damaging an entity.
     * We need to set the base damage of this event to the arrow's base damage and apply
     * a multiplier on it based on arrow velocity.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onArrowDamageReceived(EntityDamageByEntityEvent event) {

        // Ignore events not involving an arrow harming something.
        if (!(event.getDamager() instanceof AbstractArrow arrow))
            return;

        // Retrieve the base damage of this arrow assuming this arrow is at max velocity.
        double baseArrowDamage = getBaseProjectileDamage(arrow);

        // Convert the velocity to m/s by using the tick rate of the server
        double arrowVelocity = arrow.getVelocity().length() * 20;
        double arrowForce = arrowVelocity / MAX_ARROW_DAMAGE_VELOCITY;
        double newArrowDamage = baseArrowDamage * arrowForce;

        // Multiply the base damage of this event by how fast this arrow is going compared to the "max" velocity
        event.setDamage(newArrowDamage);
    }

    /*
     * When entities launch projectiles, the attack damage stat of the entity needs to be
     * transferred to the projectile, so we can listen for it when it deals damage.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityNonArrowProjectileLaunch(ProjectileLaunchEvent event) {

        // Ignore arrows. This is handled in its own case.
        if (event.getEntity() instanceof AbstractArrow)
            return;

        // See if there was an entity that launched this projectile that can have attributes.
        if (!(event.getEntity().getShooter() instanceof LivingEntity living))
            return;

        // See if they have an attack damage stat
        AttributeInstance attack = living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attack == null)
            return;

        // This projectile should do the damage of the attack stat.
        setBaseProjectileDamage(event.getEntity(), attack.getValue());
    }

    /*
     * Used to listen for when a damage tagged projectile is dealing damage to an entity.
     * This method ignores arrows as that is handled somewhere else.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamagedByNonArrowProjectile(EntityDamageByEntityEvent event) {

        // We only want to listen to projectile damage events.
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        if (!(event.getDamager() instanceof Projectile projectile))
            return;

        // Do not handle arrows. That is done somewhere else.
        if (projectile instanceof AbstractArrow)
            return;

        // Set the damage of the event to the damage that the projectile is supposed to do
        event.setDamage(getBaseProjectileDamage(projectile));
    }

    /*
     * Fixes sonic boom attack from wardens to use their attack damage stat rather than a fixed amount
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSonicBoom(EntityDamageByEntityEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.SONIC_BOOM))
            return;

        if (!(event.getDamager() instanceof Warden warden))
            return;

        AttributeInstance damage = warden.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        double adjustedDamage = getDifficultyAdjustedDamage(event.getEntity().getWorld(), damage != null ? damage.getValue() : 15);
        event.setDamage(adjustedDamage);
    }

    /*
     * Some entities can explode. The damage done should scale based on the level of the entity.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplosionDamage(EntityDamageByEntityEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
            return;

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        LeveledEntity leveledEntity = plugin.getEntityService().getEntityInstance(living);
//        int level = leveledEntity.getLevel();

        // Convert the vanilla mc damage to a multiplier. If we divide by 20, we can say that it would one shot a player in vanilla.
        double power = event.getFinalDamage() / 20.0;
        event.setDamage((leveledEntity.getLevel() * leveledEntity.getLevel() + 50) * power);
    }

    /**
     * Hook into when two entities are damaging each other, and construct a custom event where
     * the entity dealing damage is instead set to the entity that *caused* the damage to occur.
     * This makes it much easier to intercept events such as a player shooting a zombie with
     * a bow, as it does the work of backtracking a projectile shooter back to the player.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
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
            if (projectile.getShooter() instanceof LivingEntity)
                dealer = (Entity) projectile.getShooter();
        }

        // Handle the case where the entity dealing damage doesn't even have the attack attribute, we have to set
        // the damage of the event manually
        if (dealer instanceof LivingEntity && ((LivingEntity) dealer).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) == null)
            event.setDamage(plugin.getEntityService().getEntityInstance(dealer).getBaseAttackDamage());

        // Call the event and check if it is canceled for any reason.
        CustomEntityDamageByEntityEvent eventWrapper = new CustomEntityDamageByEntityEvent(event, damaged, dealer, projectile);
        eventWrapper.callEvent();
        if (eventWrapper.isCancelled())
            return;

        event.setDamage(eventWrapper.getFinalDamage());
    }

    /**
     * Handle the effectiveness of defense while wearing armor.
     * Analyze the total defense of the entity who is taking damage, multiply the damage
     * by their resistance multiplier.
     *
     * Since this is a multiplicative operation, this should run almost last.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageEntityWithDefense(CustomEntityDamageByEntityEvent event) {

        // Since we are only really overriding how armor and resistance works, let's use the raw damage
        double newDamage = event.getFinalDamage();

        // Using the defense of the receiver, reduce the damage
        if (event.getDamaged() instanceof Attributable) {
            // Apply the entity's defense attribute
            LeveledEntity leveledEntity = plugin.getEntityService().getEntityInstance(event.getDamaged());
            newDamage *= calculateDefenseDamageMultiplier(leveledEntity.getDefense());
        }

        event.setFinalDamage(newDamage);
    }

    /*
     * Entities with the "armor" attribute should take off some damage additively at the very end of all damage calculations.
     */
    @EventHandler
    public void onEntityDamagedEntityWithArmor(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        AttributeInstance armor = living.getAttribute(Attribute.GENERIC_ARMOR);
        if (armor == null)
            return;

        double damage = event.getFinalDamage();
        damage -= armor.getValue();
        damage = Math.max(1, damage);
        event.setDamage(damage);
    }

    /*
     * Currently, bows have a strength stat that can work as a melee stat which is not intended due
     * to the constraints of minecraft's attribute system. If an entity is holding a bow and does melee damage,
     * they should be severely nerfed.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
        if (cooldown >= COOLDOWN_FORGIVENESS_THRESHOLD)
            return;

        double newDamage = Math.sqrt(event.getFinalDamage());

        // This player was well below the threshold for us to consider dealing full damage, apply some math to reduce it.
        event.setDamage(newDamage);
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
