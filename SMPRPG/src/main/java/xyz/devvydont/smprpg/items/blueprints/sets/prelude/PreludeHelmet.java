package xyz.devvydont.smprpg.items.blueprints.sets.prelude;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class PreludeHelmet extends PreludeArmorSet {

    public PreludeHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 700),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 365),
                new AdditiveAttributeEntry(AttributeWrapper.ARMOR, 4),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .65),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .2),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, .2),
                new ScalarAttributeEntry(AttributeWrapper.LUCK, .45)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.REDSTONE;
    }
}
