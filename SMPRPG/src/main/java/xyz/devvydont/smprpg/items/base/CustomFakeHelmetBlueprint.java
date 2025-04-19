package xyz.devvydont.smprpg.items.base;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

/**
 * Used to detect when we are trying to equip a "fake" helmet. Fake helmets are simply normal blocks that a
 * player is able to wear on their head
 */
public abstract class CustomFakeHelmetBlueprint extends CustomAttributeItem implements Listener {

    public static final EquippableComponent EQUIPPABLE_COMPONENT = new ItemStack(Material.IRON_HELMET).getItemMeta().getEquippable();

    public CustomFakeHelmetBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setMaxStackSize(1);  // These cannot stack
        meta.setEquippable(EQUIPPABLE_COMPONENT);  // Set this item to behave like an iron helmet.
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @EventHandler
    public void onAttemptPlaceHelmet(BlockPlaceEvent event) {
        if (!isItemOfType(event.getItemInHand()))
            return;

        event.setCancelled(true);
    }











}
