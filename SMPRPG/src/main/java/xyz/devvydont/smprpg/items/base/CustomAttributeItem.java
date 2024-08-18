package xyz.devvydont.smprpg.items.base;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Attr;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents some sort of item that can modify an entity's attributes either when worn or held.
 * Contains an extra "lore" section in the item description to display these stats to players since
 * we are overriding the given minecraft one (it's ugly)
 *
 * Also, all children of this class can be "prefixed", where a player can reforge an item for small additional
 * stat bonuses. Only one may be applied
 */
public abstract class CustomAttributeItem extends CustomItemBlueprint implements Attributeable, Sellable {


    public CustomAttributeItem(ItemService itemService, CustomItemType type) {
        super(itemService, type);
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
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        // Append the attribute data just before the description of the item.
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Power Rating: ").color(NamedTextColor.GRAY).append(Component.text(Symbols.POWER + getTotalPower(meta)).color(NamedTextColor.YELLOW)));
        lore.add(Component.empty());

        lore.addAll(AttributeUtil.getAttributeLore(this, meta));
        lore.addAll(super.getDescriptionComponent(meta));
        return lore;
    }

    @Override
    public void applyModifiers(ItemMeta meta) {
        AttributeUtil.applyModifiers(this, meta);
    }

    @Override
    public AttributeModifierType.AttributeSession getAttributeSession(AttributeModifierType type, ItemMeta meta) {
        return type.session(this, meta);
    }

    @Override
    public void updateMeta(ItemMeta meta) {

        // Before we can update the meta of this item, we need to fix its "vanilla" behavior
        applyModifiers(meta);
        super.updateMeta(meta);
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
