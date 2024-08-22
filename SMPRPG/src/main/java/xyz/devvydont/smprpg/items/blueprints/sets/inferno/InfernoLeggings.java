package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

import java.util.Collection;
import java.util.List;

public class InfernoLeggings extends InfernoArmorSet {

    public InfernoLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .15)
        );
    }

    @Override
    public int getDefense() {
        return 230;
    }

    @Override
    public int getHealth() {
        return 200;
    }

    @Override
    public double getStrength() {
        return .4;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.WAYFINDER;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, ItemService.getItem(CustomItemType.INFERNO_REMNANT), generate()).build();
    }
}
