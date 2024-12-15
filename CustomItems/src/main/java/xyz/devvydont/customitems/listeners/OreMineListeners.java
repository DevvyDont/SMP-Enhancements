package me.devvy.customitems.listeners;

import me.devvy.customitems.CustomItems;
import me.devvy.customitems.blueprints.CustomItemBlueprint;
import me.devvy.customitems.events.RareItemDropEvent;
import me.devvy.customitems.util.RNGRoller;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OreMineListeners implements Listener {

    public OreMineListeners() {
        CustomItems.getInstance().getServer().getPluginManager().registerEvents(this, CustomItems.getInstance());
    }
    
    public static float getBaseDropChance(Material material) {
        
        switch (material) {
            case COAL_ORE:
            case COPPER_ORE:
                return 1/3000f;
            case IRON_ORE:
                return 1/2700f;
            case REDSTONE_ORE:
            case LAPIS_ORE:
                return 1/2500f;
            case GOLD_ORE:
                return 1/2150f;
            case DIAMOND_ORE:
                return 1/1400f;
            case EMERALD_ORE:
                return 1/250f;
                
            case NETHER_GOLD_ORE:
            case NETHER_QUARTZ_ORE:
                return 1/2100f;
                
            case DEEPSLATE_COAL_ORE:
            case DEEPSLATE_COPPER_ORE:
                return 1/2750f;
            case DEEPSLATE_IRON_ORE:
                return 1/2400f;
            case DEEPSLATE_REDSTONE_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return 1/2050f;
            case DEEPSLATE_GOLD_ORE:
                return 1/1650f;
            case DEEPSLATE_DIAMOND_ORE:
                return 1/900f;
            case DEEPSLATE_EMERALD_ORE:
                return 1/180f;

                
        }
        
        return 0f;
        
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        
        float baseChance = getBaseDropChance(event.getBlock().getType());
        if (baseChance <= 0)
            return;

        // Don't let silk touch trigger this
        if (event.getPlayer().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0)
            return;

        RNGRoller rngRoller = new RNGRoller(event.getPlayer(), baseChance, Enchantment.LOOT_BONUS_BLOCKS);

        if (!rngRoller.roll())
            return;

        // They got a rare item
        CustomItemBlueprint blueprint = CustomItems.getInstance().getCustomItemManager().getRandomBlueprint();
        ItemStack item = blueprint.get();
        Item entityItem = event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);

        RareItemDropEvent rareItemDropEvent = new RareItemDropEvent(event.getPlayer(), entityItem, rngRoller.getChance(), "mining");
        CustomItems.getInstance().getServer().getPluginManager().callEvent(rareItemDropEvent);
        
    }
}
