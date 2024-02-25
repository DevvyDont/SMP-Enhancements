package me.devvy.smpparkour.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

    public static void spawnFireworksInstantly(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location.add(0, .5, 0), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder effectBuilder = FireworkEffect.builder();
        effectBuilder.with(FireworkEffect.Type.BALL_LARGE);
        effectBuilder.withColor(color);
        meta.addEffect(effectBuilder.build());
        firework.setFireworkMeta(meta);
        firework.detonate();
    }

}
