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
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

import java.util.Collection;
import java.util.List;

public class InfernoBoots extends InfernoArmorSet {

    public InfernoBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .25)
        );
    }

    @Override
    public int getDefense() {
        return 80;
    }

    @Override
    public int getHealth() {
        return 20;
    }

    @Override
    public double getStrength() {
        return .5;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.WAYFINDER;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, ItemService.generate(CustomItemType.INFERNO_REMNANT), generate()).build();
    }
}
