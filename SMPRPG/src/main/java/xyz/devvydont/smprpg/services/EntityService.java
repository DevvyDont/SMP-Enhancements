package xyz.devvydont.smprpg.services;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.*;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.entity.base.EnemyEntity;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.entity.vanilla.*;
import xyz.devvydont.smprpg.events.LeveledEntitySpawnEvent;
import xyz.devvydont.smprpg.util.formatting.DamagePopupUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityService implements BaseService, Listener {

    public static final String ENTITY_CLASS_KEY = "entity-class";
    public static final String LEVEL_KEY_STRING = "level";

    private final SMPRPG plugin;

    private final Map<UUID, LeveledEntity> entityInstances;
    private final Map<String, CustomEntityType> entityResolver;
    private final Map<EntityType, Class<? extends VanillaEntity>> vanillaEntityHandlers;

    public static NamespacedKey getClassNamespacedKey(SMPRPG plugin) {
        return new NamespacedKey(plugin, ENTITY_CLASS_KEY);
    }

    public NamespacedKey getClassNamespacedKey() {
        return getClassNamespacedKey(plugin);
    }

    public static NamespacedKey getLevelNamespacedKey(SMPRPG plugin) {
        return new NamespacedKey(plugin, LEVEL_KEY_STRING);
    }

    public NamespacedKey getLevelNamespacedKey() {
        return getLevelNamespacedKey(plugin);
    }

    public EntityService(SMPRPG plugin) {
        this.plugin = plugin;
        this.entityInstances = new HashMap<>();
        this.entityResolver = new HashMap<>();
        this.vanillaEntityHandlers = new HashMap<>();
    }

    @Override
    public boolean setup() {
        plugin.getLogger().info("Setting up Entity Service");

        for (CustomEntityType customEntityType : CustomEntityType.values())
            entityResolver.put(customEntityType.key(), customEntityType);
        plugin.getLogger().info(String.format("Registered %s custom entity types", entityResolver.size()));

        vanillaEntityHandlers.put(EntityType.ENDERMAN, LeveledEnderman.class);
        vanillaEntityHandlers.put(EntityType.ENDER_DRAGON, LeveledDragon.class);
        vanillaEntityHandlers.put(EntityType.WITHER, LeveledWither.class);
        vanillaEntityHandlers.put(EntityType.ELDER_GUARDIAN, LeveledElderGuardian.class);
        vanillaEntityHandlers.put(EntityType.WARDEN, LeveledWarden.class);

        plugin.getLogger().info(String.format("Associated %s vanilla entities with custom handlers", vanillaEntityHandlers.size()));

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return true;
    }


    @Override
    public void cleanup() {
        for (LeveledEntity entity : entityInstances.values())
            entity.cleanup();
        entityInstances.clear();
    }

    @Override
    public boolean required() {
        return false;
    }

    /**
     * A vanilla entity is being queried, determine if there is a child class of VanillaEntity to use as a
     * handler class. If there is not, then we will use the default VanillaEntity that is compatible with every
     * entity.
     *
     * @param entity
     */
    public VanillaEntity getNewVanillaEntityInstance(LivingEntity entity) {

        VanillaEntity ret;

        // Are we using the vanilla handler? (We don't have a custom class setup for this vanilla type)
        if (!vanillaEntityHandlers.containsKey(entity.getType())) {
            ret =  new VanillaEntity(plugin, entity);
            ret.updateAttributes();
            trackEntity(ret);
            return ret;
        }

        // Reflection hacks since we have a custom handler
        Class<? extends VanillaEntity> handler = vanillaEntityHandlers.get(entity.getType());
        try {
            ret = handler.getConstructor(SMPRPG.class, LivingEntity.class).newInstance(plugin, entity);
            ret.updateAttributes();
            trackEntity(ret);
            return ret;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            plugin.getLogger().severe(String.format("Failed to instantiate vanilla class handler %s for entity type %s. Method signatures are probably incorrect", handler.getName(), entity.getType()));
            ret =  new VanillaEntity(plugin, entity);
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
     * @param entity
     * @return
     */
    public LeveledEntity getEntityInstance(LivingEntity entity) {

        // Are we already tracking them?
        if (entityInstances.containsKey(entity.getUniqueId()))
            return entityInstances.get(entity.getUniqueId());

        // Is this a player? We use a pretty barebones instance for players for least amount of interference possible
        if (entity instanceof Player player) {
            LeveledPlayer leveledPlayer = new LeveledPlayer(plugin, player);
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

        // Reflection hacks since we know we have a custom entity
        try {
            LeveledEntity leveled = type.entityHandler.getConstructor(SMPRPG.class, LivingEntity.class, CustomEntityType.class).newInstance(plugin, entity, type);
            trackEntity(leveled);
            return leveled;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            plugin.getLogger().severe(String.format("Failed to instantiate class handler %s for entity type %s. Method signatures are probably incorrect", type.entityHandler.getName(), type.name));
            return getNewVanillaEntityInstance(entity);
        }
    }

    public LeveledPlayer getPlayerInstance(Player player) {
        // We can guarantee we are gonna get a player instance from this
        return (LeveledPlayer) getEntityInstance(player);
    }

    public CustomEntityInstance spawnCustomEntity(CustomEntityType type, Location location) {

        Entity entity = location.getWorld().spawnEntity(location, type.entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, e -> {
            e.getPersistentDataContainer().set(getClassNamespacedKey(), PersistentDataType.STRING, type.key());
        });
        if (!(entity instanceof LivingEntity living)) {
            plugin.getLogger().severe(String.format("Tried to spawn incorrect custom entity vanilla class %s", type.entityType));
            entity.remove();
            return null;
        }

        // Reflection hacks
        try {
            CustomEntityInstance customEntity = (CustomEntityInstance) type.entityHandler.getConstructor(SMPRPG.class, LivingEntity.class, CustomEntityType.class).newInstance(plugin, living, type);
            customEntity.updateAttributes();
            trackEntity(customEntity);
            return customEntity;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            plugin.getLogger().severe(String.format("Failed to instantiate class handler %s for entity type %s. Method signatures are probably incorrect", type.entityHandler.getName(), type.name));
            entity.remove();
            return null;
        }

    }

    public void trackEntity(LeveledEntity entity) {
        removeEntity(entity.getEntity().getUniqueId());
        entityInstances.put(entity.getEntity().getUniqueId(), entity);
        if (entity instanceof Listener)
            plugin.getServer().getPluginManager().registerEvents((Listener) entity, plugin);
        entity.updateNametag();
    }

    public void removeEntity(UUID uuid) {
        LeveledEntity removed = entityInstances.remove(uuid);
        if (removed == null)
            return;

        removed.cleanup();
        if (removed instanceof Listener)
            HandlerList.unregisterAll((Listener) removed);
    }

    /**
     * Called when a creature is spawned for the first time.
     * Makes sure they have proper attributes and we are tracking them
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawnForTheFirstTime(CreatureSpawnEvent event) {
        LeveledEntity entity = getEntityInstance(event.getEntity());
        trackEntity(entity);
        entity.resetLevel();

        LeveledEntitySpawnEvent leveledSpawnEvent = new LeveledEntitySpawnEvent(entity);
        leveledSpawnEvent.callEvent();

        entity.updateAttributes();
        entity.updateNametag();

        // If this entity is holding/wearing anything, we need to fix their items to have proper stats
        EntityEquipment equipment = event.getEntity().getEquipment();
        if (equipment != null) {
            equipment.setHelmet(plugin.getItemService().ensureItemStackUpdated(equipment.getHelmet()));
            equipment.setChestplate(plugin.getItemService().ensureItemStackUpdated(equipment.getChestplate()));
            equipment.setLeggings(plugin.getItemService().ensureItemStackUpdated(equipment.getLeggings()));
            equipment.setBoots(plugin.getItemService().ensureItemStackUpdated(equipment.getBoots()));

            equipment.setItemInMainHand(plugin.getItemService().ensureItemStackUpdated(equipment.getItemInMainHand()));
            equipment.setItemInOffHand(plugin.getItemService().ensureItemStackUpdated(equipment.getItemInOffHand()));
        }
    }

    /**
     * Every time an entity is spawned, we need to construct an instance for them, we do this just to make sure
     * that at least every entity has some sort stat initialization
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawn(EntityAddToWorldEvent event) {

        // Ignore things that aren't living creatures
        if (!(event.getEntity() instanceof LivingEntity entity))
            return;

        // Ignore players, this is done somewhere else
        if (event.getEntity() instanceof Player)
            return;

        LeveledEntity leveled = getEntityInstance(entity);
        leveled.updateAttributes();
        trackEntity(leveled);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        LeveledEntity leveled = getPlayerInstance(event.getPlayer());
        leveled.updateAttributes();
        trackEntity(leveled);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDespawn(EntityRemoveFromWorldEvent event) {
        removeEntity(event.getEntity().getUniqueId());
    }

    // Handle cleaning up the secondary name tag on players when they die
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        LeveledPlayer p = getPlayerInstance(event.getPlayer());
        p.killSecondaryNametag();
    }

    // Handle spawning in the secondary name tags on players when they respawn
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerPostRespawnEvent event) {
        LeveledPlayer p = getPlayerInstance(event.getPlayer());
        p.updateNametag();
    }

    // Handle nametag updates and Damage popups for damage events
    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(EntityDamageEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        // Construct the damage popup
        DamagePopupUtil.spawnTextPopup(((LivingEntity) event.getEntity()).getEyeLocation(), (int)Math.round(event.getFinalDamage()), DamagePopupUtil.PopupType.DAMAGE);

        // Update the entity's nametag
        LeveledEntity leveled = getEntityInstance((LivingEntity) event.getEntity());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(plugin, 1L);
    }

    // Handle nametag updates and Damage popups for healing events
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegen(EntityRegainHealthEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        // Construct the heal popup
        DamagePopupUtil.spawnTextPopup(((LivingEntity) event.getEntity()).getEyeLocation(), (int)Math.round(event.getAmount()), DamagePopupUtil.PopupType.HEAL);

        // Update the entity's nametag
        LeveledEntity leveled = getEntityInstance((LivingEntity) event.getEntity());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(plugin, 1L);

    }

    // Handle nametag updates potion effect events
    @EventHandler
    public void onPotionEffectUpdate(EntityPotionEffectEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        LeveledEntity leveled = getEntityInstance((LivingEntity) event.getEntity());

        if (event.getNewEffect() != null && event.getNewEffect().getType().equals(PotionEffectType.ABSORPTION))
            DamagePopupUtil.spawnTextPopup(((LivingEntity) event.getEntity()).getEyeLocation(), (int) ((event.getNewEffect().getAmplifier()+1.0) * leveled.getMaxHp() * .05 * 4), DamagePopupUtil.PopupType.GAIN_ARMOR);

        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(plugin, 1L);
    }

    // Handle nametag updates when we switch the item in our hand
    @EventHandler
    public void onSwitchItemInHand(PlayerItemHeldEvent event) {
        LeveledEntity leveled = getEntityInstance(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(plugin, 1L);
    }

    // Handle nametag updates when we switch armor we are wearing
    @EventHandler
    public void onEquipArmor(PlayerArmorChangeEvent event) {
        LeveledEntity leveled = getEntityInstance(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                leveled.updateNametag();
            }
        }.runTaskLater(plugin, 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDiscoverNearbyEntity(PlayerMoveEvent event) {

        // Only do this every chunk change
        if (event.getFrom().getChunk().equals(event.getTo().getChunk()))
            return;

        event.getPlayer().getNearbyEntities(16, 4, 16).forEach(entity -> {
            if (!(entity instanceof Player) && entity instanceof LivingEntity)
                getEntityInstance((LivingEntity) entity).brightenNametag();
        });

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamageEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        LeveledEntity leveled = getEntityInstance(living);
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
        if (leveled instanceof EnemyEntity enemy)
            enemy.addDamageDealtByEntity(player, (int)event.getFinalDamage());

        leveled.brightenNametag();
    }
}
