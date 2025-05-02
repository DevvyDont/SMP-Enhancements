package xyz.devvydont.smprpg.items.blueprints.debug;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.ItemService;

public class InfinirocketBlueprint extends CustomItemBlueprint implements Listener {

    public InfinirocketBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateMeta(ItemStack itemStack) {
        super.updateMeta(itemStack);
        itemStack.addUnsafeEnchantment(EnchantmentService.KEEPING_BLESSING.getEnchantment(), 1);
    }

    @Override
    public boolean wantFakeEnchantGlow() {
        return true;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @EventHandler
    private void onRocket(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR))
            return;

        if (!isItemOfType(event.getItem()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    private void onRocket(PlayerElytraBoostEvent event) {

        if (!isItemOfType(event.getItemStack()))
            return;

        event.setShouldConsume(false);
    }


}
