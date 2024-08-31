package xyz.devvydont.smprpg.entity.bosses;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.effects.tasks.TetheredEffect;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomBossInstance;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;
import xyz.devvydont.smprpg.util.particles.ParticleUtil;

import java.util.*;

public class BlazeBoss extends CustomBossInstance implements Listener {

    private enum BlazeBossPhase {
        STARTING,
        DEFAULT,
        MOBBING,
    }

    private final static double GOAL_SCALE = 7.5;
    private final static double CHARGE_PROGRESS_PER_TICK = .003;
    private final static int MAX_MINIONS = 10;

    /*
     * A collection of minions spawned by the boss. Used so we don't spawn too many, to use in instances where we want
     * to track them for certain mechanics.
     */
    private final Map<UUID, LeveledEntity> minions = new HashMap<>();

    // The current tick this boss has been alive for, used for certain aspects during the fight
    private int tick = 0;

    private BlazeBossPhase phase = BlazeBossPhase.STARTING;

    public BlazeBoss(SMPRPG plugin, Entity entity, CustomEntityType type) {
        super(plugin, entity, type);
    }

    public Blaze getBlazeEntity() {
        return (Blaze) entity;
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return BossBar.bossBar(Component.empty(), 1.0f, BossBar.Color.RED, BossBar.Overlay.NOTCHED_20, Set.of(BossBar.Flag.DARKEN_SCREEN));
    }

    @Override
    public void tick() {
        super.tick();
        tick++;

        // The main logic of the boss.
        // If there are at least 5 mobs alive, enter the mobbing phase
        if (minions.size() >= (MAX_MINIONS/2))
            phase = BlazeBossPhase.MOBBING;

        // If the blaze cannot be hit, set its bar color to white, otherwise red.
        if (bossBar != null)
            bossBar.color(entity.isInvulnerable() ? BossBar.Color.WHITE : BossBar.Color.RED);

        // If we are in the mobbing phase, we are invulnerable
        if (phase == BlazeBossPhase.MOBBING) {
            entity.setInvulnerable(true);
            getBlazeEntity().setAI(false);

            for (LeveledEntity minion : minions.values())
                ParticleUtil.spawnParticlesBetweenTwoPoints(Particle.ENCHANTED_HIT, entity.getWorld(), getBlazeEntity().getEyeLocation().toVector(), minion.getEntity().getLocation().add(0, 1, 0).toVector(), 50);

            // No minions left? Back to default
            if (minions.isEmpty())
                phase = BlazeBossPhase.DEFAULT;

            return;
        }

        // If we are in the default phase, don't do anything. This is basically the "DPS" phase
        // We can also move from one phase to another here
        if (phase == BlazeBossPhase.DEFAULT) {
            entity.setInvulnerable(false);
            getBlazeEntity().setAI(true);
            return;
        }

        // If we are in the starting phase, we are charging up similarly to how the wither does.
        if (phase == BlazeBossPhase.STARTING) {

            double chargeProgress = tick * CHARGE_PROGRESS_PER_TICK + .001;

            entity.setInvulnerable(true);
            getBlazeEntity().setAI(false);
            setHealthPercentage(Math.min(chargeProgress, 1.0));
            updateBaseAttribute(Attribute.GENERIC_SCALE, chargeProgress * GOAL_SCALE);

            // Are we done charging?
            if (chargeProgress >= 1.0) {
                entity.setInvulnerable(false);
                getBlazeEntity().setAI(true);
                phase = BlazeBossPhase.DEFAULT;
                entity.getWorld().strikeLightningEffect(getBlazeEntity().getEyeLocation());
                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_DEATH, 10f, .25f);
            }
            return;
        }

    }

    @Override
    public long getTimeLimit() {
        return 60L * 5L;
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        for (LeveledEntity leveledEntity : minions.values().stream().toList())
            if (leveledEntity.getEntity().isValid() && leveledEntity.getEntity() instanceof LivingEntity living)
                living.damage(999_999_999);
        minions.clear();
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        updateBaseAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(

                // Common drops
                new QuantityLootDrop(ItemService.getItem(CustomItemType.CHILI_PEPPER), 1, 3, this),
                new QuantityLootDrop(ItemService.getItem(Material.BLAZE_ROD), 2, 6, this),
                new QuantityLootDrop(ItemService.getItem(Material.IRON_INGOT), 2, 6, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.PREMIUM_BLAZE_ROD), 2, this),

                // Pity system drops
                new ChancedItemDrop(ItemService.getItem(CustomItemType.ENCHANTED_IRON), 20, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.ENCHANTED_IRON_BLOCK), 90, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.IRON_SINGULARITY), 2000, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.ENCHANTED_BLAZE_ROD), 25, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.SCORCHING_STRING), 90, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.BOILING_INGOT), 20, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.INFERNO_REMNANT), 40, this),
                new QuantityLootDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.INFERNO_RESIDUE), 1, 2, this),

                // Gear drops
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_HELMET), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_CHESTPLATE), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_LEGGINGS), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_BOOTS), 150, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_SABER), 175, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_SHORTBOW), 175, this),

                // Chance to summon again
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_ARROW), 50, this)
        );
    }

    public int getMinionLimit() {
        return phase == BlazeBossPhase.MOBBING ? MAX_MINIONS : (MAX_MINIONS/2);
    }

    /*
     * When our boss shoots a fireball and it explodes, it will summon a normal phoenix enemy
     * Also, if it hits a player directly, they get "tethered"
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBossFireballExplode(ProjectileHitEvent event) {

        if (!entity.equals(event.getEntity().getShooter()))
            return;

        // Did we hit a player directly? if so, we need to tether them.
        if (event.getHitEntity() instanceof Player hitPlayer)
            plugin.getSpecialEffectsService().giveEffect(hitPlayer, new TetheredEffect(plugin.getSpecialEffectsService(), hitPlayer, getBlazeEntity(), 5));

        // Do we have too many?
        if (minions.size() >= getMinionLimit())
            return;

        // RNG check
        if (Math.random() < .5)
            return;

        // This fireball is from our blaze boss.
        Location spawn = event.getEntity().getLocation();
        LeveledEntity mob = plugin.getEntityService().spawnCustomEntity(CustomEntityType.PHOENIX, spawn);
        if (mob == null || mob.getEntity() == null)
            return;

        minions.put(mob.getEntity().getUniqueId(), mob);
    }

    /*
     * When a player damages the boss....
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamageBoss(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        // Was the damaged entity the boss?
        if (!event.getEntity().equals(entity))
            return;

        // Is the boss already under half HP?
        if (getHealthPercentage() <= .5)
            return;

        double threshold = getMaxHp() / 2;
        // Are we hitting the half hp threshold?
        if (!(getBlazeEntity().getHealth() - event.getDamage() < threshold))
            return;

        // Set the damage to hit the threshold exactly and spawn a bunch of minions
        event.setDamage(living.getHealth()-threshold);

        if (getActivelyInvolvedPlayers().isEmpty())
            return;

        getBlazeEntity().getWorld().playSound(getBlazeEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10f, .2f);

        int minionsSpawned = 0;

        while (minionsSpawned < 10) {
            for (Player p : getActivelyInvolvedPlayers()) {
                spawnMinion(p.getEyeLocation().subtract(p.getLocation().getDirection().normalize().multiply(3).add(new Vector(Math.random() * 6 - 3, 0, Math.random() * 6 - 3))));
                minionsSpawned++;
            }
        }

        phase = BlazeBossPhase.MOBBING;
    }

    private void spawnMinion(Location location) {
        LeveledEntity mob = plugin.getEntityService().spawnCustomEntity(CustomEntityType.PHOENIX, location);
        if (mob == null || mob.getEntity() == null)
            return;

        minions.put(mob.getEntity().getUniqueId(), mob);
    }

    /*
     * When a minion dies, spawn a fire particle trail, and damage the boss
     */
    @EventHandler
    public void onMinionDeath(EntityDeathEvent event) {

        // Was the killed entity a minion?
        if (!minions.containsKey(event.getEntity().getUniqueId()))
            return;

        // Remove the reference and handle any logic
        minions.remove(event.getEntity().getUniqueId());
        event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 2);

        // If we were in the mobbing phase and this is the last minion, set phase back to default
        if (phase == BlazeBossPhase.MOBBING && minions.isEmpty())
            phase = BlazeBossPhase.DEFAULT;

        // Was there a cause?
        if (event.getDamageSource().getCausingEntity() == null || event.getDamageSource().getDirectEntity() == null)
            return;

        // Damage the boss for how much health the minion had / 2
        double damage = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        boolean invulnState = entity.isInvulnerable();
        entity.setInvulnerable(false);
        getBlazeEntity().damage(damage * .5, DamageSource.builder(DamageType.MAGIC).withCausingEntity(event.getDamageSource().getCausingEntity()).withDirectEntity(event.getDamageSource().getDirectEntity()).build());
        entity.setInvulnerable(invulnState);
        ParticleUtil.spawnParticlesBetweenTwoPoints(Particle.FLAME, event.getEntity().getWorld(), getBlazeEntity().getEyeLocation().toVector(), event.getEntity().getEyeLocation().toVector(), 100);
    }
}
