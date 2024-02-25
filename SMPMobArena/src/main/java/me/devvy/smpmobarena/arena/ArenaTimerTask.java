package me.devvy.smpmobarena.arena;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Helper class for running various timers everywhere
 */
public abstract class ArenaTimerTask extends BukkitRunnable {

    private final MobArena arena;
    private int ticksRemaining;

    public ArenaTimerTask(MobArena arena, int ticks) {
        this.arena = arena;
        this.ticksRemaining = ticks;
    }

    public MobArena getArena() {
        return arena;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    public abstract void tick();  // Perform action every tick, not called on final tick
    public abstract void finish();  // Perform action when timer is finished

    @Override
    public void run() {

        if (ticksRemaining <= 0) {
            finish();
            cancel();
            return;
        }

        tick();
        ticksRemaining--;
    }
}
