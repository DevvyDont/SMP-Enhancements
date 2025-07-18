package xyz.devvydont.smprpg.entity.bosses;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledWither extends BossInstance<Wither> {

    public LeveledWither(Wither entity) {
        super(entity);
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return null;
    }

    /*
     * 5 min
     */
    @Override
    public long getTimeLimit() {
        return 60L * 5L;
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(40)
                .withHealth(400_000)
                .withDamage(1450)
                .build();
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.WITHER;
    }

    @Override
    public String getEntityName() {
        return "Wither";
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new QuantityLootDrop(ItemService.generate(Material.SOUL_SAND), 1, 2, this),
                new QuantityLootDrop(ItemService.generate(Material.OBSIDIAN), 1, 3, this),
                new QuantityLootDrop(ItemService.generate(Material.COAL), 1, 3, this),
                new ChancedItemDrop(ItemService.generate(Material.WITHER_SKELETON_SKULL), 50, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.COMPRESSED_OBSIDIAN), 14, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_OBSIDIAN), 90, this),

                new ChancedItemDrop(ItemService.generate(Material.NETHER_STAR), 4, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_NETHER_STAR), 65, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_NETHER_STAR), 900, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.FORSAKEN_HELMET), 100, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.FORSAKEN_CHESTPLATE), 125, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.FORSAKEN_LEGGINGS), 110, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.FORSAKEN_BOOTS), 100, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.FORSAKEN_CUTLASS), 125, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.DESOLATED_STONE), 100, this),

                new ChancedItemDrop(ItemService.generate(Material.NETHERITE_INGOT), 70, this),
                new ChancedItemDrop(ItemService.generate(Material.NETHERITE_BLOCK), 900, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_NETHERITE), 15_000, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
