package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.items.interfaces.ITrimmable;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class AraxysArmorPiece extends CustomAttributeItem implements IBreakableEquipment, IDyeable, ITrimmable {

    public static final int POWER = 25;
    public static final int DURABILITY = 27_500;

    public AraxysArmorPiece(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return POWER;
    }


    @Override
    public Color getColor() {
        return Color.fromRGB(0x474f52);
    }

    @Override
    public int getMaxDurability() {
        return DURABILITY;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.NETHERITE;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.DUNE;
    }
}
