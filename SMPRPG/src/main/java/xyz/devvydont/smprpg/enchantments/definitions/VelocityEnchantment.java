package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;

public class VelocityEnchantment extends CustomEnchantment implements Listener {

    public static int getSpeedIncrease(int level) {
        return level * 4;
    }

    public VelocityEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Velocity");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases arrow speed by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getSpeedIncrease(getLevel()) + "%").color(NamedTextColor.GREEN));
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
        return 5;
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 41;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onShootBow(EntityShootBowEvent event) {

        if (event.getBow() == null)
            return;

        int velocity = event.getBow().getEnchantmentLevel(getEnchantment());
        if (velocity <= 0)
            return;

        double speedMult = getSpeedIncrease(velocity) / 100.0 * velocity + 1.0;
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(speedMult));
    }
}
