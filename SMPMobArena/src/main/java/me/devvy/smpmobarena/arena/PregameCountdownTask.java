package me.devvy.smpmobarena.arena;

import me.devvy.smpmobarena.SMPMobArena;
import me.devvy.smpmobarena.player.ArenaPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;

public class PregameCountdownTask extends ArenaTimerTask {

    public static final int COUNTDOWN_TIME = 20;  // 20 Seconds
    public static final int PERIOD = 20;  // Once a second

    public PregameCountdownTask(MobArena arena) {
        super(arena, COUNTDOWN_TIME);
    }

    @Override
    public void tick() {

        for (ArenaPlayer ap : getArena().getPlayerManager().getAllPlayersInArena()) {
            ap.getPlayer().sendActionBar(Component.text("Game starting in " + getTicksRemaining() + " seconds!", NamedTextColor.GOLD));
            ap.getPlayer().playSound(ap.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
        }

    }

    @Override
    public void finish() {

        if (getArena().getGameplayManager().getState() != ArenaGameplayManager.ArenaState.STARTING) {
            SMPMobArena.getInstance().getLogger().warning("Pregame countdown finished but arena state was not set to STARTING!");
            return;
        }

        // Teleport the players in to the arena, and set their beginning stats and stuff
        for (ArenaPlayer ap : getArena().getGameplayManager().getPlayers()) {
            ap.getPlayer().sendMessage(Component.text("The game has started and you are a player!", NamedTextColor.GREEN));
            ap.setSafeLocation(ap.getPlayer().getLocation());
            ap.getPlayer().teleport(getArena().getArenaOrigin());
            ap.getPlayer().getWorld().playSound(ap.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            ap.getPlayer().setInvulnerable(false);
        }

        getArena().getGameplayManager().nextState();

    }


}
