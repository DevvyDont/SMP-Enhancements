package me.devvy.smpparkour.config;

import me.devvy.smpparkour.SMPParkour;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ConfigManager {

    public static class TimeEntry {
        private final UUID uuid;
        private final String name;
        private final long timeMs;

        public TimeEntry(UUID uuid, String name, long timeMs) {
            this.uuid = uuid;
            this.name = name;
            this.timeMs = timeMs;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public long getTimeMs() {
            return timeMs;
        }
    }

    public static void initConfig() {

        // Ensure a times section exists
        if (SMPParkour.getInstance().getConfig().getConfigurationSection("times") == null) {
            SMPParkour.getInstance().getConfig().createSection("times");
            SMPParkour.getInstance().saveConfig();
        }

        // Ensure enabled announce
        if (!SMPParkour.getInstance().getConfig().contains("announce")) {
            SMPParkour.getInstance().getConfig().set("announce", true);
            SMPParkour.getInstance().saveConfig();
        }

    }

    /**
     * Get the section corresponding to a player's data, creates if it does not exist
     *
     * @param id
     * @return
     */
    public static ConfigurationSection getPlayerSection(UUID id) {
        ConfigurationSection sec = SMPParkour.getInstance().getConfig().getConfigurationSection("times." + id.toString());
        if (sec == null) {
            sec = SMPParkour.getInstance().getConfig().createSection("times." + id);
            SMPParkour.getInstance().saveConfig();
        }

        return sec;
    }

    public static void savePlayerTime(Player player, long timeMs) {
        ConfigurationSection section = getPlayerSection(player.getUniqueId());
        section.set("name", player.getName());
        section.set("time", timeMs);
        SMPParkour.getInstance().saveConfig();
    }

    public static boolean playerHasTime(UUID playerID) {
        return SMPParkour.getInstance().getConfig().getConfigurationSection("times." + playerID.toString()) != null;
    }

    public static long getPlayerTime(UUID playerID) {

        if (!playerHasTime(playerID))
            throw new IllegalArgumentException("This player must put a time on record before we can access it!");

        return getPlayerSection(playerID).getLong("time");
    }

    public static Map<UUID, TimeEntry> getAllTimesOnRecord() {
        Map<UUID, TimeEntry> times = new HashMap<>();

        Set<String> allPlayerIDs = SMPParkour.getInstance().getConfig().getConfigurationSection("times").getKeys(false);

        for (String stringID: allPlayerIDs) {
            UUID id = UUID.fromString(stringID);
            ConfigurationSection thisPlayerSec = getPlayerSection(id);
            times.put(id, new TimeEntry(id, thisPlayerSec.getString("name"), thisPlayerSec.getLong("time")));
        }

        return times;
    }

}
