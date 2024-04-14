package me.devvy.smpparkour.config;

import me.devvy.smpparkour.SMPParkour;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ConfigManager {

    // General config options unrelated to time storing
    public static final String CONFIG_FIELD_TIMES = "records";
    public static final String CONFIG_FIELD_ANNOUNCE = "options.announce";

    // Time storage keys per record
    public static final String CONFIG_FIELD_NAME = "name";
    public static final String CONFIG_FIELD_MAP = "map";
    public static final String CONFIG_FIELD_TIME = "time";

    public static class TimeEntry {
        private final UUID uuid;  // Unique player ID
        private final String name;  // Cached name of player
        private final String map;  // Unique map name identifier, typically we use map path
        private final long timeMs;  // Time in milliseconds for how long a run took

        /**
         * Construct a TimeEntry instance manually.
         *
         * @param uuid
         * @param name
         * @param map
         * @param timeMs
         */
        public TimeEntry(UUID uuid, String name, String map, long timeMs) {
            this.uuid = uuid;
            this.name = name;
            this.map = map;
            this.timeMs = timeMs;
        }

        public TimeEntry(UUID uuid, ConfigurationSection section) {
            this.uuid = uuid;
            this.name = section.getString(CONFIG_FIELD_NAME);
            this.map = section.getString(CONFIG_FIELD_MAP);
            this.timeMs = section.getLong(CONFIG_FIELD_TIME);
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public String getMap() {
            return map;
        }

        public long getTimeMs() {
            return timeMs;
        }

        /**
         * A unique identifier for this time record.
         * Allows us to overwrite old records
         *
         * @return A string representing a yml object path on where to save times
         */
        public String key() {
            return String.format("%s.%s.%s", CONFIG_FIELD_TIMES, map, uuid);
        }

        /**
         * Saves this TimeEntry to the config using its key() method and instance attributes.
         * This method will also save the config file.
         */
        public void save() {
            // Find the section, create it if it doesn't exist. Save data!
            ConfigurationSection section = SMPParkour.getInstance().getConfig().getConfigurationSection(key());
            if (section == null)
                section = SMPParkour.getInstance().getConfig().createSection(key());

            section.set(CONFIG_FIELD_NAME, getName());
            section.set(CONFIG_FIELD_MAP, getMap());
            section.set(CONFIG_FIELD_TIME, getTimeMs());
            SMPParkour.getInstance().saveConfig();
        }
    }

    /**
     * A fake TimeEntry class that can be used to query for entries instead by utilizing the TimeQuery key() method.
     * Only
     */
    public static class QueryEntry extends TimeEntry {

        public QueryEntry(UUID uuid, String map) {
            super(uuid, "", map, 0);
        }

        @Nullable
        public TimeEntry query() {

            // Check if it is null and return null if so.
            ConfigurationSection section = SMPParkour.getInstance().getConfig().getConfigurationSection(this.key());
            if (section == null)
                return null;

            // We found something, so we can construct a real TimeEntry instance.
            return new TimeEntry(getUuid(), section);
        }
    }

    /**
     * Determine if we want to announce events related to this plugin
     *
     * @return true if we should announce things, false otherwise
     */
    public static boolean wantAnnouncements() {
        return SMPParkour.getInstance().getConfig().getBoolean(CONFIG_FIELD_ANNOUNCE, true);
    }

    public static void initConfig() {

        // Ensure announce is a setting in the config, if we add more options we should automate this process.
        if (!SMPParkour.getInstance().getConfig().contains(CONFIG_FIELD_ANNOUNCE)) {
            SMPParkour.getInstance().getConfig().set(CONFIG_FIELD_ANNOUNCE, true);
            SMPParkour.getInstance().saveConfig();
        }

    }

    /**
     * Makes a new TimeEntry and saves it.
     *
     * @param player The player to save data for.
     * @param map The unique map identifier (map path, not map name)
     * @param timeMs Time in milliseconds to save this record for
     */
    public static void savePlayerTime(Player player, String map, long timeMs) {
        TimeEntry newEntry = new TimeEntry(player.getUniqueId(), player.getName(), map, timeMs);
        newEntry.save();
    }

    /**
     * Query the config for a player's time for a specific map.
     *
     * @param playerID
     * @param map
     * @return a TimeEntry instance if it exists, otherwise null if this player hasn't beaten a map yet.
     */
    @Nullable
    public static TimeEntry queryPlayerTime(UUID playerID, String map) {
        return new QueryEntry(playerID, map).query();
    }

    /**
     * Check if a player has a time recorded for a certain map.
     *
     * @param playerID Player's UUID
     * @param map Map name to check for, remember this is unique map key (map path in its config)
     * @return true if they have a time on record false otherwise.
     */
    public static boolean playerHasTime(UUID playerID, String map) {
        return queryPlayerTime(playerID, map) != null;
    }

    /**
     * Returns a List of all times currently on record for the entire server (for every map).
     *
     * @return A List instance with TimeEntry objects.
     */
    public static List<TimeEntry> getAllTimesOnRecord() {

        List<TimeEntry> records = new ArrayList<>();

        // Retrieve the section that stores times, if it doesn't exist then that means we have none.
        ConfigurationSection section = SMPParkour.getInstance().getConfig().getConfigurationSection(CONFIG_FIELD_TIMES);
        if (section == null)
            return records;

        // Loop through every key and construct a new TimeEntry based on its corresponding section
        for (String mapName: section.getKeys(false)) {

            // Safe to assume an inner config section exists for this map.
            ConfigurationSection mapSection = section.getConfigurationSection(mapName);
            assert mapSection != null;

            // Now loop through all the entries of player IDs for this map.
            for (String playerID : mapSection.getKeys(false)) {

                // Retrieve the player section, we can safely assume it is not null since we found a key.
                ConfigurationSection playerSection = mapSection.getConfigurationSection(playerID);
                assert playerSection != null;
                records.add(new TimeEntry(UUID.fromString(playerID), playerSection));
            }

        }

        return records;
    }

    /**
     * Similarly to getAllTimesOnRecord(), but only returns entries that are for a specific map.
     *
     * @param map The map to filter by. Use map key (not name)
     * @return A List of TimeEntry instances where every entry has the same map.
     */
    public static List<TimeEntry> getAllTimesForMap(String map) {

        List<TimeEntry> records = new ArrayList<>();

        // Loop through every record on file. If the map matches what we want, add it.
        for (TimeEntry entry : getAllTimesOnRecord())
            if (entry.getMap().equalsIgnoreCase(map))
                records.add(entry);

        return records;
    }

}
