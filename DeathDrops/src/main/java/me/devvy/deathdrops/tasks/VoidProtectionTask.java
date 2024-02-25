package me.devvy.deathdrops.tasks;

import me.devvy.deathdrops.items.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidProtectionTask extends BukkitRunnable {

    public static void checkAndFloatAboveVoid(Item item) {

        if (!ItemUtil.isDeadDrop(item.getItemStack()))
            return;

        // If this item spawned in the void in the end, lets make them float at y=1
        if (item.getWorld().getEnvironment().equals(World.Environment.THE_END)) {

            if (item.getLocation().getY() <= 0) {

                // Turn off gravity, teleport it to y=1, give it no y velocity
                item.setGravity(false);
                Location spawnLoc = item.getLocation().clone();
                spawnLoc.setY(1);
                item.teleport(spawnLoc);
                item.setVelocity(item.getVelocity().setY(0));
            }

        }

    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds())
            if (world.getEnvironment().equals(World.Environment.THE_END))
                for (Item item : world.getEntitiesByClass(Item.class))
                    if (item.hasGravity())
                        checkAndFloatAboveVoid(item);
    }

}
