package xyz.devvydont.smprpg.items.blueprints.sets.valiant;

import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class ValiantHelmet extends ValiantArmorSet implements Trimmable {

    public ValiantHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 440),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 270),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .3)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.GOLD;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.FLOW;
    }
}
