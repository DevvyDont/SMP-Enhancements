package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.items.CustomItemType;
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
                new QuantityLootDrop(_plugin.getItemService().getCustomItem(Material.SOUL_SAND), 1, 2, this),
                new QuantityLootDrop(_plugin.getItemService().getCustomItem(Material.OBSIDIAN), 1, 3, this),
                new QuantityLootDrop(_plugin.getItemService().getCustomItem(Material.COAL), 1, 3, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.WITHER_SKELETON_SKULL), 250, this),

                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.COMPRESSED_OBSIDIAN), 14, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_OBSIDIAN), 90, this),

                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.NETHER_STAR), 4, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_NETHER_STAR), 65, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_NETHER_STAR), 900, this),

                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_HELMET), 200, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_CHESTPLATE), 210, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_LEGGINGS), 200, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_BOOTS), 200, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_CUTLASS), 225, this),

                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.DESOLATED_STONE), 100, this),

                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.NETHERITE_INGOT), 70, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.NETHERITE_BLOCK), 900, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_NETHERITE), 15_000, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
