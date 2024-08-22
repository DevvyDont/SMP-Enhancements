package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

import java.util.Collection;
import java.util.List;

public class ElderflameChestplate extends ElderflameArmorSet {

    public ElderflameChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 300),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 250),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 4),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .5),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .1),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .2)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.DRACONIC_CRYSTAL), generate()).build();
    }
}
