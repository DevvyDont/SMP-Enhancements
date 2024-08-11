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

public class FireProtectionEnchantment extends VanillaEnchantment implements Listener {

    public static int getFireResistancePercent(int level) {
        return (int) (level * 2.5);
    }

    public static boolean isFireCause(EntityDamageEvent.DamageCause cause) {
        return switch (cause) {
            case FIRE, FIRE_TICK, CAMPFIRE, LAVA, LIGHTNING, HOT_FLOOR, MELTING, DRAGON_BREATH -> true;
            default -> false;
        };
    }

    public FireProtectionEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Fire Protection");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases fire resistance by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getFireResistancePercent(getLevel()) + "%").color(NamedTextColor.GREEN));
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
        return 9;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFireDamage(EntityDamageEvent event) {

        if (!isFireCause(event.getCause()))
            return;

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        int fireRes = EnchantmentUtil.getWornEnchantLevel(getEnchantment(), living.getEquipment());
        if (fireRes <= 0)
            return;

        double multiplier = 1 - getFireResistancePercent(fireRes) / 100.0;
        event.setDamage(event.getDamage() * multiplier);
    }
}
