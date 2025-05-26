package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class AraxysClaw extends CustomAttributeItem implements IBreakableEquipment {

    public AraxysClaw(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(Material.DIAMOND_SWORD)+30),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.25)
        );
    }

    @Override
    public int getMaxDurability() {
        return AraxysArmorPiece.DURABILITY;
    }

    @Override
    public int getPowerRating() {
        return AraxysArmorPiece.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.WEAPON;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }
}
