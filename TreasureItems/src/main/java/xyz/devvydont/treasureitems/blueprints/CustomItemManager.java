package xyz.devvydont.treasureitems.blueprints;


import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerFishEvent;
import xyz.devvydont.treasureitems.blueprints.armor.*;
import xyz.devvydont.treasureitems.blueprints.misc.GrapplingHookBlueprint;
import xyz.devvydont.treasureitems.blueprints.misc.OxygenHelmetBlueprint;
import xyz.devvydont.treasureitems.blueprints.misc.SpaceHelmetBlueprint;
import xyz.devvydont.treasureitems.blueprints.misc.StrengthCharmBlueprint;
import xyz.devvydont.treasureitems.blueprints.tools.*;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CustomItemManager implements Listener {

    public static final NamespacedKey CUSTOM_ITEM_KEY = new NamespacedKey(xyz.devvydont.treasureitems.TreasureItems.getInstance(), "CUSTOM_ITEM_TYPE");

    private final Map<String, CustomItemBlueprint> keyToCustomItem = new HashMap<>();
    private final Map<Class<? extends CustomItemBlueprint>, CustomItemBlueprint> classToCustomItem = new HashMap<>();

    public CustomItemManager() {
        // Register for events
        xyz.devvydont.treasureitems.TreasureItems.getInstance().getServer().getPluginManager().registerEvents(this, xyz.devvydont.treasureitems.TreasureItems.getInstance());

        // Register the custom items
        registerCustomItem(new CopiousPickaxeBlueprint());
        registerCustomItem(new GigadrillPickaxeBlueprint());

        registerCustomItem(new SuperRodBlueprint());

        registerCustomItem(new SharpenedSwordBlueprint());
        registerCustomItem(new ProsperousSwordBlueprint());
        registerCustomItem(new SharpenedAxeBlueprint());
        registerCustomItem(new PunchBowBlueprint());

        registerCustomItem(new HeartyHelmetBlueprint());
        registerCustomItem(new HeartyChestplateBlueprint());
        registerCustomItem(new HeartyLeggingsBlueprint());
        registerCustomItem(new HeartyBootsBlueprint());
        registerCustomItem(new LuckyPantsBlueprint());
        registerCustomItem(new SpeedsterBootsBlueprint());

        registerCustomItem(new ReinforcedElytraBlueprint());

        registerCustomItem(new KnockbackStickBlueprint());
        registerCustomItem(new SpeedTotemBlueprint());
        registerCustomItem(new LuckyCharmBlueprint());
        registerCustomItem(new FeatherTridentBlueprint());

        registerCustomItem(new GrapplingHookBlueprint());
        registerCustomItem(new StrengthCharmBlueprint());
        registerCustomItem(new SteppingBootsBlueprint());
        registerCustomItem(new SpaceHelmetBlueprint());
        registerCustomItem(new OxygenHelmetBlueprint());
    }

    public void registerCustomItem(CustomItemBlueprint blueprint) {
        CustomItemBlueprint old = keyToCustomItem.put(blueprint.key(), blueprint);

        if (blueprint.key() == null)
            throw new IllegalArgumentException("Blueprint " + blueprint.getClass().getName() + " has a null key!");

        if (old != null)
            throw new IllegalArgumentException("Already registered item with key " + blueprint.key());

        old = classToCustomItem.put(blueprint.getClass(), blueprint);

        if (old != null)
            throw new IllegalArgumentException("Already registered item with key " + blueprint.key());
    }

    public CustomItemBlueprint getBlueprint(Class<? extends CustomItemBlueprint> clazz) {

        if (!classToCustomItem.containsKey(clazz))
            throw new NullPointerException("No custom item with class: " + clazz.getName() + ". Did you forget to register it?");

        return classToCustomItem.get(clazz);
    }

    public CustomItemBlueprint getBlueprint(ItemStack item) {

        // No item or item meta means it cannot be a custom item
        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta())
            return null;

        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        String itemKey = data.getOrDefault(CUSTOM_ITEM_KEY, PersistentDataType.STRING, "");
        CustomItemBlueprint foundBlueprint = keyToCustomItem.get(itemKey);

        if (foundBlueprint != null && !foundBlueprint.matches(item))
            foundBlueprint = null;

        return foundBlueprint;
    }

    public CustomItemBlueprint getBlueprint(String key) {

        if (!keyToCustomItem.containsKey(key))
            throw new IllegalArgumentException("No custom item with key: " + key + "!");

        return keyToCustomItem.get(key);
    }

    public CustomItemBlueprint getRandomBlueprint() {
        // Retrieve a random blueprint that is registered
        return (CustomItemBlueprint) getBlueprints().toArray()[new Random().nextInt(getBlueprints().size())];
    }

    public Collection<CustomItemBlueprint> getBlueprints() {
        return keyToCustomItem.values();
    }

    @EventHandler
    public void onInteractWithCustomItem(PlayerInteractEvent e) {

        // Only listen to events where we have a custom item
        CustomItemBlueprint usedItem = getBlueprint(e.getItem());
        if (usedItem == null)
            return;

        // Handle what we should do
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            usedItem.onRightClick(e);

        else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
            usedItem.onLeftClick(e);

    }

    @EventHandler
    public void onClickCustomItem(InventoryClickEvent e) {

        // Only listen to events where we have a custom item
        CustomItemBlueprint usedItem = getBlueprint(e.getCurrentItem());
        if (usedItem == null)
            return;

        usedItem.onInventoryClick(e);

    }

    @EventHandler
    public void onBreakWithCustomItem(BlockBreakEvent event) {

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR))
            return;

        // Only listen to events where we have a custom item
        CustomItemBlueprint usedItem = getBlueprint(item);
        if (usedItem == null)
            return;

        usedItem.onBlockBreak(event);
    }

    @EventHandler
    public void onFishWithCustomItem(PlayerFishEvent event) {

        if (event.getHand() == null)
            return;

        ItemStack rod = event.getPlayer().getInventory().getItem(event.getHand());

        if (rod.getType().equals(Material.AIR))
            return;

        // Only listen to events where we have a custom item
        CustomItemBlueprint usedItem = getBlueprint(rod);
        if (usedItem == null)
            return;

        usedItem.onFish(event);
    }

    // Events related to fixing/updating items
    @EventHandler
    public void onPlayerInventoryClose(InventoryCloseEvent event) {

        // Don't fix guis
        if (event.getInventory().getHolder() == null)
            return;

        // Only fix player inventories
        if (!event.getInventory().getType().equals(InventoryType.CRAFTING))
            return;

        // Every time an inventory is open fix everything in it
        for (ItemStack item : event.getInventory().getContents())
            if (getBlueprint(item) != null)
                getBlueprint(item).fix(item);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {

        // Don't fix guis
        if (event.getInventory().getHolder() == null)
            return;

        // Every time an inventory is open fix everything in it
        for (ItemStack item : event.getInventory().getContents())
            if (getBlueprint(item) != null)
                getBlueprint(item).fix(item);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Every time a player joins fix everything in their inventory
        for (ItemStack item : event.getPlayer().getInventory().getContents())
            if (getBlueprint(item) != null)
                getBlueprint(item).fix(item);
    }

    @EventHandler
    public void onPlaceCustomItem(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();
        if (item.getType().equals(Material.AIR))
            return;

        // Only listen to events where we have a custom item
        CustomItemBlueprint usedItem = getBlueprint(item);
        if (usedItem == null)
            return;

        event.setCancelled(true);
    }

    public void cleanup() {
    }
}

