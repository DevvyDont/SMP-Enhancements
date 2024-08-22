package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.*;

public class InfernoSaber extends CustomAttributeItem implements Listener {

    public static final int COOLDOWN = 3;
    public static final int COST = 150;
    public static final int DAMAGE = 15_000;
    public static final double EXPLOSION_RADIUS = 5;

    // We need a reference to projectiles that we shoot so that we can handle them at different stages in its life
    // since PDCs do not work during the EntityExplodeEvent
    private static Map<UUID, Entity> projectiles = new HashMap<>();

    public InfernoSaber(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private boolean isInfernoProjectile(Entity projectile) {
        return projectiles.containsKey(projectile.getUniqueId());
    }

    private void setInfernoProjectile(Entity projectile) {
        projectiles.put(projectile.getUniqueId(), projectile);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 300),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.6),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .25)
        );
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> lines = new ArrayList<>(super.getDescriptionComponent(meta));
        lines.add(Component.empty());
        lines.add(AbilityUtil.getAbilityComponent("Hot Shot (Right Click)"));
        lines.add(ComponentUtil.getDefaultText("Shoot a ").append(ComponentUtil.getColoredComponent("fireball", NamedTextColor.GOLD)).append(ComponentUtil.getDefaultText(" in the direction")));
        lines.add(ComponentUtil.getDefaultText("you are looking that deals ").append(ComponentUtil.getColoredComponent(DAMAGE + "", NamedTextColor.RED)).append(ComponentUtil.getDefaultText(" damage")));
        lines.add(AbilityUtil.getHealthCostComponent(COST));
        lines.add(AbilityUtil.getCooldownComponent(COOLDOWN + "s"));
        return lines;
    }

    @Override
    public int getPowerRating() {
        return InfernoArmorSet.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.WEAPON;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    public boolean canUse(Player player) {
        return player.getHealth() > COST;
    }

    public void summonFireball(Player player) {
        Projectile projectile = player.launchProjectile(Fireball.class, player.getLocation().getDirection().normalize().multiply(2));
        SMPRPG.getInstance().getEntityDamageCalculatorService().setBaseProjectileDamage(projectile, DAMAGE);
        player.damage(COST, DamageSource.builder(DamageType.MAGIC).withCausingEntity(player).withDirectEntity(player).build());
        player.setCooldown(getCustomItemType().material, COOLDOWN*20);
        setInfernoProjectile(projectile);
    }

    /*
     * When we right click with the item in our hand, we need to summon a fireball.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractWhileHoldingBlade(PlayerInteractEvent event) {

        // Is this the blade?
        ItemStack item = event.getItem();
        if (!isItemOfType(item))
            return;

        // Are we right-clicking?
        if (!event.getAction().isRightClick())
            return;

        // Are we right-clicking a block?
        if (event.getClickedBlock() != null)
            return;

        // Are we on cooldown?
        if (event.getPlayer().getCooldown(getCustomItemType().material) > 0)
            return;

        event.setCancelled(true);

        // Do we have the health to use this?
        if (!canUse(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatUtil.getErrorMessage("You do not have enough life in you to use this item!"));
            return;
        }

        summonFireball(event.getPlayer());
    }

    /*
     * When the fireball explodes, we need to ensure that it does not damage terrain and also damage nearby entities
     * at a penalty (for missing)
     */
    @EventHandler
    public void onFireballExplode(EntityExplodeEvent event) {

        // If this isn't an inferno projectile we don't care
        if (!isInfernoProjectile(event.getEntity()))
            return;

        Player source = null;
        if (event.getEntity() instanceof Projectile projectile && projectile.getShooter() instanceof Player player)
            source = player;

        // Cancel the actual explosion part and create a safe one that doesn't break things.
        event.setCancelled(true);
        event.blockList().clear();
        for (LivingEntity living : event.getLocation().getNearbyLivingEntities(EXPLOSION_RADIUS)) {
            double falloff = EXPLOSION_RADIUS - event.getLocation().distance(living.getLocation());
            falloff /= EXPLOSION_RADIUS;
            double damage = DAMAGE * falloff;

            if (falloff < 0)
                continue;

            if (source != null)
                living.setKiller(source);

            living.damage(
                    damage,
                    DamageSource.builder(DamageType.MAGIC).build()
            );
        }
        event.getLocation().createExplosion(0, false, false);
    }

}
