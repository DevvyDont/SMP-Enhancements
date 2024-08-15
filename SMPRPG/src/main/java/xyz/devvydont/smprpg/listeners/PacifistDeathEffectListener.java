package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Currently, it sucks when you die. Apply some sort of "pacifist" effect on death to let people retrieve their items.
 * Doing any action other than moving around and placing blocks should clear this effect though.
 */
public class PacifistDeathEffectListener implements Listener {

    private Map<UUID, PacifistTask> currentTasks = new HashMap<>();

    private class PacifistTask extends BukkitRunnable {
        private Player player;
        int seconds;

        public PacifistTask(Player player, int seconds) {
            this.player = player;
            this.seconds = seconds;
        }

        public Player getPlayer() {
            return player;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds+1;
        }

        @Override
        public void run() {

            // Did they log out or did we lose the reference? Cancel the task if that is the case
            if (player.isValid()) {
                removePacifistEffect(player);
                return;
            }

            // If the task expired, remove this task
            if (seconds <= 0) {
                sendActionBar(player, 0);
                removePacifistEffect(player);
                return;
            }

            seconds--;
            // Announce to them that they are being pacified
            sendActionBar(player, seconds);
        }
    }

    SMPRPG plugin;

    public PacifistDeathEffectListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void sendActionBar(Player player, int seconds) {
        plugin.getActionBarService().addActionBarComponent(player, ActionBarService.ActionBarSource.PACIFIST, getPacifistComponent(seconds), 2);
    }

    private Component getPacifistComponent(int seconds) {

        int minutes = seconds / 60;

        boolean expired = seconds <= 0;
        Component time;
        String timestring = String.format("%d:%02d", minutes, seconds % 60);
        if (expired)
            time = ComponentUtil.getColoredComponent("EXPIRED!", NamedTextColor.RED);
        else
            time = ComponentUtil.getColoredComponent(timestring, NamedTextColor.GREEN);
        return ComponentUtil.getColoredComponent("Shrouded", NamedTextColor.GOLD).append(ComponentUtil.getDefaultText(" - ")).append(time);
    }

    public boolean hasPacifistEffect(Player player) {
        PacifistTask task = currentTasks.get(player.getUniqueId());
        if (task == null)
            return false;

        return task.getSeconds() > 0;
    }

    public void givePacifistEffect(Player player, int seconds) {

        // Remove if it is active
        removePacifistEffect(player);

        // Create a task and run it every second and store it
        PacifistTask task = new PacifistTask(player, seconds);
        task.runTaskTimer(plugin, 0, 20);
        currentTasks.put(player.getUniqueId(), task);
    }

    private void removePacifistEffect(Player player) {

        BukkitRunnable task = currentTasks.get(player.getUniqueId());

        // They don't have the effect, don't do anything
        if (task == null)
            return;

        // Cancel the task, send an expired action bar, and remove the reference to the task
        task.cancel();
        sendActionBar(player, 0);
        currentTasks.remove(player.getUniqueId());
    }

    /**
     * When a player respawns, give them the effect.
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onRespawn(PlayerDeathEvent event) {
        givePacifistEffect(event.getPlayer(), 60*5);
    }

    /**
     * Do not let pacifist effected entities get targeted.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {

        // We don't care for untarget events
        if (event.getTarget() == null)
            return;

        // We don't care for non player targets
        if (!(event.getTarget() instanceof Player player))
            return;

        // We don't care if the pacifist effect is not present on the target
        if (!hasPacifistEffect(player))
            return;

        event.setCancelled(true);
    }

    /**
     * If a player deals damage, remove their pacifist
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDealDamage(CustomEntityDamageByEntityEvent event) {

        // Ignore non players
        if (!(event.getDealer() instanceof Player player))
            return;

        removePacifistEffect(player);
    }

    /**
     * If a player triggers a loot event, remove their pacifist
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOpenLootChest(LootGenerateEvent event) {

        // Ignore non players
        if (!(event.getEntity() instanceof Player player))
            return;

        removePacifistEffect(player);
    }

}
