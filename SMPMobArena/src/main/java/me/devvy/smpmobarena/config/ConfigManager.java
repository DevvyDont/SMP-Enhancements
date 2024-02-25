package me.devvy.smpmobarena.config;

import me.devvy.smpmobarena.SMPMobArena;
import org.bukkit.Location;

public class ConfigManager {

    public final static String ARENA_ORIGIN = "arena-origin";

    public static Location getArenaLocation() {
        return SMPMobArena.getInstance().getConfig().getLocation(ARENA_ORIGIN, null);
    }

    public static void setArenaLocation(Location location) {
        SMPMobArena.getInstance().getConfig().set(ARENA_ORIGIN, location);
    }

}
