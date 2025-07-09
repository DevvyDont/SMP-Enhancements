package xyz.devvydont.smprpg.listeners;

import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.listeners.damage.CriticalDamageListener;
import xyz.devvydont.smprpg.listeners.damage.DamagePopupListener;
import xyz.devvydont.smprpg.services.DifficultyService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.IService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Overrides all instances of vanilla damage that is desired to fit our new attribute logic
 */
public class EntityDamageCalculatorService implements Listener, IService {

    /*
     * STATIC CONSTANTS (SETTINGS)
     */

    // How much to decrease damage by depending on the difficulty.
    public static final float EASY_DAMAGE_MULTIPLIER = .5f;
    public static final float NORMAL_DAMAGE_MULTIPLIER = .75f;
    public static final float HARD_DAMAGE_MULTIPLIER = 1f;

    // How effective defense is. The lower the number, the better damage reduction from defense is.
    public static final int DEFENSE_FACTOR = 100;

    // What should be the attack cooldown threshold to use vanilla's logic?
    public final static float COOLDOWN_FORGIVENESS_THRESHOLD = 0.9f;

    // Used to store the amount of damage an arrow entity should do.
    public static final NamespacedKey PROJECTILE_DAMAGE_TAG = new NamespacedKey("smprpg", "projectile_damage");
    // The base damage to an arrow if it was not assigned one when it is launched. This could happen in the event
    // that we do not set a damage value on an arrow entity when it is fired from any source.
    public final static double BASE_ARROW_DAMAGE = 5;
    // The force factor to assume for entities. Entities do not have "bow force" in the same way
    // that players do, so we can make one up here. They struggle to shoot arrows at full velocity but still shoot
    // them with a full bow force of 1.0. We should give them a little boost.
    public final static double AI_BOW_FORCE_FACTOR = 1.75;
    // The velocity an arrow needs to be travelling to deal "maximum" damage. Arrows traveling this fast
    // will deal exactly the amount of damage to the attack damage stat that was applied to it when it was shot.
    public final static double MAX_ARROW_DAMAGE_VELOCITY = 60.0;

    // How much percentage of damage to increase per level of strength an entity has.
    public final static double DAMAGE_PERCENT_PER_LEVEL_STRENGTH_EFFECT = 20;
    // How much defense per level of resistance is applied when an entity has the resistance effect.
    // This will actually end up being implemented in LeveledEntity#getDefense() for convenience.
    public final static int DEFENSE_PERCENT_PER_LEVEL_RESISTANCE_EFFECT = 20;

    /*
     * STATIC HELPER METHODS
     */

    /**
     * Checks the state of this entity regarding holding bows. Returns a map for all hand related equipment slots and
     * whether they are holding a bow.
     * @param entity The entity in question.
     * @return A map of equipment slot states regarding holding bows.
     */
    public static Map<EquipmentSlotGroup, Boolean> holdingBowInHand(LivingEntity entity) {

        var map = new HashMap<>(Map.of(
                EquipmentSlotGroup.MAINHAND, false,
                EquipmentSlotGroup.OFFHAND, false,
                EquipmentSlotGroup.HAND, false
        ));

        if (entity.getEquipment() == null)
            return map;

        // Analyze what's in both hands and update accordingly if a bow is present.
        var mainHand = entity.getEquipment().getItemInMainHand();
        var offHand = entity.getEquipment().getItemInOffHand();
        if (!mainHand.getType().equals(Material.AIR) && SMPRPG.getService(ItemService.class).getBlueprint(mainHand).getItemClassification().isBow())
            map.put(EquipmentSlotGroup.MAINHAND, true);
        if (!offHand.getType().equals(Material.AIR) && SMPRPG.getService(ItemService.class).getBlueprint(offHand).getItemClassification().isBow())
            map.put(EquipmentSlotGroup.OFFHAND, true);

        if (map.get(EquipmentSlotGroup.MAINHAND) || map.get(EquipmentSlotGroup.OFFHAND))
            map.put(EquipmentSlotGroup.HAND, true);

        return map;
    }

    /**
     * The vanilla minecraft attribute system has a significant flaw. Since bows can be offhanded, we need to make sure
     * that the entity isn't dual wielding an attribute increasing weapon in their main hand if they are shooting
     * using their offhand, or dual wielding bows.
     * @param entity The entity that is being state checked.
     * @return True if they satisfy the condition of bow stack exploiting, false if they seem to be clear.
     */
    public static boolean isTryingToBowStackExploit(LivingEntity entity) {

        if (entity.getEquipment() == null)
            return false;

        // First check the easy condition, are they dual wielding bows?
        var bowState = holdingBowInHand(entity);
        if (bowState.getOrDefault(EquipmentSlotGroup.MAINHAND, false) && bowState.getOrDefault(EquipmentSlotGroup.OFFHAND, false))
            return true;

        // Now check if they are trying to shoot a bow in their offhand but hold a weapon in their man hand.
        if (!bowState.getOrDefault(EquipmentSlotGroup.OFFHAND, false))
            return false;

        var main = entity.getEquipment().getItemInMainHand();
        if (main.getType().equals(Material.AIR))
            return false;

        // At this point, we know we are off-handing a bow. Are we attempting to hold a stat increasing weapon in our main hand as well?
        var blueprint = SMPRPG.getService(ItemService.class).getBlueprint(main);
        if (blueprint.getItemClassification().isWeapon())
            return true;

        // We seem to be innocent...
        return false;
    }

    /**
     * Given a defense attribute value, return the multiplier of some damage to take.
     * For example, if we have 100 defense, we should only take 10% of the damage
     * If we have 10 defense, we should take only 50% of the damage, etc.
     *
     * @param defense The defense to calculate with.
     * @return The multiplier to use.
     */
    public static double calculateDefenseDamageMultiplier(double defense) {
        return (double)DEFENSE_FACTOR / (Math.max(defense, 0) + DEFENSE_FACTOR);
    }

    /**
     * Given a defense attribute value, return the percentage of damage to negate.
     *
     * @param defense The defense to calculate with.
     * @return The multiplier to use.
     */
    public static double calculateResistancePercentage(double defense) {

        // Since we know that the damage multiplier for defense is ALWAYS (0-1], we can reverse it
        return 1.0 - calculateDefenseDamageMultiplier(defense);
    }

    /**
     * Given a health and defense value, calculate the effective health of an entity.
     * Effective health can be calculated as HP/DEF% where DEF% is the percentage of damage something takes due to DEF.
     *
     * @param health The health to calculate with.
     * @param defense The defense to calculate with.
     * @return The EHP of the HP and DEF combination.
     */
    public static double calculateEffectiveHealth(double health, double defense) {
        return health / calculateDefenseDamageMultiplier(defense);
    }

    private final CriticalDamageListener criticalDamageListener;
    private final DamagePopupListener popupListener;

    public EntityDamageCalculatorService() {
        // Instantiate event handlers.
        criticalDamageListener = new CriticalDamageListener();
        popupListener = new DamagePopupListener();
    }

    /*
     * SERVICE BOILERPLATE
     */

    @Override
    public void setup() throws RuntimeException {
        // Start event handlers. You can comment these out to disable game features.
        criticalDamageListener.start();  // Handles the critical damage calculation mechanic.
        popupListener.start();  // Handles popups in the world for health related events.
    }

    @Override
    public void cleanup() {
        HandlerList.unregisterAll(this);

        // Stop event handlers.
        criticalDamageListener.stop();
        popupListener.stop();
    }

    /*
     * HELPER METHODS
     */

    /**
     * The difficulty of the world affects the damage multiplier in some cases, we should account for that
     * @param world The world to extract difficulty from.
     * @param damage The final damage to multiply.
     * @return A multiplied damage value.
     */
    private double getDifficultyAdjustedDamage(World world, double damage) {
        return switch (world.getDifficulty()) {
            case PEACEFUL -> EASY_DAMAGE_MULTIPLIER * damage * .5;
            case EASY -> EASY_DAMAGE_MULTIPLIER * damage;
            case NORMAL -> NORMAL_DAMAGE_MULTIPLIER * damage;
            case HARD -> HARD_DAMAGE_MULTIPLIER * damage;
        };
    }

    /**
     * Given an arrow entity, return its tagged modified damage. Returns 5 by default if this arrow is not tagged.
     * @param projectile The projectile to query.
     * @return The intended base projectile damage.
     */
    public double getBaseProjectileDamage(Projectile projectile) {
        return projectile.getPersistentDataContainer().getOrDefault(PROJECTILE_DAMAGE_TAG, PersistentDataType.DOUBLE, BASE_ARROW_DAMAGE);
    }

    /**
     * Given an arrow and a damage value, set the arrow's base damage to deal upon impact.
     * @param projectile The projectile to set damage to.
     * @param damage The damage to set.
     */
    public void setBaseProjectileDamage(Entity projectile, double damage) {
        projectile.getPersistentDataContainer().set(PROJECTILE_DAMAGE_TAG, PersistentDataType.DOUBLE, damage);
    }

    /**
     * Helper method that determines if a certain damage cause should use the entity's strength stat as a base
     * damage point. We do this so that we can properly override vanilla minecraft's mechanics such as resistance,
     * armor plating, strength, and vanilla enchantments so we can define those attributes ourselves.
     * @param cause The damage cause of an EntityDamageEvent
     * @return true if we should use strength, false if we should use vanilla's logic or handle it somewhere else.
     */
    private boolean doesntUseStrengthAttribute(EntityDamageEvent.DamageCause cause) {

        // If this is environmental damage, we don't need to worry about the case
        if (EnvironmentalDamageListener.getEnvironmentalDamagePercentage(cause) > 0)
            return true;

        // Used as a whitelist for certain cases where two entities are damaging each other
        return !switch (cause) {
            case ENTITY_SWEEP_ATTACK, ENTITY_EXPLOSION, ENTITY_ATTACK, SONIC_BOOM -> true;
            default -> false;
        };

    }

    /*
     * GENERAL DAMAGE EVENTS
     * The order of events *should* somewhat match the expected order of execution of events, to make this feel less
     * like spaghetti. The general flow of events should be something like this:
     * - EntityDamageEvent/EntityDamageByEntityEvent LOWEST -> HIGHEST (Keep in mind, EntityDamageEvent handlers will ALSO receive EntityDamageByEntityEvent instances!)
     * - CustomEntityDamageByEntityEvent LOWEST -> MONITOR
     * - EntityDamageEvent/EntityDamageByEntityEvent -> MONITOR
     * As you can see, the CustomEntityDamageByEntity executes before the normal Bukkit even is done.
     * This is because our event is fired *during* the Bukkit one, preferably during HIGHEST priority.
     * This is crucial as if we have a "monitor" handler in our custom event, and any modifications were made in
     * a high priority standard event that happened after our custom one, our results will seem mixed.
     */

    /*
     * The very first entry point into our damage calculations. If an entity is dealing damage to another one, set the
     * damage to the attack to the strength stat of the entity.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void __onEntityDealDamageToAnotherEntity(EntityDamageByEntityEvent event) {

        // Depending on the cause of the damage, determine whether we should use strength or not
        if (doesntUseStrengthAttribute(event.getCause()))
            return;

        // If the entity that dealt this damage is not an entity that could potentially have a damage stat, we cannot
        // set the very base damage to the attack.
        if (!(event.getDamageSource().getCausingEntity() instanceof LivingEntity dealer))
            return;

        var attack = dealer.getAttribute(Attribute.ATTACK_DAMAGE);

        // Do they have an attack damage stat? If they don't, we cannot determine an attack to set.
        if (attack == null)
            return;

        // Set the damage
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, attack.getValue());
    }

    /*
     * It is annoying that baby enemies deal the same damage as normal ones since they are annoying to hit.
     * Apply an -80% damage reduction if an entity is a baby.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void __onBabyEntityDealtDamage(EntityDamageByEntityEvent event) {

        // Only look for entities that can be a baby
        if (!(event.getDamager() instanceof Ageable ageable))
            return;

        // Only look for babies
        if (ageable.isAdult())
            return;

        // Apply damage debuff
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * .2);
    }

    /*
     * Handle simple damage boosting effects. For now, this is just the strength potion effect.
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void __onDamageDealtWithStrength(CustomEntityDamageByEntityEvent event) {

        if (doesntUseStrengthAttribute(event.getVanillaCause()))
            return;

        if (!(event.getDealer() instanceof LivingEntity living))
            return;

        PotionEffect strength = living.getPotionEffect(PotionEffectType.STRENGTH);
        if (strength == null)
            return;

        double multiplier = (strength.getAmplifier() + 1) * DAMAGE_PERCENT_PER_LEVEL_STRENGTH_EFFECT / 100.0 + 1.0;
        event.multiplyDamage(multiplier);
    }

    /*
     * Currently, bows have a strength stat that can work as a melee stat which is not intended due
     * to the constraints of minecraft's attribute system. If an entity is holding a bow and does melee damage,
     * they should be severely nerfed.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void __onMeleeDamageHoldingBow(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        // 95% damage reduction for abusing bow attributes
        if (isTryingToBowStackExploit(living)) {
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * .05);
            living.sendMessage(ComponentUtils.error("You seem to be struggling trying to deal damage with the items you are holding..."));
            living.getWorld().playSound(living.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1f, 1.25f);
        }

    }

    /**
     * Handle the "armor" mechanic. Currently, you just get a small i-frame bonus when you have armor.
     * @param event The {@link EntityDamageByEntityEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onEntityDamagedEntityWithArmor(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        var leveled = SMPRPG.getService(EntityService.class).getEntityInstance(living);

        var armor = living.getAttribute(Attribute.ARMOR);
        int iframeTicks = 0;

        // Armor changes how much iframes we get for this attack
        if (armor != null)
            iframeTicks = (int) armor.getValue();

        final int noDamageTicks = leveled.getInvincibilityTicks() + iframeTicks;

        living.setNoDamageTicks(noDamageTicks);
        Bukkit.getScheduler().runTaskLater(SMPRPG.getInstance(), () -> living.setNoDamageTicks(noDamageTicks), 0);
    }

    /*
     * PROJECTILE/BOW RELATED DAMAGE EVENTS
     */

    /**
     * Used to tag arrows with the proper damage value based on the attack damage stat of the shooter.
     * @param event The {@link EntityShootBowEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onEntityShootBow(EntityShootBowEvent event) {

        AttributeInstance attackDamage = event.getEntity().getAttribute(Attribute.ATTACK_DAMAGE);
        // This entity doesn't have an attack damage attribute, we can't do anything.
        if (attackDamage == null)
            return;

        // Using the attack of the entity, set a base damage value to use.
        double arrowDamage = attackDamage.getValue();

        // Modify it by the force of the event.
        arrowDamage *= event.getForce();

        // If this wasn't a player, consider difficulty and give the arrow a 2x boost to nullify velocity falloff since entities don't shoot arrows that travel at max speed
        if (!(event.getEntity() instanceof Player))
            arrowDamage = getDifficultyAdjustedDamage(event.getEntity().getWorld(), arrowDamage) * AI_BOW_FORCE_FACTOR;

        // Punish bow stacking.
        if (isTryingToBowStackExploit(event.getEntity())) {
            arrowDamage *= .05;
            event.getEntity().sendMessage(ComponentUtils.error("You seem to be struggling shooting arrows correctly with the items you are holding..."));
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ENDERMAN_HURT, 1f, 1.25f);
        }

        // Set the damage.
        setBaseProjectileDamage(event.getProjectile(), arrowDamage);
    }

    /**
     * As soon as we possibly can, intercept events where an arrow is damaging an entity.
     * We need to set the base damage of this event to the arrow's base damage and apply
     * a multiplier on it based on arrow velocity.
     * @param event The {@link EntityDamageByEntityEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onArrowDamageReceived(EntityDamageByEntityEvent event) {

        // Ignore events not involving an arrow harming something.
        if (!(event.getDamager() instanceof AbstractArrow arrow))
            return;

        // Send ding noise to player if it was a non PVP interaction
        if (event.getDamageSource().getCausingEntity() instanceof Player player && !(event.getEntity() instanceof Player))
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, .75f, .8f);

        // Retrieve the base damage of this arrow assuming this arrow is at max velocity.
        double baseArrowDamage = getBaseProjectileDamage(arrow);

        // Convert the velocity to m/s by using the tick rate of the server
        double arrowVelocity = arrow.getVelocity().length() * TickTime.TPS;
        double arrowForce = arrowVelocity / MAX_ARROW_DAMAGE_VELOCITY;
        double newArrowDamage = baseArrowDamage * arrowForce;

        // Multiply the base damage of this event by how fast this arrow is going compared to the "max" velocity
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, newArrowDamage);
    }

    /*
     * When entities launch projectiles, the attack damage stat of the entity needs to be
     * transferred to the projectile, so we can listen for it when it deals damage.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void __onEntityNonArrowProjectileLaunch(ProjectileLaunchEvent event) {

        // Ignore arrows. This is handled in its own case.
        if (event.getEntity() instanceof AbstractArrow)
            return;

        // See if there was an entity that launched this projectile that can have attributes.
        if (!(event.getEntity().getShooter() instanceof LivingEntity living))
            return;

        // See if they have an attack damage stat
        AttributeInstance attack = living.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attack == null)
            return;

        // This projectile should do the damage to the attack stat.
        setBaseProjectileDamage(event.getEntity(), attack.getValue());
    }

    /*
     * Used to listen for when damage tagged projectile is dealing damage to an entity.
     * This method ignores arrows as that is handled somewhere else.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void __onEntityDamagedByNonArrowProjectile(EntityDamageByEntityEvent event) {

        // We only want to listen to projectile damage events.
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        if (!(event.getDamager() instanceof Projectile projectile))
            return;

        // Do not handle arrows. That is done somewhere else.
        if (projectile instanceof AbstractArrow)
            return;

        // Set the damage of the event to the damage that the projectile is supposed to do
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, getBaseProjectileDamage(projectile));
    }

    /*
     * MISC ENTITY DAMAGE SOURCE EVENTS (WARDEN BEAMS, CREEPER EXPLOSIONS ETC)
     */

    /*
     * MAIN DAMAGE CALCULATIONS
     */

    /**
     * Hook into when two entities are damaging each other, and construct a custom event where
     * the entity dealing damage is instead set to the entity that *caused* the damage to occur.
     * This makes it much easier to intercept events such as a player shooting a zombie with
     * a bow, as it does the work of backtracking a projectile shooter back to the player.
     * This should also go last, so that events can modify vanilla interactions first if needed.
     * Our plugin should ideally rely on the custom event to prevent mismatches.
     * @param event The {@link EntityDamageByEntityEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onVanillaDamageEntityVsEntity(EntityDamageByEntityEvent event) {

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

        // Call the event and check if it is canceled for any reason.
        CustomEntityDamageByEntityEvent eventWrapper = new CustomEntityDamageByEntityEvent(event, damaged, dealer, projectile);
        eventWrapper.callEvent();
        if (eventWrapper.isCancelled())
            return;

        event.setDamage(EntityDamageEvent.DamageModifier.BASE, eventWrapper.getFinalDamage());
    }

    /**
     * Handle the effectiveness of defense while wearing armor.
     * Analyze the total defense of the entity who is taking damage, multiply the damage
     * by their resistance multiplier.
     * Since this is a multiplicative operation, this should run almost last.
     * @param event The {@link CustomEntityDamageByEntityEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void __onEntityDamageEntityWithDefense(CustomEntityDamageByEntityEvent event) {

        // Using the defense of the receiver, reduce the damage
        if (event.getDamaged() instanceof Attributable) {
            // Apply the entity's defense attribute
            var leveledEntity = SMPRPG.getService(EntityService.class).getEntityInstance(event.getDamaged());
            event.multiplyDamage(calculateDefenseDamageMultiplier(leveledEntity.getDefense()));
        }

    }

    /*
     * When players deal melee damage, we need to more aggressively decrease their damage output since Minecraft's
     * vanilla formula is not good enough for inflated damage numbers. The reasoning for this is to reward well-timed
     * hits and to prevent spam clicking DPS abuse on bosses since they don't have i-frames.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void __onPlayerAttackWhileOnCooldown(CustomEntityDamageByEntityEvent event) {

        // Is the entity dealing damage a player? We only care about players.
        if (!(event.getDealer() instanceof Player player))
            return;

        // We only care about melee interactions. Events caused by other sources are not affected by cooldown.
        if (!event.getOriginalEvent().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK))
            return;

        float cooldown = player.getAttackCooldown();

        // Is the player on cooldown? Don't do anything if they are dealing a full damage hit.
        // If the player was *close enough* then allow vanilla Minecraft's damage rules for damage reduction.
        if (cooldown >= COOLDOWN_FORGIVENESS_THRESHOLD)
            return;

        // Apply the multiplier.
        double multiplier = 0.05 + (1.0 - 0.05) * Math.pow(cooldown, 2);
        event.multiplyDamage(multiplier);
    }

    /**
     * Handle the incoming damage multiplier due to being on a different difficulty for players.
     * Only the entity receiving damage will be affected if they are a player and on a difficulty that isn't
     * standard.
     * @param event The {@link CustomEntityDamageByEntityEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void __onEntityDamageEntityOnSpecialDifficulty(CustomEntityDamageByEntityEvent event) {

        // First make sure the receiver is a player.
        if (!(event.getDamaged() instanceof Player player))
            return;

        // Ignore this event if another player is causing this. PVP is unaffected.
        if (event.getDealer() instanceof Player)
            return;

        // Multiply their difficulty damage multiplier.
        var multiplier = DifficultyService.getDamageMultiplier(SMPRPG.getService(DifficultyService.class).getDifficulty(player));
        event.multiplyDamage(multiplier);
    }

}
