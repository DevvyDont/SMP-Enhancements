package me.devvy.smpparkour.util;

import me.devvy.smpparkour.SMPParkour;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Announcer implements Listener {

    public static final Component PREFIX =
            Component.text("[", TextColor.color(120, 120, 120))
                    .append(Component.text("!", TextColor.color(255, 255, 0)))
                    .append(Component.text("] ", TextColor.color(120, 120, 120)));

    public static final TextColor GRAY = TextColor.color(180, 180, 180);
    public static final TextColor DARK_GRAY = TextColor.color(100, 100, 100);
    public static final TextColor GOLD = TextColor.color(255, 215, 0);
    public static final TextColor GREEN = TextColor.color(0, 200, 0);
    public static final TextColor RED = TextColor.color(200, 50, 0);

    private boolean enabled;

    public Announcer() {

        if (!SMPParkour.getInstance().getConfig().getBoolean("announce", false))
            return;

        enable();
    }

    public void enable() {

        if (enabled)
            return;

        SMPParkour.getInstance().getServer().getPluginManager().registerEvents(this, SMPParkour.getInstance());
        enabled = true;
    }

    public void disable() {

        if (!enabled)
            return;

        HandlerList.unregisterAll(this);
        enabled = false;
    }

    public static void sendAnnouncementMessage (Player player) {

        new BukkitRunnable(){

            int tick = 0;

            final Component[] msgs = {
                    PREFIX.append(Component.text("There is currently a ", GRAY)).append(Component.text("Parkour Race ", GOLD, TextDecoration.BOLD)).append(Component.text("in progress!", GRAY)),
                    PREFIX.append(Component.text("Achieve the fastest time by ", GRAY)).append(Component.text("Saturday 11:59pm EST!", RED, TextDecoration.BOLD)),
                    PREFIX.append(Component.text("Top 3 players will be rewarded with ", GRAY)).append(Component.text("unobtainable items ", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD)).append(Component.text(" :)", GRAY)),
                    PREFIX.append(Component.text("You can access the parkour via the ", GRAY)).append(Component.text("Event Area", GOLD, TextDecoration.BOLD).clickEvent(ClickEvent.runCommand("/event")).hoverEvent(Component.text("Click to warp!", NamedTextColor.GREEN))).append(Component.text("!", GRAY)),
            };

            @Override
            public void run() {

                if (tick >= msgs.length || !player.isOnline()) {
                    cancel();
                    return;
                }

                Component msg = msgs[tick];
                player.sendMessage(msg);
//                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
                tick++;
            }
        }.runTaskTimer(SMPParkour.getInstance(), 20*5, 20*3);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (!enabled)
            return;

        sendAnnouncementMessage(event.getPlayer());

    }
}
