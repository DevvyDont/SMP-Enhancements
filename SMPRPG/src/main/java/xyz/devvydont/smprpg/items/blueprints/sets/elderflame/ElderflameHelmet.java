package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
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
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ElderflameChestplate.DEFENSE/2.0 + 30),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, ElderflameChestplate.HEALTH/2.0 + 20),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 3),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, ElderflameChestplate.STRENGTH),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, ElderflameChestplate.CRIT),
                new AdditiveAttributeEntry(AttributeWrapper.REGENERATION, 50)
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
