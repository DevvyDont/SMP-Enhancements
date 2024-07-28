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
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class ChannelingEnchantment extends UnchangedEnchantment {

    public ChannelingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Channeling");
    }

    @Override
    public Component getDescription() {
        return Component.text("Summon ").color(NamedTextColor.GRAY)
                .append(Component.text("lightning").color(NamedTextColor.YELLOW))
                .append(Component.text(" during ").color(NamedTextColor.GRAY))
                .append(Component.text("thunderstorms").color(NamedTextColor.AQUA));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_TRIDENT;
    }

    @Override
    public int getSkillRequirement() {
        return 42;
    }
}
