package xyz.devvydont.smprpg.enchantments.definitions.vanilla;


import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
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
//        System.out.println("Attempting to bootstrap " + getClass().getName() + " enchantment");

        if (isBootstrapped())
            throw new IllegalStateException("Enchantment " + getClass().getName() + " is already bootstrapped!");

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.entryAdd()
                .newHandler(event -> event.builder()
                        .description(getDisplayName()))
                // Configure the handled to only be called for the Vanilla sharpness enchantment.
                .filter(getTypedKey())
        );

        bootstrapCompleted();
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
