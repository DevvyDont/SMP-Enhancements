package xyz.devvydont.smprpg.effects.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.effects.tasks.ShroudedEffect;

/*
 * Responsible for the application of the "Shrouded" special effect.
 * This is in its own class since the application of special effects cannot be contained in its class.
 */
public class ShroudedEffectListener implements Listener {

    private final SpecialEffectService service;

    public ShroudedEffectListener(SpecialEffectService service) {
        this.service = service;
    }

    /*
     * When a player respawns, give them the shrouded effect.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        service.giveEffect(event.getPlayer(), new ShroudedEffect(service, event.getPlayer(),  60*5));
    }

}
