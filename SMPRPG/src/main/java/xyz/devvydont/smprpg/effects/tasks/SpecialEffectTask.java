package xyz.devvydont.smprpg.effects.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.effects.services.SpecialEffectService;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

/*
 * Represents a base "effect" that players can have. These are typically referred to as "ailments" and a player can only
 * have one of these at a time. The simplest one is the "Shrouded" effect, where upon respawning players cannot be
 * targeted by hostile mobs for a short period of time to allow easy recovery for their items
 */
public abstract class SpecialEffectTask extends BukkitRunnable {

    // How many ticks to wait to run this task. 2 would be every 2 ticks, or 10 times a second.
    public static final int PERIOD = 2;

    protected final SpecialEffectService service;
    protected final Player player;
    int seconds;
    private int ticks = 0;

    public SpecialEffectTask(SpecialEffectService service, Player player, int seconds) {
        this.service = service;
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

    public abstract Component getExpiredComponent();

    public abstract Component getNameComponent();

    public abstract TextColor getTimerColor();

    /*
     * Logic to perform every tick of this ailment.
     */
    protected abstract void tick();

    /*
     * Logic to perform when the effect expires naturally from time running out. This is not called when the effect
     * is forcefully removed.
     */
    protected abstract void expire();

    /*
     * Logic to perform when the effect is forcefully removed instead of having time run out. This is not called when
     * the effect naturally expires from time running out.
     */
    public abstract void removed();

    private Component generateComponent(int seconds) {

        int displaySeconds = seconds-1;
        int minutes = displaySeconds / 60;

        boolean expired = seconds <= 0;
        Component time;
        String timestring = String.format("%d:%02d", minutes, seconds % 60);
        if (displaySeconds <= 59)
            timestring = String.format("%d.%d", displaySeconds, (9-ticks%10));
        if (expired)
            time = getExpiredComponent();
        else
            time = ComponentUtils.create(timestring, getTimerColor());
        return getNameComponent().append(ComponentUtils.create(" - ")).append(time);
    }

    public void sendActionBar() {
        sendActionBar(seconds);
    }

    public void sendActionBar(int seconds) {
        SMPRPG.getInstance().getActionBarService().addActionBarComponent(player, ActionBarService.ActionBarSource.AILMENT, generateComponent(seconds), 2);
    }

    @Override
    public void run() {

        ticks++;

        // Did they log out or did we lose the reference? Cancel the task if that is the case
        if (!player.isValid()) {
            removed();
            service.removeEffect(player.getUniqueId());
            cancel();
            return;
        }

        // If a second has gone by (PERIOD * ticks is divisible by the tick rate of the server), then take a second away
        if (PERIOD * ticks % 20 == 0)
            seconds--;

        // Announce to them how much time they have left with this effect
        tick();
        sendActionBar();

        // If the task expired, remove this task
        if (seconds <= 0) {
            expire();
            sendActionBar();
            service.removeEffect(player.getUniqueId());
            cancel();
            return;
        }
    }

}
