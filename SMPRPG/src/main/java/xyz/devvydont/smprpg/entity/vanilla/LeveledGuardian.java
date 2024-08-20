package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledGuardian extends VanillaEntity {

    public LeveledGuardian(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return .6;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.SOGGY_LETTUCE), 2, this),

                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.PRISMARINE_SHARD), 1, 3, this),
                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.PRISMARINE_CRYSTALS), 1, 3, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.DIAMOND), 10, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_PRISMARINE_SHARD), 30, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_PRISMARINE_CRYSTAL), 30, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_DIAMOND), 500, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_SHARD), 140, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL), 140, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_DIAMOND_BLOCK), 15000, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.NEPTUNES_CONCH), 80, this),

                // Extremely rare pity drops
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_HELMET), 200_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_CHESTPLATE), 200_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_LEGGINGS), 200_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_BOOTS), 200_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_TRIDENT), 200_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.NEPTUNE_BOW), 200_000, this),

                // Pity drops
                // Crafts into Jupiter crystal, need 8 to get 1 crystal
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.JUPITER_CRYSTAL), 1500, this),
                // Armor components, 24 required for full set from pity alone.
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.JUPITERS_ARTIFACT), 7500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PLUTO_FRAGMENT), 4500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PLUTOS_ARTIFACT), 20_000, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
