package me.devvy.smpparkour.map;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.checkpoints.Checkpoint;
import me.devvy.smpparkour.checkpoints.CheckpointRegionBox;
import me.devvy.smpparkour.checkpoints.CheckpointRegionSphere;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;

public class InfinityMap implements ParkourMap {

    private final Checkpoint[] checkpoints;
    private final Checkpoint finish;

    public InfinityMap() {
        World w = SMPParkour.getInstance().getParkourWorld();

        this.checkpoints = new Checkpoint[]{

                // FIRST JUMPS
                new Checkpoint(
                        new Location(w, -100f, 51f, -16.5f, -270f, 0f),
                        new CheckpointRegionSphere(new Location(w, -100f, 51f, -16.5f), 3),
                        0,
                        Component.text("First Jumps", NamedTextColor.GREEN)
                ),

                // START NEW GUY IN TOWN
                new Checkpoint(
                        new Location(w, -117.23, 55.00, -29.99, 75.99f, 3.32f),
                        new CheckpointRegionSphere(new Location(w, -117.23, 55.00, -29.99), 4),
                        1,
                        Component.text("New Guy in Town", NamedTextColor.GREEN)
                ),

                // INTO THE DEEP
                new Checkpoint(
                        new Location(w, -142.33f, 47.00f, 13.06f, -718.79f, 2.38f),
                        new CheckpointRegionBox(new Location(w, -139.58f, 52.12f, 11.18f), new Location(w, -144.37f, 46.50f, 16.29f)),
                        2,
                        Component.text("Into the Deep", NamedTextColor.GREEN)
                ),

                // PIPED
                new Checkpoint(
                        new Location(w, -159.48f, 44.00f, 27.85f, -658.27f, 2.17f),
                        new CheckpointRegionSphere(new Location(w, -160.92, 47.65, 30.67), 5.5),
                        3,
                        Component.text("Piped", NamedTextColor.YELLOW)
                ),


                // HOT N SPICY
                new Checkpoint(
                        new Location(w, -183.74f, 50.00f, 30.48f, 20.01f, 2.74f),
                        new CheckpointRegionBox(new Location(w, -181.29f, 55.45f, 32.85f), new Location(w, -184.87f, 49.83f, 28.50f)),
                        4,
                        Component.text("Hot and Spicy", NamedTextColor.YELLOW)
                ),

                // PREHISTORIC DISCOVERY
                new Checkpoint(
                        new Location(w, -203.52, 53.00, 16.45, 126.64f, 3.31f),
                        new CheckpointRegionBox(new Location(w, -202.12f, 56.16f, 19.76f), new Location(w, -206.12f, 51.65f, 12.99f)),
                        5,
                        Component.text("Prehistoric Discovery", NamedTextColor.YELLOW)
                ),

                // TIMELAPSE
                new Checkpoint(
                        new Location(w, -215.63f, 51.00f, -12.05f, 216.09f, 3.31f),
                        new CheckpointRegionBox(new Location(w, -217.53f, 55.02f, -10.01f), new Location(w, -212.29f, 50.16f, -11.31f)),
                        6,
                        Component.text("Timelapse", NamedTextColor.YELLOW)
                ),

                // OVERGROWN
                new Checkpoint(
                        new Location(w, -215.45, 57.00, -32.42, 269.72f, 0.91f),
                        new CheckpointRegionBox(new Location(w, -221.42f, 55.42f, -31.05f), new Location(w, -207.30f, 64.72f, -33.70f)),
                        7,
                        Component.text("Overgrown", NamedTextColor.GOLD)
                ),

                // GOLD RUSH
                new Checkpoint(
                        new Location(w, -209.47, 49.00, -40.37, 179.64f, 6.39f),
                        new CheckpointRegionBox(new Location(w, -212.80f, 55.10f, -37.28f), new Location(w, -207.10f, 47.98f, -42.62f)),
                        8,
                        Component.text("Gold Rush", NamedTextColor.GOLD)
                ),

                // CLOUD NINE
                new Checkpoint(
                        new Location(w, -198.32, 45.00, -54.11, 254.00f, 0.86f),
                        new CheckpointRegionSphere(new Location(w, -194.20f, 45.75f, -55.30f), 5),
                        9,
                        Component.text("Cloud Nine", NamedTextColor.GOLD)
                ),

                // NUCLEAR MELTDOWN
                new Checkpoint(
                        new Location(w, -154.37, 48.00, -57.90, 315.09f, 2.16f),
                        new CheckpointRegionBox(new Location(w, -159.18f, 52.74f, -62.66f), new Location(w, -155.53f, 46.76f, -59.08f)),
                        10,
                        Component.text("Nuclear Meltdown", NamedTextColor.GOLD)
                ),

                // HANGING BY A THREAD
                new Checkpoint(
                        new Location(w, -135.72, 36.00, -34.56, 271.02f, 1.70f),
                        new CheckpointRegionSphere(new Location(w, -132.98, 49.42, -36.12), 8),
                        11,
                        Component.text("Hanging by a Thread", NamedTextColor.RED)
                ),

                // WINTER WONDERLAND
                new Checkpoint(
                        new Location(w, -60.22, 57.00, 30.12, 281.67f, 1.38f),
                        new CheckpointRegionBox(new Location(w, -56.30f, 56.38f, 21.70f), new Location(w, -62.56f, 58.87f, 37.15f)),
                        12,
                        Component.text("Winter Wonderland", NamedTextColor.RED)
                ),

                // SILENCE
                new Checkpoint(
                        new Location(w, -23.70, 47.00, 31.72, 211.60f, 2.90f),
                        new CheckpointRegionBox(new Location(w, -24.70f, 45.51f, 25.62f), new Location(w, -21.01f, 53.74f, 35.83f)),
                        13,
                        Component.text("Silence", NamedTextColor.RED)
                ),

                // PRICKY SITUATION
                new Checkpoint(
                        new Location(w, -1.50, 55.00, 6.12, 269.50f, 2.22f),
                        new CheckpointRegionSphere(new Location(w, -1.52f, 57.08f, 8.98f), 6),
                        14,
                        Component.text("Pricky Situation", NamedTextColor.RED)
                ),

                // MONUMENT DRAIN
                new Checkpoint(
                        new Location(w, 5.55, 57.50, -18.82, 180.00f, 2.22f),
                        new CheckpointRegionBox(new Location(w, 6.30f, 57.05f, -16.95f), new Location(w, 3.64f, 60.41f, -20.00f)),
                        15,
                        Component.text("Monument Drain", NamedTextColor.DARK_RED)
                ),

                // EXOTIC ENVIRONMENTS
                new Checkpoint(
                        new Location(w, -5.94, 56.00, -41.56, 163.14f, 0.91f),
                        new CheckpointRegionSphere(new Location(w, -8.84, 56.00, -51.94), 9),
                        16,
                        Component.text("Exotic Environments", NamedTextColor.DARK_RED)
                ),

                // ALMOST THERE...
                new Checkpoint(
                        new Location(w, -60.66, 55.00, -50.59, 48.47f, 2.27f),
                        new CheckpointRegionSphere(new Location(w, -62.81f, 55.17f, -48.86f), 5),
                        17,
                        Component.text("Almost there...", NamedTextColor.DARK_RED)
                )

        };

        this.finish = new Checkpoint(
                new Location(w, -90.85f, 62.00f, -22.70f),
                new CheckpointRegionSphere(new Location(w,  -88.61, 62.00, -23.21), 3),
                18,
                Component.text("Finished!", NamedTextColor.LIGHT_PURPLE)
        );
    }

    @Override
    public Location getStart() {
        return getCheckpoints()[0].getSpawn();
    }

    @Override
    public Checkpoint[] getCheckpoints() {
        return checkpoints;
    }

    @Override
    public Checkpoint getFinish() {
        return finish;
    }
}
