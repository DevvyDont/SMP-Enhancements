package xyz.devvydont.smprpg.items.base;

import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.interfaces.Wearable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents some sort of item that can modify an entity's attributes either when worn or held.
 * Contains an extra "lore" section in the item description to display these stats to players since
 * we are overriding the given minecraft one (it's ugly)
 *
 * Also, all children of this class can be "prefixed", where a player can reforge an item for small additional
 * stat bonuses. Only one may be applied
 */
public abstract class CustomAttributeItem extends CustomItemBlueprint implements Attributeable {

    public CustomAttributeItem(ItemService itemService) {
        super(itemService);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        // Append the attribute data just before the description of the item.
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Power Rating: ").color(NamedTextColor.GRAY).append(Component.text(Symbols.POWER + getPowerRating()).color(NamedTextColor.YELLOW)));
        lore.add(Component.empty());

        // If this item is armor, we need defense line
        if (this instanceof Wearable)
            lore.add(Component.text("Defense: ").color(NamedTextColor.GRAY).append(Component.text("+" + ((Wearable) this).getDefense()).color(NamedTextColor.GREEN)));

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
    public int getDefense() {
        return 0;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.BASE;
    }


}
