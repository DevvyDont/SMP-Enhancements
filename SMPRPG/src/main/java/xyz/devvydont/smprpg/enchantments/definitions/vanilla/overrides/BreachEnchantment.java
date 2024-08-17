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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;

public class BreachEnchantment extends VanillaEnchantment implements Listener {

    public BreachEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getDefensePiercing(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 100;
            case 2 -> 250;
            case 3 -> 600;
            case 4 -> 1000;
            case 5 -> 2000;
            default -> getDefensePiercing(level) + (level-5)*1000;
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Breach");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Attacks pierce through ").color(NamedTextColor.GRAY)
                .append(Component.text(getDefensePiercing(getLevel())).color(NamedTextColor.GREEN))
                .append(Component.text(" defense").color(NamedTextColor.GRAY))
                .append(Component.text(" **NOT IMPLEMENTED").color(NamedTextColor.DARK_RED));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_MACE;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 60;
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.DENSITY);
    }

    @EventHandler
    public void onDealMaceDamage(CustomEntityDamageByEntityEvent event) {
        // todo figure out how to decrease defense on attacks
    }
}
