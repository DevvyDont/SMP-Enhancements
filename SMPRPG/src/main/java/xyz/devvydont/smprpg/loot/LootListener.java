package xyz.devvydont.smprpg.loot;

import org.bukkit.block.Chest;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.calculator.EnchantmentCalculator;
import xyz.devvydont.smprpg.items.CustomItemType;

import java.util.*;

import static xyz.devvydont.smprpg.listeners.StructureEntitySpawnListener.getMinimumEntityLevel;

/**
 * Class responsible for hooking into chest loot generation events and populating them with overrides if desired
 */
public class LootListener implements Listener {

    private final SMPRPG plugin;

    // Our plugin injects custom loot tables into existing ones for extra items
    private final Map<LootTables, CustomLootTable> lootTableAdditions = new HashMap<>();

    public LootListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        lootTableAdditions.put(LootTables.ABANDONED_MINESHAFT, new CustomLootTable(
                new LootTableMember(plugin.getItemService().getCustomItem(CustomItemType.COPPER_HELMET)).withChance(.1f).withEnchants(true, 20),
                new LootTableMember(plugin.getItemService().getCustomItem(CustomItemType.COPPER_CHESTPLATE)).withChance(.1f).withEnchants(true, 20),
                new LootTableMember(plugin.getItemService().getCustomItem(CustomItemType.COPPER_LEGGINGS)).withChance(.1f).withEnchants(true, 20),
                new LootTableMember(plugin.getItemService().getCustomItem(CustomItemType.COPPER_BOOTS)).withChance(.1f).withEnchants(true, 20),
                new LootTableMember(plugin.getItemService().getCustomItem(CustomItemType.GRAPPLING_HOOK)).withChance(.005f),
                new LootTableMember(plugin.getItemService().getCustomItem(CustomItemType.SILVER_COIN)).withChance(.2f).withMax(5)
        ));
    }



    @EventHandler
    public void onLootChestGenerate(LootGenerateEvent event) {

        // If this isn't a chest return
        if (!(event.getInventoryHolder() instanceof Chest chest))
            return;

        // Attempt to find a structure this chest is contained in
        GeneratedStructure containedStructure = null;
        for (GeneratedStructure structure : chest.getChunk().getStructures())
            if (structure.getBoundingBox().contains(chest.getX(), chest.getY(), chest.getZ()))
                containedStructure = structure;

        // If the structure is null, we can't properly override loot
        if (containedStructure == null)
            return;

        int structureLevel = getMinimumEntityLevel(containedStructure);

        // Go through all the items. If there is anything with enchants on them, we need to re roll them with our logic.
        for (ItemStack loot : event.getLoot()) {

            if (loot.getEnchantments().isEmpty())
                continue;

            // We have enchants, completely re roll them
            loot.removeEnchantments();
            EnchantmentCalculator calculator = new EnchantmentCalculator(loot, EnchantmentCalculator.MAX_BOOKSHELF_BONUS, structureLevel, new Random().nextInt(Integer.MAX_VALUE));
            List<EnchantmentOffer> offers = calculator.calculate().get(EnchantmentCalculator.EnchantmentSlot.EXPENSIVE);
            for (EnchantmentOffer offer : offers)
                loot.addUnsafeEnchantment(offer.getEnchantment(), offer.getEnchantmentLevel());

        }

        // Now handle custom item injections if the loot tables desires it.
        CustomLootTable customLootTable = lootTableAdditions.get(LootTables.ABANDONED_MINESHAFT);
        if (customLootTable == null)
            return;

        // Count how many empty slots we have to work with
        int emptySlots = 0;
        for (int i = 0; i < chest.getBlockInventory().getSize(); i++)
            if (chest.getBlockInventory().getItem(i) == null)
                emptySlots++;

        // Roll items and add them to the empty slots
        Collection<ItemStack> extras = customLootTable.rollItems(event.getLootContext().getLuck(), emptySlots);
        event.getLoot().addAll(extras);
        Collections.shuffle(event.getLoot());

    }


}
