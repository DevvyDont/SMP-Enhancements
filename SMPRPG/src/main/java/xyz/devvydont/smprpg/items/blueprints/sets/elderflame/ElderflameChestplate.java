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
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

import java.util.Collection;
import java.util.List;

public class ElderflameChestplate extends ElderflameArmorSet {

    public static final int DEFENSE = 300;
    public static final int HEALTH = 200;
    public static final double STRENGTH = .75;
    public static final int CRIT = 25;

    public ElderflameChestplate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, DEFENSE),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, HEALTH),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 4),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, STRENGTH),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .25),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, CRIT)
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
