package me.devvy.customitems.listeners;

import me.devvy.customitems.CustomItems;
import me.devvy.customitems.blueprints.CustomItemBlueprint;
import me.devvy.customitems.events.RareItemDropEvent;
import me.devvy.customitems.util.RNGRoller;
import org.bukkit.attribute.Attribute;
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
        CustomItems.getInstance().getServer().getPluginManager().registerEvents(this, CustomItems.getInstance());
    }

    public static float getBaseDropChance(EntityType entityType) {

        switch (entityType) {

            case WARDEN:
            case ENDER_DRAGON:
            case WITHER:
                return 1/10f;

            case ELDER_GUARDIAN:
                return 1/25f;

            case ENDERMAN:
                return 1/25000f;

            case ZOMBIFIED_PIGLIN:
                return 1/9000f;

            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case CREEPER:
            case STRAY:
            case HUSK:
            case DROWNED:
                return 1/6000f;

            case CAVE_SPIDER:
            case WITCH:
            case BLAZE:
            case WITHER_SKELETON:
            case MAGMA_CUBE:
            case SLIME:
            case HOGLIN:
            case ZOGLIN:
            case GUARDIAN:
            case PIGLIN_BRUTE:
            case ZOMBIE_VILLAGER:
                return 1/4250f;

            case SHULKER:
            case SILVERFISH:
                return 1/3750f;

            case GHAST:
            case PHANTOM:
            case PILLAGER:
            case VINDICATOR:
                return 1/2000f;

            case ENDERMITE:
            case VEX:
                return 1/1250f;

            case EVOKER:
            case RAVAGER:
                return 1/750f;

        }

        return 0f;

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

        RNGRoller rngRoller = new RNGRoller(killer, baseChance, Enchantment.LOOT_BONUS_MOBS);

        if (!rngRoller.roll())
            return;

        // They got a rare item
        CustomItemBlueprint blueprint = CustomItems.getInstance().getCustomItemManager().getRandomBlueprint();
        ItemStack item = blueprint.get();
        Item entityItem = event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);

        RareItemDropEvent rareItemDropEvent = new RareItemDropEvent(killer, entityItem, rngRoller.getChance(), "a(n) " + event.getEntity().getName());
        CustomItems.getInstance().getServer().getPluginManager().callEvent(rareItemDropEvent);

    }
}
