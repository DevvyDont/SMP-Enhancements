package xyz.devvydont.smprpg.items.base;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;


public abstract class VanillaAttributeItem extends VanillaItemBlueprint implements IAttributeItem, Sellable {

    public VanillaAttributeItem(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public int getWorth() {
        return AttributeUtil.calculateValue(getPowerRating(), getDefaultRarity(), true);
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.BASE;
    }

    @Override
    public AttributeModifierType.AttributeSession getAttributeSession(AttributeModifierType type, ItemMeta meta) {
        return type.session(getItem().getType(), meta);
    }

}
