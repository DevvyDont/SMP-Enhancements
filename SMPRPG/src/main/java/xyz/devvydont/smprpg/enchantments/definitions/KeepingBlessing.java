package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class KeepingBlessing extends CustomEnchantment implements Listener {

    public KeepingBlessing(String id) {
        super(id);
    }

    @Override
    public Component getDisplayName() {
        return ComponentUtil.getColoredComponent("Blessing of Keeping", NamedTextColor.YELLOW);
    }

    @Override
    public TextColor getEnchantColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    public Component getDescription() {
        return Component.text("This item ").color(NamedTextColor.GRAY)
                .append(Component.text("will not").color(NamedTextColor.AQUA))
                .append(Component.text(" drop from ").color(NamedTextColor.GRAY))
                .append(Component.text("death").color(NamedTextColor.DARK_RED));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_VANISHING;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ANY;
    }

    @Override
    public int getSkillRequirement() {
        return 10;
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.VANISHING_CURSE);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        // Loop through every item in the drops. If it is enchanted with our blessing, remove it from the drops
        // and set it as an item to keep.
        for (ItemStack drop : event.getDrops().stream().toList()) {

            // Does this item have blessing?
            if (drop.getEnchantmentLevel(getEnchantment()) <= 0)
                continue;

            // Remove from the drops and set as a keep item
            event.getDrops().remove(drop);
            event.getItemsToKeep().add(drop);
        }

    }
}
