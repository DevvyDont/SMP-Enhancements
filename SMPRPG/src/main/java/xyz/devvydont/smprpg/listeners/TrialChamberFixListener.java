package xyz.devvydont.smprpg.listeners;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.Block;
import org.bukkit.block.Vault;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

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

        // todo, implement logic for using the key :3
        event.getPlayer().sendMessage(ComponentUtils.create("Not implemented yet!", NamedTextColor.RED));
    }

}
