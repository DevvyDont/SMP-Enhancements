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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class FlameEnchantment extends UnchangedEnchantment {

    public FlameEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getSecondsOfBurn(int level) {
        return level * 4;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Flame");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Ignites enemies for ").color(NamedTextColor.GRAY)
                .append(Component.text(getSecondsOfBurn(getLevel()) + "s").color(NamedTextColor.GOLD));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_BOW;
    }

    @Override
    public int getSkillRequirement() {
        return 19;
    }
}
