package me.devvy.smpbuildworld.region;

import me.devvy.smpbuildworld.SMPBuildWorld;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class BuildRegion {

    /**
     * Initialize and return an instance of this class from persistent yml data
     *
     * @param section Config section w/ corresponding path: root.regions.<some player id>
     * @return An instance of BuildRegion or null if section was invalid
     */
    public static BuildRegion fromConfigSection(ConfigurationSection section) {

        Location c1 = section.getLocation("corner1");
        Location c2 = section.getLocation("corner2");

        // Usually means that this person didn't have a region yet, we handle new region configuring somewhere else
        if (c1 == null || c2 == null)
            return null;

        return new BuildRegion(c1, c2);
    }

    
    // Create a bounding box using 2 corners, Y level will not matter in this scenario
    private Location corner1;
    private Location corner2;

    public BuildRegion(Location corner1, Location corner2) {
        
        // If we somehow get two separate worlds we messed up
        if (!corner1.getWorld().equals(corner2.getWorld()))
            throw new IllegalArgumentException("Both corners must exist in the same world");
        
        this.corner1 = corner1;
        this.corner2 = corner2;
    }
    
    private int minX() {
        return Math.min(corner1.getBlockX(), corner2.getBlockX());
    }

    private int minZ() {
        return Math.min(corner1.getBlockZ(), corner2.getBlockZ());
    }

    private int maxX() {
        return Math.max(corner1.getBlockX(), corner2.getBlockX());
    }

    private int maxZ() {
        return Math.max(corner1.getBlockZ(), corner2.getBlockZ());
    }
    
    public boolean inBounds(Location location) {
        
        if (!location.getWorld().equals(corner1.getWorld()))
            return false;
        
        // In bounds on x axis and y axis?
        int blockX = location.getBlockX();
        int blockZ = location.getBlockZ();
        boolean withinX = false;
        boolean withinZ = false;
        
        if (blockX >= minX() && blockX <= maxX())
            withinX = true;
        
        if (blockZ >= minZ() && blockZ <= maxZ())
            withinZ = true;
        
        return withinX && withinZ;
    }

    /**
     * Given a player and a config section, save this region to the config
     *
     * @param section Section w/ corresponding path: root.regions
     * @param player Player that will own this region
     */
    public void saveToConfig(ConfigurationSection section, Player player) {

        // Make sure a section for this player exists
        ConfigurationSection thisPlayersSection;

        if (section.contains(player.getUniqueId().toString()))
            thisPlayersSection = section.getConfigurationSection(player.getUniqueId().toString());
        else
            thisPlayersSection = section.createSection(player.getUniqueId().toString());

        if (thisPlayersSection == null)
            throw new IllegalStateException(String.format("Somehow %s does not have a section in the config", player.getName()));

        thisPlayersSection.set("corner1", corner1);
        thisPlayersSection.set("corner2", corner2);
        SMPBuildWorld.getInstance().saveConfig();
    }
}
