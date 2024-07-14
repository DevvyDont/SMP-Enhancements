package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;

public class SmiteEnchantment extends VanillaEnchantment implements Listener {

    public static int getPercentageIncrease(int level) {
        return level * 20;
    }

    public static boolean isUndead(EntityType type) {
        return switch (type) {
            case ZOMBIE, ZOMBIE_VILLAGER, ZOMBIE_HORSE, ZOMBIFIED_PIGLIN, DROWNED, HUSK, ZOGLIN, WITHER,
                 WITHER_SKELETON, PHANTOM, SKELETON, SKELETON_HORSE, BOGGED, STRAY -> true;
            default -> false;
        };
    }

    public SmiteEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Smite");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases damage dealt by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getPercentageIncrease(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" against ").color(NamedTextColor.GRAY))
                .append(Component.text("the undead").color(NamedTextColor.RED));
    }

    @Override
    public RegistryKeySet<ItemType> getSupportedItems(RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
        return event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON);
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
        return 2;
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
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 2;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageArthropod(CustomEntityDamageByEntityEvent event) {

        // Skip non arthropods
        if (!isUndead(event.getDamaged().getType()))
            return;

        // Skip entity if they aren't alive
        if (!(event.getDealer() instanceof LivingEntity dealer))
            return;

        int level = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.HAND, dealer.getEquipment());
        if (level <= 0)
            return;

        double multiplier = 1.0 + (getPercentageIncrease(level) / 100.0);
        event.setFinalDamage(event.getFinalDamage() * multiplier);
    }
}
