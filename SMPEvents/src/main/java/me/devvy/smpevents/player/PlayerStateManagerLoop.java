package me.devvy.smpevents.player;

import org.bukkit.scheduler.BukkitRunnable;

public class PlayerStateManagerLoop extends BukkitRunnable {

    private PlayerStateManager psm;

    public PlayerStateManagerLoop(PlayerStateManager psm) {
        this.psm = psm;
    }

    @Override
    public void run() {

        // Feed all players that have it on
        for (EventPlayer ep : psm.getAllEventPlayers())
            if (ep.isKeepFed())
                ep.feed();


    }
}
