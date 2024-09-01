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

public class StabilizedEnchantment extends CustomEnchantment implements Listener {

    public StabilizedEnchantment(String id) {
        super(id);
    }

    public static int getPercentageIncrease(int level) {
        return BlessedEnchantment.getPercentageIncrease(level);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Stabilized");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases damage dealt by "),
            ComponentUtils.create("+" + getPercentageIncrease(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" while in "),
            ComponentUtils.create("The End", NamedTextColor.LIGHT_PURPLE)
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
        return 44;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageDealtInNether(CustomEntityDamageByEntityEvent event) {

        // Skip if not in nether
        if (!event.getDamaged().getWorld().getEnvironment().equals(World.Environment.THE_END))
            return;

        // Skip entity if they aren't alive
        if (!(event.getDealer() instanceof LivingEntity dealer))
            return;

        int level = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.HAND, dealer.getEquipment());
        if (level <= 0)
            return;

        double multiplier = 1.0 + getPercentageIncrease(level) / 100.0;
        event.multiplyDamage(multiplier);
    }

}
