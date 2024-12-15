package xyz.devvydont.treasureitems.listeners;

import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.events.RareItemDropEvent;
import xyz.devvydont.treasureitems.util.RNGRoller;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MobKillListeners implements Listener {

    public MobKillListeners() {
        xyz.devvydont.treasureitems.TreasureItems.getInstance().getServer().getPluginManager().registerEvents(this, xyz.devvydont.treasureitems.TreasureItems.getInstance());
    }

    public static float getBaseDropChance(EntityType entityType) {

        return switch (entityType) {
            case WARDEN, ENDER_DRAGON, WITHER -> 1 / 10f;
            case ELDER_GUARDIAN -> 1 / 25f;
            case ENDERMAN -> 1 / 25000f;
            case ZOMBIFIED_PIGLIN -> 1 / 9000f;
            case ZOMBIE, SKELETON, SPIDER, CREEPER, STRAY, HUSK, DROWNED -> 1 / 6000f;
            case CAVE_SPIDER, WITCH, BLAZE, WITHER_SKELETON, MAGMA_CUBE, SLIME, HOGLIN, ZOGLIN, GUARDIAN, PIGLIN_BRUTE,
                 ZOMBIE_VILLAGER -> 1 / 4250f;
            case SHULKER, SILVERFISH -> 1 / 3750f;
            case GHAST, PHANTOM, PILLAGER, VINDICATOR -> 1 / 2000f;
            case ENDERMITE, VEX -> 1 / 1250f;
            case EVOKER, RAVAGER -> 1 / 750f;
            default -> 0f;
        };

    }

    @EventHandler
    public void onMobDeathByPlayer(EntityDeathEvent event) {

        // Was there a killer?
        Player killer = event.getEntity().getKiller();
        if (killer == null)
            return;

        // Should this be an entity that drops a rare item?
        float baseChance = getBaseDropChance(event.getEntityType());

        if (baseChance <= 0)
            return;

        RNGRoller rngRoller = new RNGRoller(killer, baseChance, Enchantment.LOOTING);

        if (!rngRoller.roll())
            return;

        // They got a rare item
        CustomItemBlueprint blueprint = xyz.devvydont.treasureitems.TreasureItems.getInstance().getCustomItemManager().getRandomBlueprint();
        ItemStack item = blueprint.get();
        Item entityItem = event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);

        RareItemDropEvent rareItemDropEvent = new RareItemDropEvent(killer, entityItem, rngRoller.getChance(), "a(n) " + event.getEntity().getName());
        xyz.devvydont.treasureitems.TreasureItems.getInstance().getServer().getPluginManager().callEvent(rareItemDropEvent);

    }
}
