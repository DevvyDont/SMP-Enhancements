package xyz.devvydont.smprpg.items.blueprints.sets.emberclad;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
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

public class EmbercladBoots extends EmbercladArmorSet {

    public EmbercladBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 85),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 60),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .18),
                new ScalarAttributeEntry(AttributeWrapper.BURNING_TIME, -.25)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(EmbercladArmorSet.INGREDIENT), generate()).build();
    }
}
