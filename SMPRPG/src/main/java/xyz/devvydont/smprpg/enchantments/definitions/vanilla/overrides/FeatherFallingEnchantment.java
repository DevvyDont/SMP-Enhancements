package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;

import java.util.Collection;
import java.util.List;

public class FeatherFallingEnchantment extends VanillaEnchantment implements AttributeEnchantment, Listener {

    public static int getFallResistPercent(int level) {
        return Math.min(Math.max(0, level * 9), 99);
    }

    public FeatherFallingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Feather Falling");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Resists ").color(NamedTextColor.GRAY)
                .append(Component.text(getFallResistPercent(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" of fall damage").color(NamedTextColor.GRAY))
                ;
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_FOOT_ARMOR;
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
        return 4;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.FEET;
    }

    @Override
    public int getSkillRequirement() {
        return 10;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return null;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new MultiplicativeAttributeEntry(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER, -getFallResistPercent(getLevel()) / 100.0),
                new AdditiveAttributeEntry(Attribute.GENERIC_SAFE_FALL_DISTANCE, getLevel()*2)
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel()/5;
    }

    @EventHandler
    public void onFallDamageTaken(EntityDamageEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        int featherFallingLevel = EnchantmentUtil.getWornEnchantLevel(Enchantment.FEATHER_FALLING, living.getEquipment());
        if (featherFallingLevel <= 0)
            return;

        double resist = 1 - getFallResistPercent(featherFallingLevel) / 100.0;
        event.setDamage(EntityDamageEvent.DamageModifier.BASE, event.getDamage() * resist);
        event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_WOOL_BREAK, 1, 1.5f);
    }
}
