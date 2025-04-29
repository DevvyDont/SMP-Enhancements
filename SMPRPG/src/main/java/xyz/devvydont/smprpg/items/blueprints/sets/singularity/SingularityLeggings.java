package xyz.devvydont.smprpg.items.blueprints.sets.singularity;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class SingularityLeggings extends CustomArmorBlueprint implements ToolBreakable, Trimmable {

    public SingularityLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public int getPowerRating() {
        return 100;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 6),
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 930),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 460),
                new ScalarAttributeEntry(Attribute.KNOCKBACK_RESISTANCE, .2),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .2),
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, .2)
        );
    }

    @Override
    public int getMaxDurability() {
        return 100_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }
}
