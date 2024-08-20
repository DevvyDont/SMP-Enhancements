package xyz.devvydont.smprpg.items.blueprints.sets.slimy;

import org.bukkit.Color;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlimyBoots extends SlimyArmorSet implements Dyeable {

    public SlimyBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        List<AttributeEntry> attributes = new ArrayList<>(super.getAttributeModifiers());
        attributes.add(new ScalarAttributeEntry(AttributeWrapper.FALL_DAMAGE_MULTIPLIER, -.3));
        attributes.add(new AdditiveAttributeEntry(AttributeWrapper.SAFE_FALL, 7));
        return attributes;
    }

    @Override
    public int getDefense() {
        return 10;
    }

    @Override
    public int getHealth() {
        return 25;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_SLIME), generate()).build();
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.DIAMOND;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.WARD;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x80c71f);
    }

}
