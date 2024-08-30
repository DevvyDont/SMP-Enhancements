package xyz.devvydont.smprpg.effects.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.world.LootGenerateEvent;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;

public class ShroudedEffect extends SpecialEffectTask implements Listener {

    public ShroudedEffect(SpecialEffectService service, Player player, int seconds) {
        super(service, player, seconds);
    }

    @Override
    public Component getExpiredComponent() {
        return Component.text("EXPIRED!", NamedTextColor.RED);
    }

    @Override
    public Component getNameComponent() {
        return Component.text("Shrouded!", NamedTextColor.GOLD);
    }

    @Override
    public TextColor getTimerColor() {
        return NamedTextColor.GREEN;
    }

    @Override
    protected void tick() {
        // No need to do anything!
    }

    @Override
    protected void expire() {
        // No need to do anything!
    }

    @Override
    public void removed() {
        // No need to do anything!
    }

    /*
     * When an entity targets our player that has the effect, don't allow it to happen
     */
    @EventHandler
    public void onPlayerTargeted(EntityTargetEvent event) {

        // We don't care for untarget events
        if (event.getTarget() == null)
            return;

        // We don't care for non player targets
        if (!(event.getTarget() instanceof Player eventPlayer))
            return;

        // We don't care unless the player targeted is the owner of this effect.
        if (!eventPlayer.equals(getPlayer()))
            return;

        // Our player is being targeted.
        event.setCancelled(true);
    }

    /*
     * If our player deals damage, remove their shrouded effect.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDealDamage(CustomEntityDamageByEntityEvent event) {

        // Ignore non players
        if (!(event.getDealer() instanceof Player eventPlayer))
            return;

        if (!eventPlayer.equals(getPlayer()))
            return;

        service.removeEffect(eventPlayer);
    }

    /**
     * If a player triggers a loot event, remove their pacifist
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOpenLootChest(LootGenerateEvent event) {

        // Ignore non players
        if (!(event.getEntity() instanceof Player eventPlayer))
            return;

        // Ignore players that aren't our player
        if (!eventPlayer.equals(player))
            return;

        service.removeEffect(eventPlayer);
    }
}
