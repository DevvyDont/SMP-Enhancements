package me.devvy.smpparkour.player;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.checkpoints.Checkpoint;
import me.devvy.smpparkour.config.ConfigManager;
import me.devvy.smpparkour.events.PlayerCompletedParkourEvent;
import me.devvy.smpparkour.items.blueprints.*;
import me.devvy.smpparkour.util.Announcer;
import me.devvy.smpparkour.util.Fireworks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;

import static me.devvy.smpparkour.player.ParkourTimer.PLAYER_TIMER_DECIMAL_FORMAT;

public class ParkourPlayer {

    private final Player player;  // We don't track offline players so we can do this smile
    private final PlayerStateRollback rollbackState;  // What to revert to when we are done
    private final ParkourTimer timer;
    private Checkpoint currentCheckpoint;
    private boolean practicing = false;

    private Location practiceCheckpointOverride;

    public ParkourPlayer(Player player) {
        this.player = player;
        this.rollbackState = new PlayerStateRollback(player);
        this.timer = new ParkourTimer(this);
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerStateRollback getRollbackState() {
        return rollbackState;
    }

    public ParkourTimer getTimer() {
        return timer;
    }

    public Checkpoint getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setCurrentCheckpoint(Checkpoint currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    public boolean isPracticing() {
        return practicing;
    }

    @Nullable
    public Location getPracticeCheckpointOverride() {
        return practiceCheckpointOverride;
    }

    public void setPracticeCheckpointOverride(Location practiceCheckpointOverride) {
        this.practiceCheckpointOverride = practiceCheckpointOverride;
    }

    /**
     * What should we do when the player enters parkour world?
     */
    public void enter() {
        // Clear inventory/effects, start timer
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);

        for (PotionEffectType potEffect : PotionEffectType.values())
            player.removePotionEffect(potEffect);

        timer.runTaskTimerAsynchronously(SMPParkour.getInstance(), 0, 1);
        timer.reset();
        setLobbyLoadout();

        SMPParkour.getInstance().getScoreboardUtil().addPlayer(player);

        // Perform teleport, send messages/play sounds
        player.sendMessage(Component.text("Joining parkour world, inventory stowed", NamedTextColor.YELLOW));
        player.teleport(SMPParkour.getInstance().getParkourWorld().getSpawnLocation());
        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        SMPParkour.getInstance().getParkourWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    /**
     * What should we do if the player is leaving parkour world?
     */
    public void exit() {
        // Give them their inventory back, health, gamemode, levels, location etc, stop the timer task
        SMPParkour.getInstance().getScoreboardUtil().removePlayer(player);
        player.setAllowFlight(false);
        rollbackState.restore(player);
        timer.cancel();
        player.playerListName(player.displayName());

        // Tell them what happened and play a cute sound
        player.sendMessage(Component.text("Leaving parkour world, inventory restored", NamedTextColor.YELLOW));
        player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        SMPParkour.getInstance().getParkourWorld().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    public void setLobbyLoadout() {
        player.getInventory().clear();
        player.getInventory().addItem(SMPParkour.getInstance().getItemManager().getBlueprint(StartParkourBlueprint.class).get());
        player.getInventory().addItem(SMPParkour.getInstance().getItemManager().getBlueprint(PracticeModeBlueprint.class).get());
    }

    public void setRunningLoadout() {
        player.getInventory().clear();
        player.getInventory().setItem(3, SMPParkour.getInstance().getItemManager().getBlueprint(LastCheckpointParkourBlueprint.class).get());
        player.getInventory().setItem(7, SMPParkour.getInstance().getItemManager().getBlueprint(ResetParkourBlueprint.class).get());
        player.getInventory().setItem(8, SMPParkour.getInstance().getItemManager().getBlueprint(EndParkourBlueprint.class).get());
    }

    public void setPracticeLoadout() {
        setRunningLoadout();
        player.getInventory().setItem(4, SMPParkour.getInstance().getItemManager().getBlueprint(SetPracticeCheckpointBlueprint.class).get());
        player.getInventory().setItem(5, SMPParkour.getInstance().getItemManager().getBlueprint(TeleportPracticeCheckpointBlueprint.class).get());
        player.getInventory().setItem(6, SMPParkour.getInstance().getItemManager().getBlueprint(EnableFlightBlueprint.class).get());
    }

    /**
     * Call to start a fresh new run
     */
    public void startParkour() {
        practicing = false;
        // Reset timer, give them their shit, and set their checkpoint to one
        timer.start();
        setRunningLoadout();
        Checkpoint start = SMPParkour.getInstance().getMapManager().getMap().getCheckpoints()[0];
        setCurrentCheckpoint(start);
        player.teleport(getCurrentCheckpoint().getSpawn());

        Component sub = Component.text(start.getName(), SMPParkour.getInstance().getMapManager().getMap().getPercentageColor(0));
        player.showTitle(Title.title(
                Component.empty(),
                sub.append(Component.text(String.format(" [%s]", start.getIndex()+1), NamedTextColor.GRAY))
        ));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.5f);

        player.setAllowFlight(false);
        player.setInvulnerable(false);
    }

    public void startPractice() {
        startParkour();
        timer.startPractice();
        setPracticeLoadout();
        practicing = true;
    }

    /**
     * Call to end a parkour attempt as a finish
     */
    public void finishParkour() {

        timer.stop();
        setLobbyLoadout();
        player.teleport(SMPParkour.getInstance().getParkourWorld().getSpawnLocation());
        new PlayerCompletedParkourEvent(this).callEvent();

        if (practicing) {
            player.setAllowFlight(false);
            setPracticeCheckpointOverride(null);
            return;
        }

        new PlayerCompletedParkourEvent(this).callEvent();
    }

    /**
     * Call to end a parkour attempt as a DNF
     */
    public void endParkour() {
        timer.reset();
        setLobbyLoadout();
        player.teleport(SMPParkour.getInstance().getParkourWorld().getSpawnLocation());
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        player.setAllowFlight(false);
    }

}
