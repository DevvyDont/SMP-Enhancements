package me.devvy.smpparkour.items;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.items.blueprints.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ItemManager implements Listener {

    public static final NamespacedKey CUSTOM_ITEM_KEY = new NamespacedKey(SMPParkour.getInstance(), "CUSTOM_ITEM_TYPE");

    private final Map<String, ParkourUtilityItemBlueprint> keyToCustomItem = new HashMap<>();
    private final Map<Class<? extends ParkourUtilityItemBlueprint>, ParkourUtilityItemBlueprint> classToCustomItem = new HashMap<>();

    public ItemManager() {
        // Register for events
        SMPParkour.getInstance().getServer().getPluginManager().registerEvents(this, SMPParkour.getInstance());

        // Register the custom items
        registerCustomItem(new StartParkourBlueprint());
        registerCustomItem(new PracticeModeBlueprint());

        registerCustomItem(new EndParkourBlueprint());
        registerCustomItem(new ResetParkourBlueprint());
        registerCustomItem(new LastCheckpointParkourBlueprint());

        registerCustomItem(new SetPracticeCheckpointBlueprint());
        registerCustomItem(new TeleportPracticeCheckpointBlueprint());
        registerCustomItem(new EnableFlightBlueprint());
    }

    public void registerCustomItem(ParkourUtilityItemBlueprint blueprint) {
        ParkourUtilityItemBlueprint old = keyToCustomItem.put(blueprint.key(), blueprint);

        if (old != null)
            throw new IllegalArgumentException("Already registered item with key " + blueprint.key());

        old = classToCustomItem.put(blueprint.getClass(), blueprint);

        if (old != null)
            throw new IllegalArgumentException("Already registered item with key " + blueprint.key());
    }

    public ParkourUtilityItemBlueprint getBlueprint(Class<? extends ParkourUtilityItemBlueprint> clazz) {

        if (!classToCustomItem.containsKey(clazz))
            throw new NullPointerException("No custom item with class: " + clazz.getName() + ". Did you forget to register it?");

        return classToCustomItem.get(clazz);
    }

    public ParkourUtilityItemBlueprint getBlueprint(ItemStack item) {

        // No item or item meta means it cannot be a custom item
        if (item == null || !item.hasItemMeta())
            return null;

        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        String itemKey = data.getOrDefault(CUSTOM_ITEM_KEY, PersistentDataType.STRING, "");
        ParkourUtilityItemBlueprint foundBlueprint = keyToCustomItem.get(itemKey);

        if (foundBlueprint != null && !foundBlueprint.matches(item))
            foundBlueprint = null;

        return foundBlueprint;
    }

    @EventHandler
    public void onInteractWithCustomItem(PlayerInteractEvent e) {

        // Only listen to events where we have a custom item
        ParkourUtilityItemBlueprint usedItem = getBlueprint(e.getItem());
        if (usedItem == null)
            return;

        // If we are ever involved with custom items, cancel the event
        e.setCancelled(true);

        // If we are on cooldown do not do anything
        if (e.getPlayer().hasCooldown(usedItem.getVanillaType()))
            return;

        // Handle what we should do
        if (e.getAction().isRightClick())
            usedItem.onRightClick(e.getPlayer());

        else if (e.getAction().isLeftClick())
            usedItem.onLeftClick(e.getPlayer());

    }

    @EventHandler
    public void onClickCustomItem(InventoryClickEvent e) {

        // Only listen to events where we have a custom item
        ParkourUtilityItemBlueprint usedItem = getBlueprint(e.getCurrentItem());
        if (usedItem == null)
            return;

        // Depending on the action, do something, leave the event alone if we have no matches
        switch (e.getAction()) {
            case PICKUP_ALL:
            case PICKUP_ONE:
            case PICKUP_HALF:
            case PICKUP_SOME:
                usedItem.onInventoryClick((Player) e.getWhoClicked());
                e.setCancelled(true);
                break;
        }

    }

    public void cleanup() {
    }
}
