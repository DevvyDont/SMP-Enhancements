package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class PreludeBoots extends PreludeArmorSet {

    public PreludeBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 640),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 330),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 4),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .6),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .3),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .15)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }
}
