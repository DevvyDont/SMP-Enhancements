package xyz.devvydont.smprpg.items.base;


import org.bukkit.Color;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

/**
 * Since armor is a little more complex than just containing attributes (armor trims, leather armor color, etc.)
 * Make a class to streamline everything and leave children class solely responsible for stat setting
 */
public abstract class CustomArmorBlueprint extends CustomAttributeItem {

    public CustomArmorBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);

        // Apply a color! (if we want them and can actually apply them...)
        if (this instanceof Dyeable && meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
            Color color = ((Dyeable) this).getColor();
            armorMeta.setColor(color);
            armorMeta.addItemFlags(ItemFlag.HIDE_DYE);
        }

        // Apply armor trims! (if we want them and can actually apply them...)
        if (this instanceof Trimmable && meta instanceof ArmorMeta) {
            ArmorMeta armorMeta = (ArmorMeta) meta;
            TrimMaterial material = ((Trimmable) this).getTrimMaterial();
            TrimPattern pattern = ((Trimmable) this).getTrimPattern();
            armorMeta.setTrim(new ArmorTrim(material, pattern));
            armorMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        }

    }
}
