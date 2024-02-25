package me.devvy.smpparkour.util;

import me.devvy.smpparkour.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static me.devvy.smpparkour.player.ParkourTimer.PLAYER_TIMER_DECIMAL_FORMAT;

/**
 * A couple of armor stands with nametags stacked on each other to make a scoreboard
 */
public class HolographicLeaderboard {

    private final float Y_PADDING = .3F;

    private List<ArmorStand> entities = new ArrayList<>();
    private final Location location;

    public HolographicLeaderboard(Location location) {
        this.location = location;
    }

    private ArmorStand spawnArmorStand(Component name, int index) {
        float yOffset = index * Y_PADDING;

        ArmorStand as = location.getWorld().spawn(location.clone().add(0, 200, 0), ArmorStand.class);
        as.setVisible(false);
        as.setMarker(true);
        as.customName(name);
        as.setCustomNameVisible(true);
        as.teleport(location.clone().subtract(0, yOffset, 0));
        return as;
    }

    public void update() {

        List<ConfigManager.TimeEntry> entries = new ArrayList<>(ConfigManager.getAllTimesOnRecord().values());
        entries.sort(Comparator.comparing(ConfigManager.TimeEntry::getTimeMs));

        List<ArmorStand> newArmorStands = new ArrayList<>();

        // Spawn an armor stand that displays scoreboard header
        // Space codes will be as follows: #:3 name:20 time:16
        newArmorStands.add(spawnArmorStand(Component.text(String.format("%-8s%-24s%-5s", "#", "Player", "Time"), NamedTextColor.GRAY, TextDecoration.BOLD), -1));

        // Now we have a sorted list of player stats, construct the new holograms but only for the top 10 players
        for (int i = 0; i < entries.size(); i++) {

            // When we get to the top 10, stop
            if (i >= 10)
                break;

            ConfigManager.TimeEntry entry = entries.get(i);

            double rawTime = entry.getTimeMs() / 1000d;
            String timeText = String.format("%d:%s", (int) rawTime / 60, PLAYER_TIMER_DECIMAL_FORMAT.format(rawTime % 60));

            // Construct the armor stand name
            Component rank = Component.text(String.format("%-4s", i+1+":"), NamedTextColor.GRAY);
            Component name = Component.text(String.format("%-20s", entry.getName()), NamedTextColor.AQUA);
            Component time = Component.text(String.format("%12s", timeText), NamedTextColor.GOLD, TextDecoration.BOLD);

            Component armorStandName = rank.append(name).append(time);
            newArmorStands.add(spawnArmorStand(armorStandName, i));
        }

        // Delete old ones
        delete();

        entities = newArmorStands;
    }

    public void delete() {
        for (ArmorStand as : entities)
            as.remove();

        entities.clear();
    }
}
