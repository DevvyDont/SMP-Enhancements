package me.devvy.smpmobarena.arena;

import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;

public class GameEndingCountdownTask extends ArenaTimerTask {

    public static final int COUNTDOWN_TIME = 10;  // 10 Seconds
    public static final int PERIOD = 20;  // Once a second

    public GameEndingCountdownTask(MobArena arena) {
        super(arena, COUNTDOWN_TIME);
    }

    @Override
    public void tick() {

        for (ArenaPlayer ap : getArena().getPlayerManager().getAllPlayersInArena()) {
            ap.getPlayer().sendActionBar(Component.text("Game ending in " + getTicksRemaining() + " seconds!", NamedTextColor.RED));
//            ap.getPlayer().playSound(ap.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
        }

    }

    @Override
    public void finish() {

        if (getArena().getGameplayManager().getState() != ArenaGameplayManager.ArenaState.ENDING) {
            SMPMobArena.getInstance().getLogger().warning("Ending countdown finished but arena state was not set to ENDING!");
            return;
        }

        getArena().getGameplayManager().nextState();

    }

}
