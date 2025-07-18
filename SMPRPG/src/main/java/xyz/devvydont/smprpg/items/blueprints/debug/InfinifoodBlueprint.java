package xyz.devvydont.smprpg.items.blueprints.debug;

import io.papermc.paper.datacomponent.item.Consumable;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.time.TickTime;

public class InfinifoodBlueprint extends CustomItemBlueprint implements IEdible, Listener {

    public InfinifoodBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.addUnsafeEnchantment(EnchantmentService.KEEPING_BLESSING.getEnchantment(), 1);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getNutrition() {
        return 8;
    }

    @Override
    public float getSaturation() {
        return 8;
    }

    @Override
    public boolean canAlwaysEat() {
        return false;
    }

    @Override
    public Consumable getConsumableComponent() {
        return Consumable.consumable()
                .consumeSeconds(1.6f)
                .build();
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent event) {

        if (!isItemOfType(event.getItem()))
            return;

        event.setReplacement(event.getItem());
        Bukkit.getScheduler().runTaskLater(SMPRPG.getInstance(), () -> event.getPlayer().getInventory().setItem(event.getHand(), event.getItem()), TickTime.INSTANTANEOUSLY);
    }
}
