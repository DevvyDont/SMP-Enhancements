package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import org.bukkit.Color;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

import java.util.Collection;
import java.util.List;

public class ElderflameBoots extends ElderflameArmorSet implements IDyeable {

    public ElderflameBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ElderflameChestplate.DEFENSE / 2.0),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, ElderflameChestplate.HEALTH / 2.0),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 2),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, ElderflameChestplate.STRENGTH - .25),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .3),
                new AdditiveAttributeEntry(AttributeWrapper.SAFE_FALL, 10),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, ElderflameChestplate.CRIT)
        );
    }


    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x1d1d21);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(CustomItemType.DRACONIC_CRYSTAL), generate()).build();
    }
}
