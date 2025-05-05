package xyz.devvydont.smprpg.items.blueprints.sets.mystbloom;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;

import java.util.Collection;
import java.util.List;

public class MystbloomLeggings extends MystbloomArmorSet {

    public MystbloomLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapperLegacy.DEFENSE, ItemArmor.getDefenseFromMaterial(Material.DIAMOND_LEGGINGS)),
                new AdditiveAttributeEntry(AttributeWrapperLegacy.HEALTH, ItemArmor.getHealthFromMaterial(Material.DIAMOND_LEGGINGS)+5),
                new ScalarAttributeEntry(AttributeWrapperLegacy.STRENGTH, .1),
                new MultiplicativeAttributeEntry(AttributeWrapperLegacy.MOVEMENT_SPEED, .1)
        );
    }
}
