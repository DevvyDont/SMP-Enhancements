package me.devvy.smpparkour.listeners;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.config.ConfigManager;
import me.devvy.smpparkour.events.PlayerCompletedParkourEvent;
import me.devvy.smpparkour.player.ParkourPlayer;
import me.devvy.smpparkour.util.Announcer;
import me.devvy.smpparkour.util.Fireworks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.devvy.smpparkour.player.ParkourTimer.PLAYER_TIMER_DECIMAL_FORMAT;

/**
 * Event handling logic for when a player completes a parkour run.
 */
public class ParkourCompletedListener implements Listener {

    public ParkourCompletedListener() {
        SMPParkour.getInstance().getServer().getPluginManager().registerEvents(this, SMPParkour.getInstance());
    }

    /**
     * Call to announce a message in chat and play effects when a player completes a parkour for every case.
     *
     * @param player Player who completed the parkour
     * @param time   The formatted time string
     */
    private void announceParkourCompletion(Player player, String time) {
        Bukkit.broadcast(
                Announcer.PREFIX
                        .append(player.displayName().color(NamedTextColor.AQUA))
                        .append(Component.text(" has finished the parkour with a time of ", NamedTextColor.GRAY))
                        .append(Component.text(time, NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
        );
        Fireworks.spawnFireworksInstantly(player.getEyeLocation(), Color.YELLOW);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
    }

    /**
     * Call to announce a message in chat and play effects when a player completes a parkour for the very first time.
     *
     * @param player Player who completed the parkour
     */
    private void announceFirstTimeCompletion(Player player) {
        Fireworks.spawnFireworksInstantly(player.getEyeLocation().subtract(0, .5, 0), Color.AQUA);
        Bukkit.broadcast(
                Announcer.PREFIX
                        .append(Component.text("This was their ", NamedTextColor.GRAY))
                        .append(Component.text("first attempt", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
        );
    }

    /**
     * Call to announce a message in chat and play effects for when a player achieved a personal best.
     *
     * @param player Player who completed the parkour (and PB'd)
     * @param diff   The time they saved in seconds based on their old record
     */
    private void announcePersonalBest(Player player, float diff) {
        Fireworks.spawnFireworksInstantly(player.getEyeLocation().subtract(0, .5, 0), Color.FUCHSIA);
        Bukkit.broadcast(
                Announcer.PREFIX
                        .append(Component.text("They beat their personal best by ", NamedTextColor.GRAY))
                        .append(Component.text(String.format("-" + "%d:%s", (int)diff / 60, PLAYER_TIMER_DECIMAL_FORMAT.format(diff % 60)), NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
        );
    }

    /**
     * Call to privately message a player how close they were to beating their old record.
     *
     * @param player Player who completed the parkour
     * @param diff   How many seconds the player missed their record by
     */
    private void sendPersonalBestMiss(Player player, float diff) {
        player.sendMessage(
                Component.text("You missed your personal best by ", NamedTextColor.GRAY)
                        .append(Component.text(String.format("+" + "%d:%s", (int)diff / 60, PLAYER_TIMER_DECIMAL_FORMAT.format(diff % 60)), NamedTextColor.RED, TextDecoration.BOLD))
                        .append(Component.text("!", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
        );
    }

    /**
     * Responsible for announcing when a player completed a parkour course and saving their record.
     *
     * @param event PlayerCompletedParkourEvent (thrown upon player completing parkour course)
     */
    @EventHandler
    public void onPlayerCompletedParkour(PlayerCompletedParkourEvent event) {

        ParkourPlayer parkourPlayerWrapper = event.getPlayer();
        Player player = parkourPlayerWrapper.getPlayer();

        // If this player was practicing, do not broadcast anything or save any records
        if (event.isPractice())
            return;

        // Announce this player's completion and play any special effects
        announceParkourCompletion(player, event.getFormattedTime());

        // Retrieve the map's key to test for and a few variables that will help us with condition checking.
        String mapKey = SMPParkour.getInstance().getMapManager().getMap().getMapPath();
        ConfigManager.TimeEntry previousRecord = ConfigManager.queryPlayerTime(player.getUniqueId(), mapKey);
        boolean hasPreviousRecord = previousRecord != null;
        long timeDifferentialMs = hasPreviousRecord ? previousRecord.getTimeMs() - event.getTimeMs() : event.getTimeMs();
        timeDifferentialMs = Math.abs(timeDifferentialMs);  // No point on storing negatives here
        float timeDifferentialS = timeDifferentialMs / 1000f;

        // Test for the case where they have completed a run, but this was not a personal best.
        // In this case, we don't set their new time.
        if (hasPreviousRecord && previousRecord.getTimeMs() < event.getTimeMs()) {
            sendPersonalBestMiss(player, timeDifferentialS);
            return;
        }

        // This was either a personal best or a first time completion, go ahead and save the time.
        ConfigManager.savePlayerTime(player, mapKey, event.getTimeMs());

        // Was this their first time?
        if (!hasPreviousRecord) {
            announceFirstTimeCompletion(player);
            return;
        }

        // From here on out, we know a player has improved on a previous time.
        announcePersonalBest(player, timeDifferentialS);
    }
}
