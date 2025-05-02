package xyz.devvydont.smprpg.items.blueprints.sets.sakura;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public abstract class SakuraArmorSet extends CustomArmorBlueprint implements IBreakableEquipment, ICraftable {

    public static final int POWER = 7;
    public static final int DURABILITY = 1_200;

    public SakuraArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.CHERRY_LOG)
        );
    }

    @Override
    public int getPowerRating() {
        return POWER;
    }

    @Override
    public int getMaxDurability() {
        return DURABILITY;
    }
}
