package xyz.devvydont.smprpg.items.reforges;

import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.*;
import xyz.devvydont.smprpg.items.interfaces.Attributeable;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.*;

public abstract class ReforgeBase {

    public enum ReforgeResult {
        SUCCESS,            // Reforge was successfully applied.
        NO_META,            // Probably really rare, item.getMeta() was null.
        UNREGISTERED,       // This reforge is not registered in the server. Cannot apply
        UNFORGEABLE,        // Attempted to reforge an item that does not support reforges.
        INVALID_EQUIPMENT,  // Attempted to apply a reforge on an item where the item does not support the reforge.
        ;
    }

    // Used to associate an attribute modifier with reforge to prevent attribute stacking from switching reforges
    public static final String REFORGE_ATTRIBUTE_NAME = "reforge_attribute";

    private final ItemService itemService;

    public ReforgeBase(ItemService itemService) {
        this.itemService = itemService;
    }

    public NamespacedKey getAttributeNamespacedKey() {
        return new NamespacedKey(SMPRPG.getInstance(), REFORGE_ATTRIBUTE_NAME);
    }

    /**
     * An item lore friendly list of components to display as a vague description of the item for what it does
     *
     * @return
     */
    public abstract List<Component> getDescription();

    /**
     * Retrieve the enum associated with this reforge, used as identifier and a way to make sure all reforges have
     * implementations
     *
     * @return
     */
    public abstract ReforgeType getReforgeType();

    public AttributeModifier generateAdditiveModifier(double amount, EquipmentSlotGroup slot) {
        return new AttributeModifier(getAttributeNamespacedKey(), amount, AttributeModifier.Operation.ADD_NUMBER, slot);
    }

    public AttributeModifier generateMultiplicativeModifier(double amount, EquipmentSlotGroup slot) {
        return new AttributeModifier(getAttributeNamespacedKey(), amount, AttributeModifier.Operation.ADD_SCALAR, slot);
    }

    /**
     * Given an item stack, returns what result would occur if this reforge were to be applied.
     * Can be used as a check for an item to prevent reforges being applied to undesirable items.
     *
     * @param itemStack
     * @return
     */
    public ReforgeResult canBeReforged(ItemStack itemStack) {

        SMPItemBlueprint blueprint = itemService.getBlueprint(itemStack);

        // Is this item real?
        if (itemStack.getItemMeta() == null)
            return ReforgeResult.NO_META;

        // Is the reforge registered? If not, the plugin was not setup correctly
        if (!itemService.getRegisteredReforgeTypes().contains(getReforgeType()))
            return ReforgeResult.UNREGISTERED;

        // Does this blueprint support attribute modification?
        if (!(blueprint instanceof Attributeable))
            return ReforgeResult.UNFORGEABLE;

        // Can this item class be reforged?
        if (!blueprint.getItemClassification().reforgeable())
            return ReforgeResult.UNFORGEABLE;

        // If this item type will end up not having any reforge buffs applied to the item, it is an invalid combination.
        if (itemService.getModifiersForReforge(getReforgeType(), blueprint.getRarity(itemStack), blueprint.getItemClassification()).isEmpty())
            return ReforgeResult.INVALID_EQUIPMENT;

        return ReforgeResult.SUCCESS;
    }

    /**
     * Given an item's meta, the rarity to reforge for, and the type of equipment,
     * apply this reforge's attribute pool to it.
     *
     * This method is considered "dumb" and will simply just apply attributes no matter what arguments are given.
     *
     * @param meta
     * @param rarity
     * @return
     */
    public ItemMeta applyReforgeAttributes(ItemMeta meta, ItemRarity rarity, ItemClassification itemClassification) {

        // Tag the item so we know what reforge is on this item
        meta.getPersistentDataContainer().set(itemService.REFORGE_TYPE_KEY, PersistentDataType.STRING, getReforgeType().key());

        // Apply all reforge attributes for this specific item class
        for (Map.Entry<Attribute, AttributeModifier> entry : itemService.getModifiersForReforge(getReforgeType(), rarity, itemClassification).entrySet())
            meta.addAttributeModifier(entry.getKey(), entry.getValue());

        return meta;
    }

    /**
     * Given an ItemStack, attempt to apply this reforge to its internal data.
     *
     * @param item
     * @return
     */
    public ReforgeResult applyReforge(ItemStack item) {

        ReforgeResult reforgeStatus = canBeReforged(item);

        // If this reforge wouldn't be successful, don't even bother
        if (!reforgeStatus.equals(ReforgeResult.SUCCESS))
            return reforgeStatus;

        SMPItemQuery itemInformation = itemService.getItemInformation(item);

        // Safe to apply the reforge
        ItemMeta reforgedItemMeta = applyReforgeAttributes(item.getItemMeta(), itemInformation.blueprint().getRarity(item), itemInformation.blueprint().getItemClassification());
        item.setItemMeta(reforgedItemMeta);

        return ReforgeResult.SUCCESS;
    }

}
