package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;

public class SnipeEnchantment extends CustomEnchantment implements Listener {

    public static int getDamageIncreasePercentPerBlock(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 3;
            case 3 -> 5;
            default -> getDamageIncreasePercentPerBlock(3) + 2 * level;
        };
    }

    public SnipeEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Snipe");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases damage by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getDamageIncreasePercentPerBlock(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" for every block the arrow travels"));
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
        return 3;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 48;
    }

    @EventHandler
    public void onArrowHit(CustomEntityDamageByEntityEvent event) {

        if (!event.getVanillaCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
            return;

        if (!(event.getProjectile() instanceof AbstractArrow arrow))
            return;

        if (!(event.getDealer() instanceof LivingEntity living) || living.getEquipment() == null)
            return;

        // Retrieve the higher snipe level of the two hands to determine which one to use
        int snipeLevels;
        int mainHandSnipeLevels = living.getEquipment().getItemInMainHand().getEnchantmentLevel(getEnchantment());
        int offHandSnipeLevels = living.getEquipment().getItemInOffHand().getEnchantmentLevel(getEnchantment());
        snipeLevels = Math.max(mainHandSnipeLevels, offHandSnipeLevels);
        if (snipeLevels <= 0)
            return;

        double multiplier = 1.0 + getDamageIncreasePercentPerBlock(snipeLevels) / 100.0 * living.getLocation().distance(arrow.getLocation());
        event.multiplyDamage(multiplier);
    }
}
