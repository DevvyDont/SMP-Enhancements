package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class DepthStriderEnchantment extends UnchangedEnchantment {

    public static int getMovementPercentage(int level) {
        return switch (level) {
          case 0 -> 0;
          case 1 -> 33;
          case 2 -> 66;
          default -> 100;
        };
    }

    public DepthStriderEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Depth Strider");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Walk underwater at ").color(NamedTextColor.GRAY)
                .append(Component.text(getMovementPercentage(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" speed").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_FOOT_ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 27;
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.FROST_WALKER);
    }
}
