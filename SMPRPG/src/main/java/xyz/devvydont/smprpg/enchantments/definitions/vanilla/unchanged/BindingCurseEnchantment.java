package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class BindingCurseEnchantment extends UnchangedEnchantment {

    public BindingCurseEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Curse of Binding");
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return NamedTextColor.RED;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Cannot be ").color(NamedTextColor.GRAY)
                .append(Component.text("removed").color(NamedTextColor.DARK_RED))
                .append(Component.text(" once worn").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }

    @Override
    public int getSkillRequirementToAvoid() {
        return 10;
    }
}
