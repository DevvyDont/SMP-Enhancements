package xyz.devvydont.smprpg.config;

import org.bukkit.configuration.file.FileConfiguration;
import xyz.devvydont.smprpg.SMPRPG;

public class ConfigManager {

    public static final String OPTION_MAX_LEVEL = "max-skill-level";

    public static void init() {

        SMPRPG plugin = SMPRPG.getInstance();
        FileConfiguration config = plugin.getConfig();

        config.addDefault(OPTION_MAX_LEVEL, 30);

        config.options().copyDefaults();
        plugin.saveDefaultConfig();
    }

}
