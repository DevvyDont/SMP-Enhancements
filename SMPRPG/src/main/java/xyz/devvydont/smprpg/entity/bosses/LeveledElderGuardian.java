package xyz.devvydont.smprpg.entity.bosses;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledElderGuardian extends BossInstance<ElderGuardian> {

    public LeveledElderGuardian(ElderGuardian entity) {
        super(entity);
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return BossBar.bossBar(ComponentUtils.EMPTY, 1.0f, BossBar.Color.YELLOW, BossBar.Overlay.NOTCHED_20);
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.ELDER_GUARDIAN;
    }

    @Override
    public String getEntityName() {
        return "Elder Guardian";
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(20)
                .withHealth(125_000)
                .withDamage(90)
                .build();
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(

                // Drop pool from the normal guardians
                new QuantityLootDrop(ItemService.generate(CustomItemType.SOGGY_LETTUCE), 1, 3, this),

                new QuantityLootDrop(ItemService.generate(Material.PRISMARINE_SHARD), 1, 2, this),
                new QuantityLootDrop(ItemService.generate(Material.PRISMARINE_CRYSTALS), 1, 2, this),
                new ChancedItemDrop(ItemService.generate(Material.DIAMOND), 5, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_PRISMARINE_SHARD), 7, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_PRISMARINE_CRYSTAL), 7, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_DIAMOND), 200, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_PRISMARINE_SHARD), 95, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL), 95, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_DIAMOND_BLOCK), 10_000, this),

                // Gear drops
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_HELMET), 125, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_CHESTPLATE), 135, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_LEGGINGS), 130, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_BOOTS), 125, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_TRIDENT), 145, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNE_BOW), 145, this),

                // Pity drops
                // Crafts into Jupiter crystal, need 8 to get 1 crystal
                // Armor components, 24 required for full set from pity alone.
                new ChancedItemDrop(ItemService.generate(CustomItemType.JUPITERS_ARTIFACT), 30, this),
                new QuantityLootDrop(ItemService.generate(CustomItemType.JUPITER_CRYSTAL), 1, 1, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PLUTO_FRAGMENT), 3, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PLUTOS_ARTIFACT), 100, this),

                // Rare chance to be able to summon it again
                new ChancedItemDrop(ItemService.generate(CustomItemType.NEPTUNES_CONCH), 25, this)
        );
    }
}
