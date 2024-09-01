package xyz.devvydont.smprpg.enchantments.definitions.vanilla;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;

public abstract class VanillaEnchantment extends CustomEnchantment {

    public VanillaEnchantment(TypedKey<Enchantment> key) {
        super(key.key().value());
        setTypedKey(key);
    }

    @Override
    public void bootstrap(BootstrapContext context) {

//        System.out.println("Attempting to bootstrap " + getClass().getName() + " enchantment");

        if (isBootstrapped())
            throw new IllegalStateException("Enchantment " + getClass().getName() + " is already bootstrapped!");

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.entryAdd()
                // Increase the max level to 10
                .newHandler(event -> event.builder()
                        .description(getDisplayName())
                        .anvilCost(getAnvilCost())
                        .maxLevel(getMaxLevel())
                        .weight(getWeight())
                        .exclusiveWith(getConflictingEnchantments())
                        .minimumCost(getMinimumCost())
                        .maximumCost(getMaximumCost()))
                // Configure the handled to only be called for the Vanilla sharpness enchantment.
                .filter(getTypedKey())
        );

        bootstrapCompleted();
    }

    public Enchantment getVanillaEnchantment() {
        return SMPRPG.getInstance().getEnchantmentService().getEnchantment(getTypedKey());
    }

    @Override
    public Key getKey() {
        return getTypedKey().key();
    }


}
