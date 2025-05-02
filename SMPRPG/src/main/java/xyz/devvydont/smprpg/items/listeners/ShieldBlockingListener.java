package xyz.devvydont.smprpg.items.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.interfaces.IShield;
import xyz.devvydont.smprpg.services.ItemService;

/*
 * Responsible for implementing shield blocking mechanics.
 */
public class ShieldBlockingListener implements Listener {

    @EventHandler
    public void onBlockDamageWithShield(CustomEntityDamageByEntityEvent event) {

        // Is the receiver a player?
        if (!(event.getDamaged() instanceof Player player))
            return;

        // Are they blocking?
        if (!player.isBlocking())
            return;

        // Are they holding a shield?
        ItemStack main = player.getInventory().getItemInMainHand();
        ItemStack off = player.getInventory().getItemInOffHand();

        // Find the best possible stats we can for delay and damage reduction
        double damageReduction = 0;
        int delay = 999_999;
        boolean isHoldingShield = false;

        ItemService itemService = SMPRPG.getInstance().getItemService();
        if (!main.getType().equals(Material.AIR) && itemService.getBlueprint(main) instanceof IShield shieldable) {
            damageReduction = Math.max(damageReduction, shieldable.getDamageBlockingPercent());
            delay = Math.min(delay, shieldable.getShieldDelay());
            isHoldingShield = true;
        }

        if (!off.getType().equals(Material.AIR) && itemService.getBlueprint(off) instanceof IShield shieldable) {
            damageReduction = Math.max(damageReduction, shieldable.getDamageBlockingPercent());
            delay = Math.min(delay, shieldable.getShieldDelay());
            isHoldingShield = true;
        }

        // Didn't find a shield?
        if (!isHoldingShield)
            return;

        // Apply the values and reduce damage
        event.multiplyDamage(1-damageReduction);
        player.setShieldBlockingDelay(delay);
        player.setCooldown(Material.SHIELD, delay);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
    }

}
