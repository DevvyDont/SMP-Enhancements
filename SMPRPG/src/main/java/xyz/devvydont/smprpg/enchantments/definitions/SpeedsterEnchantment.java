package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;

import java.util.Collection;
import java.util.List;

public class SpeedsterEnchantment extends CustomEnchantment implements AttributeEnchantment {

    public static int getSpeedPercentageIncrease(int level) {
        return level * 5;
    }

    public SpeedsterEnchantment(String id) {
        super(id);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Speedster");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases speed by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getSpeedPercentageIncrease(getLevel()) + "%").color(NamedTextColor.GREEN));
    }

    @Override
    public RegistryKeySet<ItemType> getSupportedItems(RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
        return event.getOrCreateTag(ItemTypeTagKeys.ENCHANTABLE_ARMOR);
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
        return 4;
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
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new ScalarAttributeEntry(Attribute.GENERIC_MOVEMENT_SPEED, getSpeedPercentageIncrease(getLevel()) / 100.0)
        );
    }

    @Override
    public int getDefense() {
        return 0;
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 2 + 1;
    }
}
