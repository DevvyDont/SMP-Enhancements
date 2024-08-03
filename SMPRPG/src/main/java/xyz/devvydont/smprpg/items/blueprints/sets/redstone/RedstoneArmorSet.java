package xyz.devvydont.smprpg.items.blueprints.sets.redstone;

import org.bukkit.Color;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class RedstoneArmorSet extends CustomArmorBlueprint implements ToolBreakable, Dyeable, Trimmable {

    public static final int POWER = 0;
    public static final int DURABILITY = 100;


    public RedstoneArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 1)
        );
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xB02E26);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.COPPER;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.TIDE;
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
