package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

import java.util.Collection;
import java.util.List;

public class AraxysHelmet extends CustomFakeHelmetBlueprint implements ToolBreakable {

    public AraxysHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 100),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 60),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .25),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .1)
        );
    }

    @Override
    public int getPowerRating() {
        return AraxysArmorPiece.POWER;
    }

    @Override
    public int getMaxDurability() {
        return AraxysArmorPiece.DURABILITY;
    }
}
