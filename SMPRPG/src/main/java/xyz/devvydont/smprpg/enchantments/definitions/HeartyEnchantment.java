package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.Collection;
import java.util.List;

public class HeartyEnchantment extends CustomEnchantment implements AttributeEnchantment {

    public static int getHealthIncrease(int level) {
        return switch (level) {
          case 0 -> 0;
          case 1 -> 10;
          case 2 -> 20;
          case 3 -> 30;
          case 4 -> 40;
          case 5 -> 50;
          case 6 -> 65;
          case 7 -> 80;
          case 8 -> 100;
          case 9 -> 120;
          case 10 -> 150;
          default -> getHealthIncrease(10) + 25*(level - 10);
        };
    }

    public HeartyEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Hearty");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases max HP by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getHealthIncrease(getLevel())).color(NamedTextColor.GREEN))
                .append(ComponentUtils.getColoredComponent(Symbols.HEART, NamedTextColor.RED));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_ARMOR;
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
        return EnchantmentRarity.COMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 10;
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
