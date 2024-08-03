package xyz.devvydont.smprpg.items.blueprints.sets.bone;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class BoneArmorSet extends CustomArmorBlueprint implements ToolBreakable, Trimmable {

    public BoneArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 1)
        );
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.IRON;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SHAPER;
    }

    @Override
    public int getPowerRating() {
        return 0;
    }

    @Override
    public int getMaxDurability() {
        return 100;
    }
}
