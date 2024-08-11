package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class WindBurstEnchantment extends UnchangedEnchantment {

    public WindBurstEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_MACE;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Wind Burst");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Propel upwards ").color(NamedTextColor.GRAY)
                .append(Component.text(getLevel() * 5)).color(NamedTextColor.GREEN)
                .append(Component.text(" blocks when dealing damage").color(NamedTextColor.GRAY));
    }

    @Override
    public int getSkillRequirement() {
        return 62;
    }
}
