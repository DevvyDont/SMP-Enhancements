package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
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
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class BlastProtectionEnchantment extends VanillaEnchantment implements AttributeEnchantment, Listener {

    public static int getExplosiveProtectionPercent(int level) {
        return (int) (level * 2.5);
    }

    public BlastProtectionEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blast Protection");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases explosion resistance by "),
            ComponentUtils.create("+" + getExplosiveProtectionPercent(getLevel()) + "%", NamedTextColor.GREEN)
        );
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
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 12;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                new ScalarAttributeEntry(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE, getExplosiveProtectionPercent(getLevel())/100.0)
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 5;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onExplosiveDamageTaken(EntityDamageEvent event) {

        // Ignore non explosions
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
        && !event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION))
            return;

        // Ignore if the entity can't wear equipment
        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        int blast = EnchantmentUtil.getWornEnchantLevel(getEnchantment(), living.getEquipment());
        if (blast <= 0)
            return;

        double multiplier = Math.max(0, 1.0 - (getExplosiveProtectionPercent(blast) / 100.0));
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * multiplier);
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<@NotNull Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.FIRE_PROTECTION, EnchantmentKeys.PROTECTION, EnchantmentKeys.PROJECTILE_PROTECTION);
    }
}
