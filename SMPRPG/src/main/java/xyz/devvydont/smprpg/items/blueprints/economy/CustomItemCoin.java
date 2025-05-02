package xyz.devvydont.smprpg.items.blueprints.economy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomItemCoin extends CustomItemBlueprint implements IHeaderDescribable, ISellable, Listener {

    private final int value;

    public static int getCoinValue(CustomItemType type) {

        return switch (type) {
            case COPPER_COIN -> 1;
            case SILVER_COIN -> 100;
            case GOLD_COIN -> 10_000;
            case PLATINUM_COIN -> 1_000_000;
            case ENCHANTED_COIN -> 100_000_000;
            default -> 0;
        };

    }

    public CustomItemCoin(ItemService itemService, CustomItemType coin) {
        super(itemService, coin);
        this.value = getCoinValue(coin);
    }

    /**
     * Similar to getValue(), but calculates the value of a coin based on a singular itemstack stack size.
     * If this is a coin, it will be some non-negative integer.
     *
     * @param stack The item stack of coins
     * @return
     */
    public int getStackValue(ItemStack stack) {
        return getCoinValue(this.getCustomItemType()) * stack.getAmount();
    }

    @Override
    public int getWorth(ItemStack item) {
        return getStackValue(item);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        List<Component> lines = new ArrayList<>();

        lines.add(ComponentUtils.create("The physical form of ")
            .append((ComponentUtils.create("currency ", NamedTextColor.GOLD)))
            .append(ComponentUtils.create("for this world")));

        lines.add(ComponentUtils.create("To put this in your account, use ")
            .append(ComponentUtils.create("/deposit", NamedTextColor.GREEN, TextDecoration.BOLD)));

        lines.add(ComponentUtils.EMPTY);

        lines.add(
                ComponentUtils.create("Worth of ")
                        .append(getNameComponent(itemStack))
                        .append(ComponentUtils.create(": "))
                        .append(ComponentUtils.create(EconomyService.formatMoney(getWorth(itemStack.asOne())), NamedTextColor.GOLD))
        );

        return lines;
    }

    // When the player picks up a coin, play a cute noise
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickup(PlayerAttemptPickupItemEvent event) {

        // Ignore the event if its cancelled
        if (event.isCancelled())
            return;

        // If this coin is owned by someone and we are trying to pick it up don't play the noise
        if (event.getItem().getOwner() != null && !event.getItem().getOwner().equals(event.getPlayer().getUniqueId()))
            return;

        // Ignore this item pickup event if the type of the item is not a coin
        ItemStack item = event.getItem().getItemStack();
        if (!isItemOfType(item))
            return;

        // Ignore this event if the item stack count isn't changing
        if (event.getRemaining() == event.getItem().getItemStack().getAmount())
            return;

        // Play cute noise :3
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setMaxStackSize(99);
    }

}
