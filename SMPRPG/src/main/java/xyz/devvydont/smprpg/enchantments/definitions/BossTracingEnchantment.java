package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class BossTracingEnchantment extends CustomEnchantment implements Listener {

    public static int getActivationDistance(int level) {
        return (level-1) * 15 + 5;
    }

    public static int getVelocityDecay(int level) {
        return level * 3 + 92;
    }

    public static int getTimeout(int level) {
        return level * 5 + 5;
    }

    public BossTracingEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Tracing");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtil.getDefaultText("Arrows fired will home onto bosses ")
                .append(ComponentUtil.getColoredComponent(getActivationDistance(getLevel()) + "m", NamedTextColor.GREEN))
                .append(ComponentUtil.getDefaultText(" away for "))
                .append(ComponentUtil.getColoredComponent(getTimeout(getLevel()) + "s", NamedTextColor.GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_BOW;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 35;
    }

    public class HomingArrowTask extends BukkitRunnable {

        private final Entity projectile;
        private final int aggression;
        private LivingEntity target;

        public HomingArrowTask(Entity projectile, int aggression) {
            this.projectile = projectile;
            this.aggression = aggression;
        }

        @Override
        public void run() {

            // If the arrow doesn't exist anymore or is 10 sec old cancel the task
            if (!projectile.isValid() || projectile.isOnGround() || projectile.getTicksLived() > 20 * getTimeout(aggression)) {
                this.cancel();
                return;
            }

            // If the target died somehow, give up
            if (target != null && !target.isValid()) {
                this.cancel();
                return;
            }

            // If we have a target, adjust direction
            if (target != null) {
                double oldSpeed = projectile.getVelocity().length();
                projectile.setVelocity(target.getLocation().toVector().subtract(projectile.getLocation().toVector()));
                // Here we need to make the speed match the old speed
                double velocityMultiplier = oldSpeed / projectile.getVelocity().length();
                velocityMultiplier *= getVelocityDecay(aggression) / 100.0;
                projectile.setVelocity(projectile.getVelocity().multiply(velocityMultiplier));

                projectile.getWorld().playEffect(projectile.getLocation(), Effect.ENDER_SIGNAL, 0);
                projectile.getWorld().playSound(projectile.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, .4f, 1);
                return;
            }

            // We need to find a target
            // Loop through nearby entities
            for (LivingEntity entity : projectile.getWorld().getNearbyLivingEntities(projectile.getLocation(), getActivationDistance(aggression))) {

                // If this isn't a boss then skip
                if (!(SMPRPG.getInstance().getEntityService().getEntityInstance(entity) instanceof BossInstance boss))
                    continue;

                // If we don't have line of sight we can't use this entity
                if (!entity.hasLineOfSight(projectile))
                    continue;

                // Valid entity!
                target = entity;
                break;
            }

        }
    }

    @EventHandler
    public void onArrowFire(EntityShootBowEvent event) {

        int tracing = EnchantmentUtil.getEnchantLevel(getEnchantment(), event.getBow());
        if (tracing <= 0)
            return;

        new HomingArrowTask(event.getProjectile(), tracing).runTaskTimer(SMPRPG.getInstance(), 10, 1);
    }
}
