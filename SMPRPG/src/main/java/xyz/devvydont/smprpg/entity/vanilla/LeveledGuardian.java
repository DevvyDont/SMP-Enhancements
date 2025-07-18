package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Guardian;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledGuardian extends VanillaEntity<Guardian> {

    public LeveledGuardian(Guardian entity) {
        super(entity);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.SOGGY_LETTUCE), 2, this),

                new QuantityLootDrop(ItemService.generate(Material.PRISMARINE_SHARD), 1, 3, this),
                new QuantityLootDrop(ItemService.generate(Material.PRISMARINE_CRYSTALS), 1, 3, this),
                new ChancedItemDrop(ItemService.generate(Material.DIAMOND), 10, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_PRISMARINE_SHARD), 30, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_PRISMARINE_CRYSTAL), 30, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_DIAMOND), 500, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_PRISMARINE_SHARD), 140, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL), 140, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_DIAMOND_BLOCK), 15000, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNES_CONCH), 80, this),

                // Extremely rare pity drops
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_HELMET), 200_000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_CHESTPLATE), 250_000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_LEGGINGS), 225_000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_BOOTS), 200_000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_TRIDENT), 250_000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_BOW), 250_000, this),

                // Pity drops
                // Crafts into Jupiter crystal, need 8 to get 1 crystal
                new ChancedItemDrop(ItemService.generate(CustomItemType.JUPITER_CRYSTAL), 1500, this),
                // Armor components, 24 required for full set from pity alone.
                new ChancedItemDrop(ItemService.generate(CustomItemType.JUPITERS_ARTIFACT), 7500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PLUTO_FRAGMENT), 4500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PLUTOS_ARTIFACT), 20_000, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
