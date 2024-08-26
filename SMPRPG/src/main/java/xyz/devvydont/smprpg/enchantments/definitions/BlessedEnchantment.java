package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;

public class BlessedEnchantment extends CustomEnchantment implements Listener {

    public BlessedEnchantment(String id) {
        super(id);
    }

    public static int getPercentageIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 20;
            case 2 -> 50;
            case 3 -> 80;
            case 4 -> 110;
            case 5 -> 150;
            default -> getPercentageIncrease(5) + 100 * (level-5);
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Blessed");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases damage dealt by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getPercentageIncrease(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" while in the ").color(NamedTextColor.GRAY))
                .append(Component.text("Nether").color(NamedTextColor.RED));
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
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 29;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageDealtInNether(CustomEntityDamageByEntityEvent event) {

        // Skip if not in nether
        if (!event.getDamaged().getWorld().getEnvironment().equals(World.Environment.NETHER))
            return;

        // Skip entity if they aren't alive
        if (!(event.getDealer() instanceof LivingEntity dealer))
            return;

        int level = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.HAND, dealer.getEquipment());
        if (level <= 0)
            return;

        double multiplier = 1.0 + (getPercentageIncrease(level) / 100.0);
        event.multiplyDamage(multiplier);
    }
}
