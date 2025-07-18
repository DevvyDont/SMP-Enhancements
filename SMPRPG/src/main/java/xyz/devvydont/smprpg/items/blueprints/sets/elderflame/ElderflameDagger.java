package xyz.devvydont.smprpg.items.blueprints.sets.elderflame;

import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ElderflameDagger extends CustomAttributeItem {

    public ElderflameDagger(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    /**
     * Determine what type of item this is.
     */
    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    /**
     * What modifiers themselves will be contained on the item if there are no variables to affect them?
     *
     * @param item The item that is supposed to be holding the modifiers.
     * @return
     */
    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 200),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 50),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        );
    }

    /**
     * How much should we increase the power rating of an item if this container is present?
     *
     * @return
     */
    @Override
    public int getPowerRating() {
        return 50;
    }

    /**
     * The slot that this item has to be worn in for attributes to kick in.
     *
     * @return
     */
    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }
}
