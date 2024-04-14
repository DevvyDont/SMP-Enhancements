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
        player.clearTitle();
        timer.stop();
        setLobbyLoadout();

        player.teleport(SMPParkour.getInstance().getParkourWorld().getSpawnLocation());
        Fireworks.spawnFireworksInstantly(player.getEyeLocation(), Color.YELLOW);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);

        if (practicing) {
            player.setAllowFlight(false);
            setPracticeCheckpointOverride(null);
            return;
        }

        Bukkit.broadcast(
                Announcer.PREFIX
                        .append(player.displayName().color(NamedTextColor.AQUA))
                        .append(Component.text(" has finished the parkour with a time of ", NamedTextColor.GRAY))
                        .append(Component.text(timer.getTimeFormattedString(), NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
        );

        // Retrieve the map's key to test for and a few variables that will help us with condition checking.
        String mapKey = SMPParkour.getInstance().getMapManager().getMap().getMapPath();
        ConfigManager.TimeEntry previousRecord = ConfigManager.queryPlayerTime(player.getUniqueId(), mapKey);
        boolean hasPreviousRecord = previousRecord != null;

        // Test for a PB. but first, first time?
        if (!hasPreviousRecord) {

            ConfigManager.savePlayerTime(player, SMPParkour.getInstance().getMapManager().getMap().getMapPath(), timer.elapsedTimeMs());
            Fireworks.spawnFireworksInstantly(player.getEyeLocation().subtract(0, .5, 0), Color.AQUA);
            Bukkit.broadcast(
                    Announcer.PREFIX
                            .append(Component.text("This was their ", NamedTextColor.GRAY))
                            .append(Component.text("first attempt", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
            );
            new PlayerCompletedParkourEvent(this).callEvent();
            return;
        }

        // Now actually test, was this a PB?
        long recordTimeMS = previousRecord.getTimeMs();
        float diff = (recordTimeMS - timer.elapsedTimeMs()) / 1000f;
        diff = Math.abs(diff);
        if (recordTimeMS <= timer.elapsedTimeMs()) {
            player.sendMessage(
                    Component.text("You missed your personal best by ", NamedTextColor.GRAY)
                            .append(Component.text(String.format("+" + "%d:%s", (int)diff / 60, PLAYER_TIMER_DECIMAL_FORMAT.format(diff % 60)), NamedTextColor.RED, TextDecoration.BOLD))
                            .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
            );
            new PlayerCompletedParkourEvent(this).callEvent();
            return;
        }

        // We have a PB
        ConfigManager.savePlayerTime(player, SMPParkour.getInstance().getMapManager().getMap().getMapPath(), timer.elapsedTimeMs());
        Fireworks.spawnFireworksInstantly(player.getEyeLocation().subtract(0, .5, 0), Color.FUCHSIA);
        Bukkit.broadcast(
                Announcer.PREFIX
                        .append(Component.text("They beat their personal best by ", NamedTextColor.GRAY))
                        .append(Component.text(String.format("-" + "%d:%s", (int)diff / 60, PLAYER_TIMER_DECIMAL_FORMAT.format(diff % 60)), NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
        );

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
