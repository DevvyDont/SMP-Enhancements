package xyz.devvydont.smprpg.enchantments.definitions.vanilla;


import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;

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
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return getEnchantment().getMaxLevel();
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ANY;
    }
}
