package xyz.devvydont.smprpg.util.formatting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;

public class DamagePopupUtil {

    public enum PopupType {
        DAMAGE_ARMOR(NamedTextColor.GOLD),
        DAMAGE(NamedTextColor.RED),
        GAIN_ARMOR(NamedTextColor.YELLOW),
        HEAL(NamedTextColor.GREEN);

        final TextColor color;
        PopupType(TextColor color) {
            this.color = color;
        }
    }

    public static void spawnTextPopup(Location location, int amount, PopupType type) {
        if (amount == 0)
            return;
        int finalAmount = Math.max(1, amount);
        TextDisplay display = location.getWorld().spawn(location.add(Math.random()-.5, Math.random()+.3, Math.random()-.5), TextDisplay.class, e -> {
            e.setPersistent(false);
            e.text(Component.text(finalAmount).color(type.color));
            e.setBillboard(Display.Billboard.CENTER);
            e.setShadowed(true);
            e.setSeeThrough(false);
            e.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                display.remove();
            }
        }.runTaskLater(SMPRPG.getInstance(), 30L);
    }

}
