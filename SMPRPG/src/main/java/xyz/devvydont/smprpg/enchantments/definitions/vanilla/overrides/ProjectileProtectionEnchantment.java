package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;

public class ProjectileProtectionEnchantment extends VanillaEnchantment implements Listener {

    public static int getProjectileResistancePercent(int level) {
        return (int) (level * 2.0);
    }

    public ProjectileProtectionEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Projectile Protection");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases projectile resistance by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getProjectileResistancePercent(getLevel()) + "%").color(NamedTextColor.GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_ARMOR;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 14;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFireDamage(EntityDamageEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        int projRes = EnchantmentUtil.getWornEnchantLevel(getEnchantment(), living.getEquipment());
        if (projRes <= 0)
            return;

        double multiplier = 1 - getProjectileResistancePercent(projRes) / 100.0;
        event.setDamage(event.getDamage() * multiplier);
    }
}
