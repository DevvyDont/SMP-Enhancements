package xyz.devvydont.smprpg.services;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.entity.bosses.LeveledDragon;
import xyz.devvydont.smprpg.entity.bosses.LeveledElderGuardian;
import xyz.devvydont.smprpg.entity.bosses.LeveledWarden;
import xyz.devvydont.smprpg.entity.bosses.LeveledWither;
import xyz.devvydont.smprpg.entity.interfaces.IDamageTrackable;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.entity.vanilla.*;
import xyz.devvydont.smprpg.events.LeveledEntitySpawnEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.tasks.PlaytimeTracker;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class EntityService implements IService, Listener {

    public static final String ENTITY_CLASS_KEY = "entity-class";
    public static final String LEVEL_KEY_STRING = "level";

    private final Map<UUID, LeveledEntity<?>> entityInstances;
    private final Map<String, CustomEntityType> entityResolver;
    private final Map<EntityType, Class<? extends LeveledEntity<?>>> vanillaEntityHandlers;

    private BukkitTask wellnessCheckTask = null;

    public static NamespacedKey getClassNamespacedKey() {
        return new NamespacedKey(SMPRPG.getInstance(), ENTITY_CLASS_KEY);
    }

    public static NamespacedKey getLevelNamespacedKey() {
        return new NamespacedKey(SMPRPG.getInstance(), LEVEL_KEY_STRING);
    }

    public EntityService() {
        this.entityInstances = new HashMap<>();
        this.entityResolver = new HashMap<>();
        this.vanillaEntityHandlers = new HashMap<>();
    }

    @Override
    public void setup() throws RuntimeException {

        // Start tracking playtime.
        PlaytimeTracker.start();

        for (CustomEntityType customEntityType : CustomEntityType.values())
            entityResolver.put(customEntityType.key(), customEntityType);

        var plugin = SMPRPG.getInstance();
        plugin.getLogger().info(String.format("Registered %s custom entity types", entityResolver.size()));

        vanillaEntityHandlers.put(EntityType.ZOMBIE, LeveledZombie.class);
        vanillaEntityHandlers.put(EntityType.SKELETON, LeveledSkeleton.class);
        vanillaEntityHandlers.put(EntityType.STRAY, LeveledStray.class);
        vanillaEntityHandlers.put(EntityType.BOGGED, LeveledBogged.class);
        vanillaEntityHandlers.put(EntityType.SPIDER, LeveledSpider.class);
        vanillaEntityHandlers.put(EntityType.CAVE_SPIDER, LeveledSpider.class);
        vanillaEntityHandlers.put(EntityType.CREEPER, LeveledCreeper.class);
        vanillaEntityHandlers.put(EntityType.ENDERMAN, LeveledEnderman.class);
        vanillaEntityHandlers.put(EntityType.ENDER_DRAGON, LeveledDragon.class);
        vanillaEntityHandlers.put(EntityType.WITHER, LeveledWither.class);
        vanillaEntityHandlers.put(EntityType.GUARDIAN, LeveledGuardian.class);
        vanillaEntityHandlers.put(EntityType.ELDER_GUARDIAN, LeveledElderGuardian.class);
        vanillaEntityHandlers.put(EntityType.WARDEN, LeveledWarden.class);

        vanillaEntityHandlers.put(EntityType.BLAZE, LeveledBlaze.class);
        vanillaEntityHandlers.put(EntityType.WITHER_SKELETON, LeveledWitherSkeleton.class);

        vanillaEntityHandlers.put(EntityType.VILLAGER, LeveledVillager.class);
        vanillaEntityHandlers.put(EntityType.PILLAGER, LeveledPillager.class);

        vanillaEntityHandlers.put(EntityType.ARMOR_STAND, LeveledArmorStand.class);

        vanillaEntityHandlers.put(EntityType.BLOCK_DISPLAY, LeveledDisplay.class);
        vanillaEntityHandlers.put(EntityType.ITEM_DISPLAY, LeveledDisplay.class);
        vanillaEntityHandlers.put(EntityType.TEXT_DISPLAY, LeveledDisplay.class);

        plugin.getLogger().info(String.format("Associated %s vanilla entities with custom handlers", vanillaEntityHandlers.size()));

        // Setting up default scoreboard options
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective hpObjective = scoreboard.getObjective("hp_objective");
        if (hpObjective == null)
            hpObjective = scoreboard.registerNewObjective("hp_objective", Criteria.HEALTH, ComponentUtils.create(Symbols.HEART, NamedTextColor.RED));
        hpObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        hpObjective.setAutoUpdateDisplay(true);

        // Initialize entities that are already loaded

        for (var world : Bukkit.getWorlds())
            for (var entity : world.getEntities()) {

                // Ignore non living/displays
                if (!(entity instanceof LivingEntity) && !(entity instanceof Display))
                    continue;

                var leveled = getEntityInstance(entity);

                leveled.updateAttributes();
                trackEntity(leveled);
            }

        wellnessCheckTask = new BukkitRunnable() {

            @Override
            public void run() {
                var invalid = new ArrayList<UUID>();
                for (var entry : entityInstances.entrySet())
                    if (!entry.getValue().getEntity().isValid())
                        invalid.add(entry.getKey());
                for (var id : invalid)
                    entityInstances.remove(id);
            }
        }.runTaskTimer(plugin, 5*60*20, 5*60*20);
    }


    @Override
    public void cleanup() {
        for (var entity : entityInstances.values())
            entity.cleanup();
        entityInstances.clear();
        wellnessCheckTask.cancel();
    }

    /**
     * A vanilla entity is being queried, determine if there is a child class of VanillaEntity to use as a
     * handler class. If there is not, then we will use the default VanillaEntity that is compatible with every
     * entity.
     *
     * @param entity The entity to create a new wrapper instance for.
     */
    public LeveledEntity<?> getNewVanillaEntityInstance(Entity entity) {

        LeveledEntity<?> ret;

        // Are we using the vanilla handler? (We don't have a custom class setup for this vanilla type)
        if (!vanillaEntityHandlers.containsKey(entity.getType())) {
            ret = new VanillaEntity<>(entity);
            ret.updateAttributes();
            trackEntity(ret);
            return ret;
        }

        // Reflection hacks since we have a custom handler
        Class<? extends LeveledEntity<?>> handler = vanillaEntityHandlers.get(entity.getType());
        try {
            var clazz = entity.getType().getEntityClass();
            ret = handler.getConstructor(clazz).newInstance(entity);
            ret.updateAttributes();
            trackEntity(ret);
            return ret;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            SMPRPG.getInstance().getLogger().severe(String.format("Failed to instantiate vanilla class handler %s for entity type %s. Ensure that a constructor exists using the %s class as a parameter.", handler.getName(), entity.getType(), entity.getType().getEntityClass()));
            ret =  new VanillaEntity<>(entity);
            ret.updateAttributes();
            trackEntity(ret);
            return ret;
        }

    }

    /**
     * Given a living entity, get its LeveledEntity instance
     * If the entity has an instance we are tracking, return it.
     * If the entity is not being tracked, start tracking it by resolving its PDC
     *
     * @param entity The entity instance.
     * @return A wrapper instance.
     */
    public LeveledEntity<?> getEntityInstance(Entity entity) {

        // Are we already tracking them?
        if (entityInstances.containsKey(entity.getUniqueId()))
            return entityInstances.get(entity.getUniqueId());

        // Is this a player? We use a pretty barebones instance for players for least amount of interference possible
        if (entity instanceof Player player) {
            LeveledPlayer leveledPlayer = new LeveledPlayer(SMPRPG.getInstance(), player);
            trackEntity(leveledPlayer);
            return leveledPlayer;
        }

        // Does this entity have an associated entity handler? If no, assume vanilla entity
        String entityClass = entity.getPersistentDataContainer().get(getClassNamespacedKey(), PersistentDataType.STRING);
        if (entityClass == null || entityClass.equals(VanillaEntity.VANILLA_CLASS_KEY))
            return getNewVanillaEntityInstance(entity);

        // We do have an associated handler
        CustomEntityType type = entityResolver.get(entityClass);
        if (type == null)
            return getNewVanillaEntityInstance(entity);

        // Custom entities must be LivingEntity.
        if (!(entity instanceof LivingEntity living))
            throw new IllegalStateException("Entity " + entity.getClass().getCanonicalName() + " is not an instance of LivingEntity. Only living entities are supported for custom entities.");

        // Create an instance of the handler and track it.
        var leveled = type.create(living);
        trackEntity(leveled);
        return leveled;
    }

    public LeveledPlayer getPlayerInstance(Player player) {
        // We can guarantee we are gonna get a player instance from this
        return (LeveledPlayer) getEntityInstance(player);
    }

    @Nullable
    public LeveledEntity<?> spawnCustomEntity(CustomEntityType type, Location location) {

        // Spawn the vanilla entity to attach the wrapper to.
        var entity = location.getWorld().spawnEntity(location, type.Type, CreatureSpawnEvent.SpawnReason.CUSTOM, e -> {
            e.getPersistentDataContainer().set(getClassNamespacedKey(), PersistentDataType.STRING, type.key());
        });

        // Custom entities must be LivingEntity.
        if (!(entity instanceof LivingEntity living))
            throw new IllegalStateException("Entity " + entity.getClass().getCanonicalName() + " is not an instance of LivingEntity. Only living entities are supported for custom entities.");

        // Create an instance of the handler and track it.
        var leveled = type.create(living);
        leveled.updateAttributes();
        trackEntity(leveled);
        return leveled;
    }

    /**
     * Check if an entity is being tracked by this service.
     * @param entity The entity you want to check.
     * @return True if the entity is being tracked, otherwise False.
     */
    public boolean isTracking(Entity entity) {
        return this.entityInstances.containsKey(entity.getUniqueId());
    }

    private void trackEntity(LeveledEntity<?> entity) {
        removeEntity(entity.getEntity().getUniqueId());
        entityInstances.put(entity.getEntity().getUniqueId(), entity);
        if (entity instanceof Listener listener)
            SMPRPG.getInstance().getServer().getPluginManager().registerEvents(listener, SMPRPG.getInstance());
        entity.setup();
    }

    private void removeEntity(UUID uuid) {
        LeveledEntity<?> removed = entityInstances.remove(uuid);
        if (removed == null)
            return;

        removed.cleanup();
        if (removed instanceof Listener listener)
            HandlerList.unregisterAll(listener);
    }

    /**
     * Called when a creature is spawned for the first time.
     * Makes sure they have proper attributes and we are tracking them
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onEntitySpawnForTheFirstTime(CreatureSpawnEvent event) {
        var entity = getEntityInstance(event.getEntity());
        trackEntity(entity);
        entity.resetLevel();

        var leveledSpawnEvent = new LeveledEntitySpawnEvent(entity);
        leveledSpawnEvent.callEvent();

        entity.updateAttributes();
        entity.updateNametag();

        // If this entity is holding/wearing anything, we need to fix their items to have proper stats
        var equipment = event.getEntity().getEquipment();
        var plugin = SMPRPG.getInstance();
        if (equipment != null) {
            equipment.setHelmet(SMPRPG.getService(ItemService.class).ensureItemStackUpdated(equipment.getHelmet()));
            equipment.setChestplate(SMPRPG.getService(ItemService.class).ensureItemStackUpdated(equipment.getChestplate()));
            equipment.setLeggings(SMPRPG.getService(ItemService.class).ensureItemStackUpdated(equipment.getLeggings()));
            equipment.setBoots(SMPRPG.getService(ItemService.class).ensureItemStackUpdated(equipment.getBoots()));

            equipment.setItemInMainHand(SMPRPG.getService(ItemService.class).ensureItemStackUpdated(equipment.getItemInMainHand()));
            equipment.setItemInOffHand(SMPRPG.getService(ItemService.class).ensureItemStackUpdated(equipment.getItemInOffHand()));
        }
    }

    /**
     * Every time an entity is spawned, we need to construct an instance for them, we do this just to make sure
     * that at least every entity has some sort stat initialization
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onEntitySpawn(EntityAddToWorldEvent event) {

        // Ignore non living/displays
        if (!(event.getEntity() instanceof LivingEntity) && !(event.getEntity() instanceof Display))
            return;

        var leveled = getEntityInstance(event.getEntity());

        leveled.updateAttributes();
        trackEntity(leveled);
    }

    @EventHandler
    private void __onPlayerJoin(PlayerJoinEvent event) {
        var leveled = getPlayerInstance(event.getPlayer());
        leveled.updateAttributes();
        trackEntity(leveled);

        // Fix every item in their inventory
        for (var item : event.getPlayer().getInventory().getContents())
            if (item != null && !item.getType().equals(Material.AIR))
                SMPRPG.getService(ItemService.class).ensureItemStackUpdated(item);

        // Store first joined. No checking necessary.
        PlaytimeTracker.setFirstSeenIfNotPresent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void __onEntityDespawn(EntityRemoveFromWorldEvent event) {
        removeEntity(event.getEntity().getUniqueId());
    }

    // Handle spawning in the secondary name tags on players when they respawn
    @EventHandler(priority = EventPriority.MONITOR)
    private void __onRespawn(PlayerPostRespawnEvent event) {
        LeveledPlayer p = getPlayerInstance(event.getPlayer());
        p.updateNametag();
    }

    // Handle nametag updates and Damage popups for damage events
    @EventHandler(priority = EventPriority.MONITOR)
    private void __onHit(EntityDamageEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        // Update the entity's nametag
        var leveled = getEntityInstance(event.getEntity());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(SMPRPG.getInstance(), 1L);
    }

    // Handle nametag updates and Damage popups for healing events
    @EventHandler(priority = EventPriority.MONITOR)
    private void __onRegen(EntityRegainHealthEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        // Update the entity's nametag
        var leveled = getEntityInstance(event.getEntity());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(SMPRPG.getInstance(), 1L);

    }

    // Handle nametag updates potion effect events
    @EventHandler
    private void __onPotionEffectUpdate(EntityPotionEffectEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        var leveled = getEntityInstance((LivingEntity) event.getEntity());

        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(SMPRPG.getInstance(), 1L);
    }

    // Handle nametag updates when we switch the item in our hand
    @EventHandler
    private void __onSwitchItemInHand(PlayerItemHeldEvent event) {
        var leveled = getEntityInstance(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(SMPRPG.getInstance(), 1L);
    }

    // Handle nametag updates when we switch armor we are wearing
    @EventHandler
    private void __onEquipArmor(PlayerArmorChangeEvent event) {
        var leveled = getEntityInstance(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(SMPRPG.getInstance(), 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onDamageEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        var leveled = getEntityInstance(living);
        Entity dealer = event.getDamageSource().getCausingEntity();
        if (dealer == null)
            dealer = event.getDamager();

        // Only consider players that dealt damage
        if (!(dealer instanceof Player player))
            return;

        // Don't "brighten" a player's nametag or track damage dealt to them
        if (living instanceof Player)
            return;

        // Show the nametag and track the damage dealt if it is a monster
        if (leveled instanceof IDamageTrackable trackable)
            trackable.getDamageTracker().addDamageDealtByEntity(player, (int)event.getDamage());

        leveled.brightenNametag();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onSpawn(CreatureSpawnEvent event) {

        // Ignore non natural spawns
        List<CreatureSpawnEvent.SpawnReason> NATURAL_REASONS = List.of(CreatureSpawnEvent.SpawnReason.NATURAL, CreatureSpawnEvent.SpawnReason.DEFAULT);
        if (!NATURAL_REASONS.contains(event.getSpawnReason()))
            return;

        // Determine eligible creatures that can spawn in its place
        List<CustomEntityType> choices = new ArrayList<>();
        for (CustomEntityType type : CustomEntityType.values())
            // Check if this location is suitable for this custom entity
            if (type.testNaturalSpawn(event.getLocation()))
                choices.add(type);

        // Did we find a custom entity type?
        if (choices.isEmpty())
            return;

        // Pick a random entity to make
        CustomEntityType newEntity = choices.get((int) (Math.random()*choices.size()));
        var entity = this.spawnCustomEntity(newEntity, event.getLocation());
        if (entity == null)
            return;

        entity.setup();
        event.setCancelled(true);
    }
}
