package me.devvy.smpparkour.util;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.checkpoints.Checkpoint;
import me.devvy.smpparkour.checkpoints.CheckpointRegion;
import me.devvy.smpparkour.checkpoints.CheckpointRegionBox;
import me.devvy.smpparkour.checkpoints.CheckpointRegionSphere;
import me.devvy.smpparkour.map.ParkourMapBase;
import me.devvy.smpparkour.map.ParkourMapBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


/**
 * Stores gross methods to simplify loading/saving yaml files regarding worlds
 */
public class MapYamlUtil {

    // General fields for a map
    public static final String FIELD_MAP_NAME = "name";  // Map name
    public static final String FIELD_MAP_PATH = "path";  // World folder name
    public static final String FIELD_MAP_LEADERBOARD = "leaderboard";  // Leaderboard location
    public static final String FIELD_MAP_CHECKPOINTS = "checkpoints";  // Checkpoint field

    // Fields defined per checkpoint
    public static final String FIELD_CHECKPOINTS_NAME = "name";  // Checkpoint name
    public static final String FIELD_CHECKPOINTS_SPAWN = "spawnpoint";  // Checkpoint spawn point
    public static final String FIELD_CHECKPOINTS_TYPE = "type";  // Checkpoint region type, either sphere or box

    /*
    Depending on the type, we can have a few options.
    for box, we have corner1 and corner2
    for sphere, we have origin and radius
     */

    public static final String FIELD_CHECKPOINTS_CORNERONE = "options.corner1";
    public static final String FIELD_CHECKPOINTS_CORNERTWO = "options.corner2";
    public static final String FIELD_CHECKPOINTS_ORIGIN = "options.origin";
    public static final String FIELD_CHECKPOINTS_RADIUS = "options.radius";

    /**
     * Get a map instance solely from options defined by a yaml file.
     * Throws FileNotFoundException if a file does not exist, resulting in a map being invalid.
     *
     * @param yamlPath File instance representing the path of the YAML file for a map
     * @return a fully instantiated and valid parkour map instance
     */
    public static ParkourMapBase loadFromYaml(File yamlPath) throws NullPointerException {

        // First make sure the file exists
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(yamlPath);
        } catch (IOException ignored) {
            SMPParkour.getInstance().getLogger().severe("Unable to load file " + yamlPath.getAbsolutePath() + "! Does it exist?");
            return null;
        } catch (InvalidConfigurationException e) {
            SMPParkour.getInstance().getLogger().severe("Failed to parse yml file" + yamlPath.getAbsolutePath() + "! Please check syntax and try again. Here's a traceback detailing the error.");
            e.printStackTrace();
            return null;
        }

        ParkourMapBuilder mapBuilder = new ParkourMapBuilder();

        mapBuilder.setName(yamlConfiguration.getString(FIELD_MAP_NAME));
        mapBuilder.setPath(yamlConfiguration.getString(FIELD_MAP_PATH));
        mapBuilder.setLeaderboardLocation(yamlConfiguration.getLocation(FIELD_MAP_LEADERBOARD));

        // Assume that checkpoints are defined in order, but if we wanted to sanity check more we can insert based on key instead
        for (String checkpointIndex : yamlConfiguration.getConfigurationSection(FIELD_MAP_CHECKPOINTS).getKeys(false)) {

            ConfigurationSection checkpointSection = yamlConfiguration.getConfigurationSection(FIELD_MAP_CHECKPOINTS).getConfigurationSection(checkpointIndex);

            String checkpointName = checkpointSection.getString(FIELD_CHECKPOINTS_NAME);
            String checkpointType = checkpointSection.getString(FIELD_CHECKPOINTS_TYPE);
            Location checkpointSpawn = checkpointSection.getLocation(FIELD_CHECKPOINTS_SPAWN);

            CheckpointRegion region;

            if (checkpointType.equalsIgnoreCase("sphere")) {

                Location origin = checkpointSection.getLocation(FIELD_CHECKPOINTS_ORIGIN);
                int radius = checkpointSection.getInt(FIELD_CHECKPOINTS_RADIUS);
                region = new CheckpointRegionSphere(origin, radius);

            } else if (checkpointType.equalsIgnoreCase("box")) {

                Location corner1 = checkpointSection.getLocation(FIELD_CHECKPOINTS_CORNERONE);
                Location corner2 = checkpointSection.getLocation(FIELD_CHECKPOINTS_CORNERTWO);
                region = new CheckpointRegionBox(corner1, corner2);

            } else {

                throw new IllegalStateException("Unknown checkpoint type " + checkpointType);

            }

            mapBuilder.addCheckpoint(new Checkpoint(
                    checkpointSpawn,
                    region,
                    Integer.parseInt(checkpointIndex),
                    checkpointName
            ));
        }

        return mapBuilder.build();

    }

    /**
     * Given a parkour map instance that is valid, save a yaml file representing its information stored in memory
     * in its world folder
     *
     * @param map Parkour map that is valid and loaded in memory
     */
    public static void saveToYaml(ParkourMapBase map) {

        File yamlFile = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/" + map.getMapPath(), "parkour.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        // Set general fields for the map
        yamlConfiguration.set(FIELD_MAP_NAME, map.getMapName());
        yamlConfiguration.set(FIELD_MAP_PATH, map.getMapPath());
        yamlConfiguration.set(FIELD_MAP_LEADERBOARD, map.getLeaderboardLocation());

        ConfigurationSection checkpointsSection = yamlConfiguration.createSection(FIELD_MAP_CHECKPOINTS);

        // Set a field for every checkpoint in order
        for (int checkpointIndex = 0; checkpointIndex < map.getNumCheckpoints(); checkpointIndex++) {

            Checkpoint checkpoint = map.getCheckpoints()[checkpointIndex];

            // Create a section for this specific checkpoint, just use index as key
            ConfigurationSection thisCheckpointSection = checkpointsSection.createSection(String.valueOf(checkpointIndex));

            thisCheckpointSection.set(FIELD_CHECKPOINTS_NAME, checkpoint.getName());
            thisCheckpointSection.set(FIELD_CHECKPOINTS_TYPE, getCheckpointTypeAsString(checkpoint.getRegion()));
            thisCheckpointSection.set(FIELD_CHECKPOINTS_SPAWN, checkpoint.getSpawn());

            // Now depending on the type, save specific options, this can probably be cleaned up
            if (checkpoint.getRegion() instanceof CheckpointRegionSphere) {
                CheckpointRegionSphere regionSphere = (CheckpointRegionSphere) checkpoint.getRegion();
                thisCheckpointSection.set(FIELD_CHECKPOINTS_RADIUS, regionSphere.getRadius());
                thisCheckpointSection.set(FIELD_CHECKPOINTS_ORIGIN, regionSphere.getOrigin());
            } else if (checkpoint.getRegion() instanceof CheckpointRegionBox) {
                CheckpointRegionBox regionBox = (CheckpointRegionBox) checkpoint.getRegion();
                thisCheckpointSection.set(FIELD_CHECKPOINTS_CORNERONE, regionBox.getCorner1());
                thisCheckpointSection.set(FIELD_CHECKPOINTS_CORNERTWO, regionBox.getCorner2());
            } else {
                throw new IllegalStateException("Unknown checkpoint region for checkpoint class " + checkpoint.getRegion().getClass().getName());
            }

        }

        // Now attempt to save the file
        try {
            yamlConfiguration.save(yamlFile);
        } catch (IOException ignored) {
            SMPParkour.getInstance().getLogger().severe("Failed to save world config file for parkour maps/checkpoints for " + map.getMapName());
        }

    }

    /**
     * Checks a checkpoint region instance and gives a string representation to save in a yaml file
     *
     * @param region CheckpointRegion instance to save
     * @return A string that is safe to save in a yaml file
     */
    public static String getCheckpointTypeAsString(CheckpointRegion region) {

        if (region instanceof CheckpointRegionSphere)
            return "sphere";

        if (region instanceof CheckpointRegionBox)
            return "box";

        throw new IllegalStateException("Unknown YAML region type for class " + region.getClass().getName() + "!");
    }


}
