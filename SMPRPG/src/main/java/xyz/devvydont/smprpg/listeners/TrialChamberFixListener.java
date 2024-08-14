package xyz.devvydont.smprpg.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Vault;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.devvydont.smprpg.SMPRPG;

/**
 * Because our custom items override item meta of trial keys, the trial chamber vaults will not recognize them as
 * valid keys since it isn't  simple material check
 */
public class TrialChamberFixListener implements Listener {

    public TrialChamberFixListener(SMPRPG plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInteractWithVault(PlayerInteractEvent event) {

        // Only listen to right clicks where we aren't holding shift
        if (event.getAction().isLeftClick())
            return;

        if (event.getPlayer().isSneaking())
            return;

        Block clicked = event.getClickedBlock();
        if (clicked == null)
            return;

        if (!(clicked.getState() instanceof Vault vault))
            return;

        org.bukkit.block.data.type.Vault data = (org.bukkit.block.data.type.Vault) clicked.getBlockData();
        data.setTrialSpawnerState(org.bukkit.block.data.type.Vault.State.UNLOCKING);
        event.getClickedBlock().setBlockData(data);
    }

}
