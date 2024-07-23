package xyz.devvydont.smprpg.util.items;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.ItemRarity;

public class DropFireworkTask extends BukkitRunnable {

    public static void start(Item item) {
        new DropFireworkTask(item).runTaskTimer(SMPRPG.getInstance(), (long)(Math.random()*20), 40L);
    }

    private final Item item;
    int fireworkAmmo = 50;

    public DropFireworkTask(Item item) {
        this.item = item;
    }

    private Firework createFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class, CreatureSpawnEvent.SpawnReason.CUSTOM);
        FireworkMeta meta = firework.getFireworkMeta();
        ItemStack itemStack = item.getItemStack();
        ItemRarity rarity = SMPRPG.getInstance().getItemService().getBlueprint(itemStack).getRarity(itemStack);
        meta.addEffect(
                FireworkEffect.builder()
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.fromRGB(rarity.color.value()))
                        .build()
        );
        firework.setFireworkMeta(meta);
        return firework;
    }

    @Override
    public void run() {

        if (!item.isValid() || fireworkAmmo < 0) {
            cancel();
            return;
        }

        createFirework(item.getLocation()).detonate();
        fireworkAmmo--;
    }
}
