package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;

import java.util.Collection;
import java.util.List;

public class PreludeBoots extends PreludeArmorSet {

    public PreludeBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapperLegacy.DEFENSE, 640),
                new AdditiveAttributeEntry(AttributeWrapperLegacy.HEALTH, 330),
                new AdditiveAttributeEntry(AttributeWrapperLegacy.ARMOR, 4),
                new ScalarAttributeEntry(AttributeWrapperLegacy.STRENGTH, .6),
                new ScalarAttributeEntry(AttributeWrapperLegacy.MOVEMENT_SPEED, .3),
                new MultiplicativeAttributeEntry(AttributeWrapperLegacy.ATTACK_SPEED, .15)
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
