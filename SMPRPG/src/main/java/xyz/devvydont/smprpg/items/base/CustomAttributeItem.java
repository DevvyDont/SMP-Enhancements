package xyz.devvydont.smprpg.items.base;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;

/**
 * Represents some sort of item that can modify an entity's attributes either when worn or held.
 * Contains an extra "lore" section in the item description to display these stats to players since
 * we are overriding the given minecraft one (it's ugly)
 *
 * Also, all children of this class can be "prefixed", where a player can reforge an item for small additional
 * stat bonuses. Only one may be applied
 */
public abstract class CustomAttributeItem extends CustomItemBlueprint implements IAttributeItem, ISellable {


    public CustomAttributeItem(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public boolean wantNerfedSellPrice() {
        return getDefaultRarity().ordinal() < ItemRarity.EPIC.ordinal();
    }

    @Override
    public int getWorth(ItemStack item) {
        return AttributeUtil.calculateValue(getPowerRating(), getDefaultRarity(), this instanceof ICraftable && wantNerfedSellPrice()) * item.getAmount();
    }

    @Override
    public AttributeModifierType.AttributeSession getAttributeSession(AttributeModifierType type, ItemMeta meta) {
        return type.session(this, meta);
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.BASE;
    }

    /**
     * Sums the power rating of the item with any additional bonuses on it
     *
     * @param meta
     * @return
     */
    public int getTotalPower(ItemMeta meta) {
        return getPowerRating() + AttributeUtil.getPowerBonus(meta);
    }


}
