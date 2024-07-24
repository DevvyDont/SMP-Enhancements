package xyz.devvydont.smprpg.items.blueprints.economy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.ArrayList;
import java.util.List;

public class CustomItemCoin extends CustomItemBlueprint implements Listener {

    private final int value;

    public static int getCoinValue(CustomItemType type) {

        return switch (type) {
            case COPPER_COIN -> 1;
            case SILVER_COIN -> 10;
            case GOLD_COIN -> 100;
            case PLATINUM_COIN -> 1_000;
            case EMERALD_COIN -> 10_000;
            case AMETHYST_COIN -> 100_000;
            case ENCHANTED_COIN -> 1_000_000;
            default -> 0;
        };

    }

    public CustomItemCoin(ItemService itemService, CustomItemType coin) {
        super(itemService, coin);
        this.value = getCoinValue(coin);
    }

    public int getValue() {
        return value;
    }

    /**
     * Similar to getValue(), but calculates the value of a coin based on a singular itemstack stack size.
     * If this is a coin, it will be some non-negative integer. If it isn't a coin, it will have 0 value.
     *
     * @param itemStack
     * @return
     */
    public int getValue(ItemStack itemStack) {

        // Retrieve the blueprint of this potential custom item.
        SMPItemBlueprint blueprint = itemService.getBlueprint(itemStack);

        // If this custom item is not a coin, it is worth 0
        if (!(blueprint instanceof CustomItemCoin))
            return 0;

        // Multiply value of coin against stack size
        return itemStack.getAmount() * ((CustomItemCoin) blueprint).getValue();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> lines = new ArrayList<>();

        lines.add(Component.text("The physical form of ").color(NamedTextColor.GRAY)
            .append((Component.text("currency ").color(NamedTextColor.GOLD)))
            .append(Component.text("for this world").color(NamedTextColor.GRAY)));

        lines.add(Component.text("To put this in your account, use ").color(NamedTextColor.GRAY)
            .append(Component.text("/deposit").style(Style.style(NamedTextColor.GREEN, TextDecoration.BOLD))));

        lines.add(Component.empty());

        lines.add(
                Component.text("Worth of ").color(NamedTextColor.GRAY)
                        .append(getNameComponent(meta))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(EconomyService.formatMoney(getValue())).color(NamedTextColor.GOLD))
        );

        return lines;
    }

    // When the player picks up a coin, play a cute noise
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickup(PlayerAttemptPickupItemEvent event) {

        // Ignore the event if its cancelled
        if (event.isCancelled())
            return;

        // Ignore this item pickup event if the type of the item is not a coin
        ItemStack item = event.getItem().getItemStack();
        if (!isItemOfType(item))
            return;

        // Ignore this event if the player's inventory is full
        if (event.getPlayer().getInventory().firstEmpty() < 0)
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
