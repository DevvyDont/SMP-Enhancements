package me.devvy.stimmys.util;

import me.devvy.stimmys.Stimmys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class ConfigManager {

    // Path in the config to where we store unredeemed stimmy points
    private static final String UNREDEEMED_STIMMY_PATH = "unredeemed-stimmy-points";

    private static ConfigurationSection getPlayerDataSection() {
        ConfigurationSection section = Stimmys.getInstance().getConfig().getConfigurationSection(UNREDEEMED_STIMMY_PATH);

        // If it wasn't created yet, create a section
        if (section == null)
            section = Stimmys.getInstance().getConfig().createSection(UNREDEEMED_STIMMY_PATH);

        return section;
    }

    /**
     * Gets how many unredeemed stimmy points are on file for a player
     *
     * @param offlinePlayer
     * @return
     */
    public static int getRedeemableStimmyPoints(OfflinePlayer offlinePlayer) {
        // Get how many points they had, if nothing on file then assume 0 points
        return getPlayerDataSection().getInt(offlinePlayer.getUniqueId().toString(), 0);
    }

    /**
     * Overrides the value stored for stimmy points for a player
     *
     * @param offlinePlayer
     * @param points
     * @return
     */
    public static void setRedeemableStimmyPoints(OfflinePlayer offlinePlayer, int points) {
        getPlayerDataSection().set(offlinePlayer.getUniqueId().toString(), points);
        Stimmys.getInstance().saveConfig();
    }

    /**
     * Adds redeemable stimmy points for a certain player to the config
     * Once they run the command to redeem they will be given the points
     *
     * @param offlinePlayer
     * @param points Number of points to give them
     */
    public static void addRedeemableStimmyPoints(OfflinePlayer offlinePlayer, int points) {

        // Get old points and add points to it for a total
        int oldPoints = getRedeemableStimmyPoints(offlinePlayer);
        getPlayerDataSection().set(offlinePlayer.getUniqueId().toString(), oldPoints + points);
        Stimmys.getInstance().saveConfig();
    }

    public static void dumpStimmyConfig(CommandSender sender) {

        ConfigurationSection section = getPlayerDataSection();

        Map<String, Object> kvps = section.getValues(false);
        for (String uuid: kvps.keySet()) {
            Object o = kvps.get(uuid);
            int amount = (int) o;
            String name = Bukkit.getOfflinePlayer(uuid).getName();

            sender.sendMessage(Component.text(String.format("%16s - %s", name, amount), NamedTextColor.GRAY));
        }

    }

}
