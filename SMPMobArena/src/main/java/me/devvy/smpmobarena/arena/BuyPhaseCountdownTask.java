package me.devvy.smpmobarena.arena;

import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;

public class BuyPhaseCountdownTask extends ArenaTimerTask {

    public static final int BUY_PHASE_TIME = 30;  // 30 Seconds
    public static final int PERIOD = 20;  // Once a second

    public BuyPhaseCountdownTask(MobArena arena) {
        super(arena, BUY_PHASE_TIME);
    }

    @Override
    public void tick() {

        for (ArenaPlayer ap : getArena().getPlayerManager().getAllPlayersInArena()) {
            ap.getPlayer().sendActionBar(Component.text("Buy phase! " + getTicksRemaining() + " seconds until next wave!", NamedTextColor.GOLD));

            if (getTicksRemaining() <= 5)
                ap.getPlayer().playSound(ap.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
        }

    }

    @Override
    public void finish() {

        if (getArena().getGameplayManager().getState() != ArenaGameplayManager.ArenaState.BUY_IN_PROGRESS) {
            SMPMobArena.getInstance().getLogger().warning("Buy phase finished but arena state was not set to BUY_IN_PROGRESS!");
            return;
        }

        getArena().getGameplayManager().nextState();
    }

}
