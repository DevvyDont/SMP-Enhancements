package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

import java.util.Collection;
import java.util.List;

public class AraxysLeggings extends AraxysArmorPiece {

    public AraxysLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 165),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 35),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .2)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }
}
