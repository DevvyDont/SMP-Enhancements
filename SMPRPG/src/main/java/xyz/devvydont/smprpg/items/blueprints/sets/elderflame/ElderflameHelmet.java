package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

import java.util.Collection;
import java.util.List;

public class ElderflameHelmet extends ElderflameArmorSet {

    public ElderflameHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 270),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 225),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 3),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .35),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .1),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .1),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .2)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(CustomItemType.DRACONIC_CRYSTAL), generate()).build();
    }
}
