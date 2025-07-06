package xyz.devvydont.smprpg.entity.bosses;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
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

public class LeveledDragon extends BossInstance<EnderDragon> {

    private final boolean wasSummoned = false;

    public LeveledDragon(EnderDragon entity) {
        super(entity);
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return null;
    }

    @Override
    public long getTimeLimit() {
        return wasSummoned ? 60*5 : INFINITE_TIME_LIMIT;
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public String getEntityName() {
        return "Ender Dragon";
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(wasSummoned ? 50 : 40)
                .withHealth(wasSummoned ? 3_000_000 : 1_000_000)
                .withDamage(wasSummoned ? 1250 : 500)
                .build();
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.ELDERFLAME_HELMET), 500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ELDERFLAME_CHESTPLATE), 500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ELDERFLAME_LEGGINGS), 500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ELDERFLAME_BOOTS), 500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.DRACONIC_CRYSTAL), 20, this),
                new QuantityLootDrop(ItemService.generate(CustomItemType.DRAGON_SCALES), 2, 7, this)
        );
    }

}
