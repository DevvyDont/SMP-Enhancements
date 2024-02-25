package me.devvy.smpbuildworld.listeners;

import me.devvy.smpbuildworld.SMPBuildWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionGriefProtection implements Listener {

    public ExplosionGriefProtection() {
        SMPBuildWorld.getInstance().getServer().getPluginManager().registerEvents(this, SMPBuildWorld.getInstance());
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent event) {

        if (!SMPBuildWorld.getInstance().isBuildWorldEnabled())
            return;

        // If this explosion happened in build world cancel it
        if (event.getBlock().getWorld().equals(SMPBuildWorld.getInstance().getBuildWorld())) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event) {

        if (!SMPBuildWorld.getInstance().isBuildWorldEnabled())
            return;

        // If this explosion happened in build world cancel it
        if (event.getEntity().getWorld().equals(SMPBuildWorld.getInstance().getBuildWorld())) {
            event.setCancelled(true);
        }

    }

}
