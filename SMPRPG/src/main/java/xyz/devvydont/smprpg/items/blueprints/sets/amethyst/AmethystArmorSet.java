package xyz.devvydont.smprpg.items.blueprints.sets.amethyst;

import org.bukkit.inventory.meta.trim.TrimMaterial;
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

public abstract class AmethystArmorSet extends CustomArmorBlueprint implements Trimmable, ToolBreakable {

    public AmethystArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 1)
        );
    }

    @Override
    public int getPowerRating() {
        return 0;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

    @Override
    public int getMaxDurability() {
        return 100;
    }
}
