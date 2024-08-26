package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;

public class UnbreakingEnchantment extends VanillaEnchantment {

    public UnbreakingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getDurabilityIgnoreChance(int level) {
        return (int) ((1-(1.0/(level+1)))*100);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Unbreaking");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Durability is ignored ").color(NamedTextColor.GRAY)
                .append(Component.text(getDurabilityIgnoreChance(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" of the time when used").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_DURABILITY;
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
        return EquipmentSlotGroup.ANY;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }
}
