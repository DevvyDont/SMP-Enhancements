package xyz.devvydont.smprpg.enchantments;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.SMPRPG;

public abstract class CustomEnchantment implements Cloneable {

    public static final int UNAPPLIED = -1;
    private boolean bootstrapped = false;

    public boolean isBootstrapped() {
        return bootstrapped;
    }

    private final String id;
    private TypedKey<Enchantment> typedKey;
    private Key key = null;

    private int level = UNAPPLIED;

    public CustomEnchantment(String id) {
        this.id = id;
    }

    public CustomEnchantment build(int level) {

        CustomEnchantment copy;

        try {
             copy = (CustomEnchantment) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Enchantment " + id + " cannot be cloned", e);
        }

        copy.setLevel(level);
        return copy;
    }

    public void bootstrapCompleted() {
//        System.out.println("Successfully bootstrapped " + getClass().getName() + " enchantment");
        bootstrapped = true;
    }

    public void bootstrap(BootstrapContext context) {

//        System.out.println("Attempting to bootstrap " + getClass().getName() + " enchantment");

        if (isBootstrapped())
            throw new IllegalStateException("Enchantment " + getClass().getName() + " is already bootstrapped!");

        setTypedKey(TypedKey.create(RegistryKey.ENCHANTMENT, getKey()));
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            event.registry().register(
                    getTypedKey(),
                    b -> b.description(getDisplayName())
                            .primaryItems(getSupportedItems(event))
                            .supportedItems(getSupportedItems(event))
                            .anvilCost(getAnvilCost())
                            .maxLevel(getMaxLevel())
                            .weight(getWeight())
                            .minimumCost(getMinimumCost())
                            .maximumCost(getMaximumCost())
                            .activeSlots(getEquipmentSlotGroup())
            );
        }));

        bootstrapCompleted();
    }

    public String getId() {
        return id;
    }

    public Key getKey() {

        if (key == null)
            key = Key.key("smprpg", id);

        return key;
    }

    public TypedKey<Enchantment> getTypedKey() {
        return typedKey;
    }

    public void setTypedKey(TypedKey<Enchantment> typedKey) {
        this.typedKey = typedKey;
    }

    public abstract Component getDisplayName();

    public TextColor getEnchantColor() {
        return NamedTextColor.BLUE;
    }

    public abstract Component getDescription();

    public abstract RegistryKeySet<ItemType> getSupportedItems(RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder> event);

    public abstract int getAnvilCost();

    public void setKey(Key key) {
        this.key = key;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public abstract int getMaxLevel();

    public abstract int getWeight();

    public abstract EnchantmentRegistryEntry.EnchantmentCost getMinimumCost();

    public abstract EnchantmentRegistryEntry.EnchantmentCost getMaximumCost();

    public abstract EquipmentSlotGroup getEquipmentSlotGroup();

    public Enchantment getEnchantment() {
        return SMPRPG.getInstance().getEnchantmentService().getEnchantment(getTypedKey());
    }
}
