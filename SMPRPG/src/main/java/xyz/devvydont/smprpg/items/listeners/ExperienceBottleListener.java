package xyz.devvydont.smprpg.items.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ExperienceThrowable;

/*
 * Class responsible for making custom experience bottles function.
 */
public class ExperienceBottleListener implements Listener {

    private final NamespacedKey EXPERIENCE_KEY = new NamespacedKey(SMPRPG.getInstance(), "stored-experience");

    /*
     * When we interact using the bottle, go ahead and throw it.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onThrowCustomExperienceBottle(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (item == null || item.getType().equals(Material.AIR))
            return;

        // Ignore left clicks
        if (event.getAction().isLeftClick())
            return;

        // Is the item involved an experience bottle?
        SMPItemBlueprint blueprint = SMPRPG.getInstance().getItemService().getBlueprint(item);
        if (!(blueprint instanceof ExperienceThrowable experienceThrowable))
            return;

        // We are throwing an exp bottle, cancel the event and throw it.
        event.setCancelled(true);
        item.setAmount(item.getAmount()-1);
        Entity bottleEntity = event.getPlayer().launchProjectile(ThrownExpBottle.class, event.getPlayer().getLocation().getDirection().normalize().multiply(.9));
        bottleEntity.getPersistentDataContainer().set(EXPERIENCE_KEY, PersistentDataType.INTEGER, experienceThrowable.getExperience());
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1, .5f);
    }

    /*
     * Listen for when exp bottles break. If the bottle involved is a bottle that we have an experience override on,
     * we should change the exp it drops.
     */
    @EventHandler
    public void onExperienceBottleBreak(ExpBottleEvent event) {

        if (!event.getEntity().getPersistentDataContainer().has(EXPERIENCE_KEY, PersistentDataType.INTEGER))
            return;

        // This is one of our custom bottles. Override the experience.
        int experience = event.getEntity().getPersistentDataContainer().getOrDefault(EXPERIENCE_KEY, PersistentDataType.INTEGER, 1);
        event.setExperience(experience);
    }

}
