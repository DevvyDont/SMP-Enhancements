package xyz.devvydont.smprpg.commands.admin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.commands.PlayerCommandBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.time.Duration;

/**
 * Simulates fishing casts for debugging purposes.
 */
public class CommandSimulateFishing extends PlayerCommandBase {

    public CommandSimulateFishing(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {

        if (args.length <= 0) {
            player.sendMessage(ComponentUtils.error("Please provide how many simulated casts you want to perform!"));
            return;
        }

        int casts;
        try {
            casts = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ComponentUtils.error("Please provide a valid number!"));
            return;
        }
        final FishHook hook = player.getWorld().getNearbyEntitiesByType(FishHook.class, player.getLocation(), 64).stream().findFirst().orElse(null);
        if (hook == null) {
            player.sendMessage(ComponentUtils.error("Could not find a fish hook to piggy back off of! Prepare a cast by using a fishing rod!"));
            return;
        }
        new BukkitRunnable() {
            private int tick = 0;
            private final Player target = player;

            @Override
            public void run() {

                if (!hook.isValid()) {
                    cancel();
                    return;
                }

                // Hack together a fish event.
                new PlayerFishEvent(target, null, hook, EquipmentSlot.HAND, PlayerFishEvent.State.CAUGHT_FISH).callEvent();
                target.showTitle(Title.title(Component.empty(), ComponentUtils.create("SIMULATING " + (tick+1) + "/" +  casts, NamedTextColor.RED), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));

                tick++;
                if (tick >= casts)
                    cancel();
            }
        }.runTaskTimer(SMPRPG.getInstance(), TickTime.INSTANTANEOUSLY, TickTime.TICK);
    }

    @Override
    public @Nullable String permission() {
        return "smprpg.command.simulatefishing";
    }
}
