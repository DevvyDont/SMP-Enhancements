package xyz.devvydont.smprpg.enchantments.definitions;

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
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class BlessedEnchantment extends CustomEnchantment implements Listener {

    public BlessedEnchantment(String id) {
        super(id);
    }

    public static int getPercentageIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 10;
            case 2 -> 25;
            case 3 -> 40;
            case 4 -> 65;
            case 5 -> 80;
            default -> getPercentageIncrease(5) + 80 * (level-5);
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blessed");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
                ComponentUtils.create("Increases damage dealt by "),
                ComponentUtils.create("+" + getPercentageIncrease(getLevel()) + "%", NamedTextColor.GREEN),
                ComponentUtils.create(" while in the "),
                ComponentUtils.create("Nether", NamedTextColor.RED)
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
        return EnchantmentRarity.RARE.getWeight();
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
