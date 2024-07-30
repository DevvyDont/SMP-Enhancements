package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

import java.util.Collection;
import java.util.List;

public class AraxysChestplate extends AraxysArmorPiece {

    public AraxysChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 210),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 35),
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 10)
        );
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_STRING), generate()).build();
    }
}