package xyz.devvydont.smprpg.items.blueprints.sets.forsaken;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.blueprints.sets.reaver.ReaverArmorSet;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class ForsakenArmorSet extends ReaverArmorSet implements ICraftable {

    public static final int POWER = 30;
    public static final CustomItemType CRAFTING_COMPONENT = CustomItemType.PREMIUM_NETHER_STAR;

    public ForsakenArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getWitherResistance() {
        return 50;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()),
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, .25)
        );
    }

    public abstract int getDefense();
    public abstract int getHealth();
    public abstract double getStrength();

    @Override
    public int getPowerRating() {
        return POWER;
    }

    @Override
    public int getMaxDurability() {
        return 40_000;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.generate(Material.NETHER_STAR)
        );
    }
}
