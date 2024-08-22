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
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;

import java.util.Collection;
import java.util.List;

public class ImpalingEnchantment extends VanillaEnchantment implements Listener {

    public ImpalingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getDamagePercentageMultiplier(int level) {
        return SmiteEnchantment.getPercentageIncrease(level);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Impaling");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases base damage by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getDamagePercentageMultiplier(getLevel()) + "%").color(NamedTextColor.GREEN)
                .append(Component.text(" against wet enemies").color(NamedTextColor.GRAY))
                );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_WEAPON;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 28;
    }

    @EventHandler
    public void onWaterDamage(CustomEntityDamageByEntityEvent event) {

        if (!(event.getDealer() instanceof LivingEntity living))
            return;

        if (!event.getDamaged().isInWaterOrRain())
            return;

        int impalingLevel = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.MAINHAND, living.getEquipment());
        if (impalingLevel <= 0)
            return;

        double multiplier = 1 - getDamagePercentageMultiplier(impalingLevel) / 100.0;
        event.setFinalDamage(event.getFinalDamage() * multiplier);
    }
}
