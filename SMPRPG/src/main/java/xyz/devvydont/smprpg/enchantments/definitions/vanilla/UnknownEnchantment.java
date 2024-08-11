package xyz.devvydont.smprpg.enchantments.definitions.vanilla;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class UnknownEnchantment extends VanillaEnchantment {


    public UnknownEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getVanillaEnchantment().displayName(0);
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Unknown description");
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_DURABILITY;
    }

    @Override
    public int getAnvilCost() {
        return getVanillaEnchantment().getAnvilCost();
    }

    @Override
    public int getMaxLevel() {
        return getVanillaEnchantment().getMaxLevel();
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ANY;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }
}
