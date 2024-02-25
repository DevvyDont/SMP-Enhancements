package me.devvy.smpmobarena.arena;

import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class WaveCountdownTask extends ArenaTimerTask {

    public static final int WAVE_TIME = 60*5;  // 5 minutes
    // How long until the wave is considered "overtime" and we start decaying player HP
    public static final int OVERTIME_THRESHOLD = 60*1;  // 1 minute remaining
    public static final int PERIOD = 20;  // Once a second

    public WaveCountdownTask(MobArena arena) {
        super(arena, WAVE_TIME);
    }

    @Override
    public void tick() {

        int ticksUntilOvertime = getTicksRemaining() - OVERTIME_THRESHOLD;

        // Handle what we do every tick in overtime
        if (ticksUntilOvertime > 0) {

            for (ArenaPlayer ap : getArena().getPlayerManager().getAllPlayersInArena()) {
                ap.getPlayer().sendActionBar(Component.text("Defeat the enemies within " + ticksUntilOvertime + " seconds!", NamedTextColor.GOLD));
            }

        }
        // Handle what we do every tick before overtime
        else {

            for (ArenaPlayer ap : getArena().getPlayerManager().getAllPlayersInArena()) {
                ap.getPlayer().sendActionBar(Component.text("Death imminent in " + getTicksRemaining() + " seconds!", NamedTextColor.GOLD));
            }

        }

        // If overtime has just activated, tell everyone their HP is decaying
        if (ticksUntilOvertime == 0)
            for (ArenaPlayer ap : getArena().getPlayerManager().getAllPlayersInArena())
                ap.getPlayer().sendMessage(Component.text("SUDDEN DEATH, HP DECAY ACTIVE!", NamedTextColor.RED));


    }

    @Override
    public void finish() {

        // If we let this task finish, it means our players have timed out, and we lost, this is one of three finish conditions
        getArena().getPlayerManager().broadcast(Component.text("Times up!").color(NamedTextColor.RED));
        getArena().getGameplayManager().setState(ArenaGameplayManager.ArenaState.FAILED);
    }
}
