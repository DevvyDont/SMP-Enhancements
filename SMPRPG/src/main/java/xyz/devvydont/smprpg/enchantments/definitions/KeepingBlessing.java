package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
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
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class KeepingBlessing extends CustomEnchantment implements Listener {

    public KeepingBlessing(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blessing of Keeping", NamedTextColor.YELLOW);
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("This item is ").color(NamedTextColor.GRAY)
                .append(Component.text("soulbound").color(NamedTextColor.DARK_PURPLE))
                .append(Component.text(" and will not drop from ").color(NamedTextColor.GRAY))
                .append(Component.text("death").color(NamedTextColor.RED));
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
        return EnchantmentRarity.BLESSING.getWeight();
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
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.VANISHING_CURSE, EnchantmentService.TELEKINESIS_BLESSING.getTypedKey(), EnchantmentService.MERCY_BLESSING.getTypedKey());
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
