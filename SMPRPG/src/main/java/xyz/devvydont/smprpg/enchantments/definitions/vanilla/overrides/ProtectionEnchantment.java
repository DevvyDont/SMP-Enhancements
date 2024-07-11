package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;

import java.util.Collection;
import java.util.List;

public class ProtectionEnchantment extends VanillaEnchantment implements AttributeEnchantment {

    public static int getProtection(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1, 2, 3, 4 -> level * 10;
            case 5, 6, 7 -> level * 15;
            case 8, 9 -> level * 20;
            default -> level * 25;
        };
    }


    public ProtectionEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Protection");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases defense by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getProtection(getLevel())).color(NamedTextColor.GREEN)
                );
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
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of();
    }

    @Override
    public int getDefense() {
        return getProtection(getLevel());
    }

    @Override
    public int getPowerRating() {
        return getLevel();
    }
}
