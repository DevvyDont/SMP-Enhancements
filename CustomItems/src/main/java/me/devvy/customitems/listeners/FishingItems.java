package me.devvy.customitems.listeners;

import me.devvy.customitems.CustomItems;
import me.devvy.customitems.blueprints.CustomItemBlueprint;
import me.devvy.customitems.events.RareItemDropEvent;
import me.devvy.customitems.util.RNGRoller;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishingItems implements Listener {

    public static final float BASE_CHANCE = 1 / 750f;

    public FishingItems() {
        CustomItems.getInstance().getServer().getPluginManager().registerEvents(this, CustomItems.getInstance());
    }

    @EventHandler
    public void onCaughtFish(PlayerFishEvent event) {

        // If the player didn't catch a fish, we don't care
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;

        // If the player didn't catch an item, we don't care
        if (event.getCaught() == null)
            return;

        // If the player didn't catch an item, we don't care
        if (!(event.getCaught() instanceof Item))
            return;

        // Rare drop
        RNGRoller rngRoller = new RNGRoller(event.getPlayer(), BASE_CHANCE, Enchantment.LUCK);

        if (!rngRoller.roll())
            return;

        // They got a rare item
        CustomItemBlueprint blueprint = CustomItems.getInstance().getCustomItemManager().getRandomBlueprint();
        ItemStack drop = blueprint.get();

        Item caughtItem = (Item) event.getCaught();

        caughtItem.setItemStack(drop);

        Event itemDropEvent = new RareItemDropEvent(event.getPlayer(), caughtItem, rngRoller.getChance(), "fishing");
        Bukkit.getServer().getPluginManager().callEvent(itemDropEvent);
    }

}
