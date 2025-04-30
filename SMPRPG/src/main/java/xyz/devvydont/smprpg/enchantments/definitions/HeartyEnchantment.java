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
          case 4 -> 45;
          case 5 -> 60;
          case 6 -> 75;
          case 7 -> 100;
          case 8 -> 125;
          case 9 -> 150;
          case 10 -> 200;
          default -> getHealthIncrease(10) + 50*(level - 10);
        };
    }

    public HeartyEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Hearty");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases max HP by "),
            ComponentUtils.create("+" + getHealthIncrease(getLevel()), NamedTextColor.GREEN),
            ComponentUtils.create(Symbols.HEART, NamedTextColor.RED)
        );
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
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.MAX_HEALTH, getHealthIncrease(getLevel()))
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 2 + 1;
    }
}
