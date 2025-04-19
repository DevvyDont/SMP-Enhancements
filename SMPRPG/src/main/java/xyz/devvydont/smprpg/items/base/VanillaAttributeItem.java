package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;


public abstract class VanillaAttributeItem extends VanillaItemBlueprint implements Attributeable, Sellable {

    public VanillaAttributeItem(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    @Override
    public int getWorth() {
        return AttributeUtil.calculateValue(getPowerRating(), getDefaultRarity(), this instanceof Craftable);
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
