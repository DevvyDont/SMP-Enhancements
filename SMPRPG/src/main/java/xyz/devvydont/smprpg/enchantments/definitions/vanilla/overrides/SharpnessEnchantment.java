package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;

import java.util.Collection;
import java.util.List;

public class SharpnessEnchantment extends VanillaEnchantment implements AttributeEnchantment {

    public SharpnessEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getDamageIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 25;
            case 2 -> 50;
            case 3 -> 75;
            case 4 -> 110;
            case 5 -> 150;
            case 6 -> 200;
            case 7 -> 275;
            case 8 -> 350;
            case 9 -> 425;
            case 10 -> 500;
            default -> getDamageIncrease(10) + 100 * (level-10);
        };
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(Attribute.GENERIC_ATTACK_DAMAGE, getDamageIncrease(getLevel()))
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel()/2;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Sharpness");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases base damage by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getDamageIncrease(getLevel())).color(NamedTextColor.GREEN)
                );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON;
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
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.SMITE, EnchantmentKeys.BANE_OF_ARTHROPODS);
    }
}
