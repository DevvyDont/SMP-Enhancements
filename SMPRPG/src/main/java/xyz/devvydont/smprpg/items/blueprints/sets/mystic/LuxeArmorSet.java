package xyz.devvydont.smprpg.items.blueprints.sets.mystic;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class LuxeArmorSet extends CustomArmorBlueprint implements ICraftable, Trimmable, IBreakableEquipment {

    public static CustomItemType ingredient = CustomItemType.ENCHANTED_LAPIS;

    public LuxeArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 5),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .4)
        );
    }

    public abstract int getHealth();

    @Override
    public int getPowerRating() {
        return 20;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(Material.LAPIS_LAZULI));
    }

    @Override
    public int getMaxDurability() {
        return 12_500;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.LAPIS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SPIRE;
    }
}
