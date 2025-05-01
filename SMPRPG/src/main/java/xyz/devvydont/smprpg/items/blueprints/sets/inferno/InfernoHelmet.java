package xyz.devvydont.smprpg.items.blueprints.sets.inferno;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

import java.util.Collection;
import java.util.List;

public class InfernoHelmet extends InfernoArmorSet {

    public InfernoHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .3)
        );
    }

    @Override
    public int getDefense() {
        return 85;
    }

    @Override
    public int getHealth() {
        return 25;
    }

    @Override
    public double getStrength() {
        return .6;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SILENCE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, ItemService.getItem(CustomItemType.INFERNO_REMNANT), generate()).build();
    }
}
