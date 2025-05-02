package xyz.devvydont.smprpg.items.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;


public abstract class VanillaAttributeItem extends VanillaItemBlueprint implements IAttributeItem, ISellable {

    public VanillaAttributeItem(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public int getWorth(ItemStack item) {
        return AttributeUtil.calculateValue(getPowerRating(), getDefaultRarity(), true);
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.BASE;
    }

    @Override
    public AttributeModifierType.AttributeSession getAttributeSession(AttributeModifierType type, ItemMeta meta) {
        return type.session(material, meta);
    }

}
