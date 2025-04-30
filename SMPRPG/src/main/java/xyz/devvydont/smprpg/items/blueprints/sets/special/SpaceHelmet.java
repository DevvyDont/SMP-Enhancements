package xyz.devvydont.smprpg.items.blueprints.sets.special;

import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class SpaceHelmet extends CustomFakeHelmetBlueprint {


    public SpaceHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return 1;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(Attribute.SAFE_FALL_DISTANCE, 50),
                new MultiplicativeAttributeEntry(Attribute.FALL_DAMAGE_MULTIPLIER, -.50),
                new MultiplicativeAttributeEntry(Attribute.GRAVITY, -.9),
                new MultiplicativeAttributeEntry(Attribute.JUMP_STRENGTH, 2)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }
}
