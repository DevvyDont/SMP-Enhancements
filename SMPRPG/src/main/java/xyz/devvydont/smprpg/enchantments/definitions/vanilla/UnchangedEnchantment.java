package xyz.devvydont.smprpg.enchantments.definitions.vanilla;


import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;

/**
 * Acts as a wrapper for vanilla enchantments with no extra behavior. Allows us to define descriptions for them
 */
public abstract class UnchangedEnchantment extends VanillaEnchantment {

    public UnchangedEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public void bootstrap(BootstrapContext context) {
        // Do nothing, we don't need to bootstrap an enchantment we are not changing
    }

    @Override
    public Component getDisplayName() {
        return getEnchantment().displayName(0);
    }

    @Override
    public RegistryKeySet<ItemType> getSupportedItems(RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event) {
        return null;  // unused
    }

    @Override
    public int getAnvilCost() {
        return 0;  // unused
    }

    @Override
    public int getMaxLevel() {
        return getEnchantment().getMaxLevel();
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return null; // unused
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return null; // unused
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ANY;
    }
}
