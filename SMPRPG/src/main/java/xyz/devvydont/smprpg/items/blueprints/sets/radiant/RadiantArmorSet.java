package xyz.devvydont.smprpg.items.blueprints.sets.radiant;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
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

public abstract class RadiantArmorSet extends CustomArmorBlueprint implements IBreakableEquipment, Trimmable, ICraftable {

    public RadiantArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public abstract int getHealth();

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 10),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .6)
        );
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.GOLD;
    }

    @Override
    public int getPowerRating() {
        return 28;
    }

    @Override
    public int getMaxDurability() {
        return 27_500;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey());
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(Material.GLOWSTONE));
    }
}
