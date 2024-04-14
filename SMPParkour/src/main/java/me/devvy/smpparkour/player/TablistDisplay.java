package me.devvy.smpparkour.player;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.checkpoints.Checkpoint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scheduler.BukkitRunnable;

public class TablistDisplay extends BukkitRunnable {

    private final PlayerManager pm;

    public TablistDisplay(PlayerManager pm) {
        this.pm = pm;
    }

    @Override
    public void run() {

        for (ParkourPlayer pp : pm.getAllParkourPlayers()) {

            String practiceTag = pp.isPracticing() ? "P" : "";

            Component timeComponent = Component.text(pp.getTimer().getTimeFormattedString() + " ", pp.getTimer().getTimerColor());
            Checkpoint checkpoint = pp.getCurrentCheckpoint() != null ? pp.getCurrentCheckpoint() : SMPParkour.getInstance().getMapManager().getMap().getCheckpoints()[0];
            Component cpComponent = Component.text("[" + practiceTag + (checkpoint.getIndex()+1) + "] ", SMPParkour.getInstance().getMapManager().getMap().getPercentageColor(checkpoint.getIndex()));
            Component name = Component.text(pp.getPlayer().getName(), NamedTextColor.GRAY);

            pp.getPlayer().playerListName(
                    cpComponent.append(timeComponent).append(name)
            );

        }


    }
}
