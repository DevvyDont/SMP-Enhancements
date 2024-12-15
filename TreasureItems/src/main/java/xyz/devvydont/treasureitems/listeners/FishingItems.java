package xyz.devvydont.treasureitems.listeners;

import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.events.RareItemDropEvent;
import xyz.devvydont.treasureitems.util.RNGRoller;
import org.bukkit.Bukkit;
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
        xyz.devvydont.treasureitems.TreasureItems.getInstance().getServer().getPluginManager().registerEvents(this, xyz.devvydont.treasureitems.TreasureItems.getInstance());
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
        if (!(event.getCaught() instanceof Item caughtItem))
            return;

        // Rare drop
        RNGRoller rngRoller = new RNGRoller(event.getPlayer(), BASE_CHANCE, Enchantment.LUCK_OF_THE_SEA);

        if (!rngRoller.roll())
            return;

        // They got a rare item
        CustomItemBlueprint blueprint = xyz.devvydont.treasureitems.TreasureItems.getInstance().getCustomItemManager().getRandomBlueprint();
        ItemStack drop = blueprint.get();

        caughtItem.setItemStack(drop);

        Event itemDropEvent = new RareItemDropEvent(event.getPlayer(), caughtItem, rngRoller.getChance(), "fishing");
        Bukkit.getServer().getPluginManager().callEvent(itemDropEvent);
    }

}
