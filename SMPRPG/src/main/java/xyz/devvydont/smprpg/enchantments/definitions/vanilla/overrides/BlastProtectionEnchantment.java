package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;

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
    public Component getDisplayName() {
        return Component.text("Blast Protection");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases explosion resistance by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getExplosiveProtectionPercent(getLevel()) + "%").color(NamedTextColor.GREEN));
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
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 3);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 6;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new ScalarAttributeEntry(Attribute.GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE, getExplosiveProtectionPercent(getLevel())/100.0)
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
        event.setDamage(event.getDamage() * multiplier);
    }
}
