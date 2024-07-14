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
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.Collection;
import java.util.List;

public class HeartyEnchantment extends CustomEnchantment implements AttributeEnchantment {

    public static int getHealthIncrease(int level) {
        return switch (level) {
          case 0 -> 0;
          case 1 -> 5;
          case 2 -> 10;
          case 3 -> 20;
          case 4 -> 35;
          case 5 -> 50;
          case 6 -> 60;
          case 7 -> 80;
          case 8 -> 100;
          case 9 -> 125;
          case 10 -> 150;
          default -> getHealthIncrease(10) + 25*(level - 10);
        };
    }

    public HeartyEnchantment(String id) {
        super(id);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Hearty");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases max HP by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getHealthIncrease(getLevel())).color(NamedTextColor.GREEN))
                .append(ComponentUtil.getColoredComponent(Symbols.HEART, NamedTextColor.RED));
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
        return 10;
    }

    @Override
    public int getWeight() {
        return 4;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 3);
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 30;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_MAX_HEALTH, getHealthIncrease(getLevel()))
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 2 + 1;
    }
}
