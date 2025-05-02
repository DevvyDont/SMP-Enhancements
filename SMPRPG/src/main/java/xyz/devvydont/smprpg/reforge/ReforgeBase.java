package xyz.devvydont.smprpg.reforge;

import net.kyori.adventure.text.Component;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.IAttributeContainer;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public abstract class ReforgeBase implements Keyed, IAttributeContainer {

    public enum ReforgeResult {
        SUCCESS,            // Reforge was successfully applied.
        NO_META,            // Probably really rare, item.getMeta() was null.
        UNREGISTERED,       // This reforge is not registered in the server. Cannot apply
        UNFORGEABLE,        // Attempted to reforge an item that does not support reforges.
        INVALID_EQUIPMENT,  // Attempted to apply a reforge on an item where the item does not support the reforge.
        ;
    }

    private final ReforgeType type;

    public ReforgeBase(ReforgeType type) {
        this.type = type;
    }

    /**
     * The enum reforge that is assigned to this handler. Assigned during construction via reflection in the
     * enum class itself.
     *
     * @return
     */
    public ReforgeType getType() {
        return type;
    }

    protected ItemService getItemService() {
        return SMPRPG.getInstance().getItemService();
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.REFORGE;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getType().key());
    }

    /**
     * Returns attribute modifiers using default rarity. Might be worth looking into figuring out a better way
     * around this because we have to consider item rarity
     *
     * @return
     */
    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return getAttributeModifiersWithRarity(ItemRarity.COMMON);
    }

    public abstract Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity);

    /**
     * Sets the persistent key on this item to this reforge
     *
     * @param itemStack
     */
    public void applyItemPersistence(ItemStack itemStack) {
        itemStack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(getItemService().REFORGE_TYPE_KEY, PersistentDataType.STRING, getKey().value());
        });
    }

    /**
     * Removes the key from this item that says this item is of a certain reforge
     *
     * @param itemStack
     */
    public void removeItemPersistence(ItemStack itemStack) {
        itemStack.editMeta(meta -> {
            meta.getPersistentDataContainer().remove(getItemService().REFORGE_TYPE_KEY);
        });
    }

    /**
     * An item lore friendly list of components to display as a vague description of the item for what it does
     *
     * @return
     */
    public abstract List<Component> getDescription();

    /**
     * Attempts to apply this reforge to the item
     *
     * @param item
     * @return
     */
    public ReforgeResult apply(ItemStack item) {

        // Attempt to remove this reforge no matter what
        remove(item);

        // Apply this reforges tag
        applyItemPersistence(item);

        // Update the item (Attributes will be updated from doing this)
        SMPItemBlueprint blueprint = getItemService().getBlueprint(item);
        blueprint.updateItemData(item);

        return ReforgeResult.SUCCESS;
    }

    /**
     * Removes this reforge from an item
     *
     * @param item
     */
    public void remove(ItemStack item) {

        if (item == null || item.getItemMeta() == null)
            return;

        SMPItemBlueprint blueprint = getItemService().getBlueprint(item);
        ItemMeta meta = item.getItemMeta();

        // Remove this item's attributes that make it reforged under this handler
        if (blueprint instanceof IAttributeItem attributeable)
            attributeable.getAttributeSession(AttributeModifierType.REFORGE, meta).removeAttributeModifiers();
        removeItemPersistence(item);

        // Now that it is removed, update the item
        blueprint.updateItemData(item);
    }

    /**
     * Checks if an item has this reforge currently equipped
     *
     * @param item an ItemStack to check a reforge against
     * @return true if the item has this reforge, false otherwise
     */
    public boolean hasReforge(ItemStack item) {
        ReforgeBase applied = SMPRPG.getInstance().getItemService().getReforge(item);
        if (applied == null)
            return false;

        return applied.getType().equals(getType());
    }

}
