package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Warden;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.entity.components.EntityConfiguration;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledWarden extends BossInstance<Warden> {


    public LeveledWarden(Warden entity) {
        super(entity);
    }

    @Override
    public long getTimeLimit() {
        return 5L * 60L;
    }

    @Override
    public @Nullable BossBar createBossBar() {
        return BossBar.bossBar(ComponentUtils.EMPTY, 1.0f, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_20);
    }

    @Override
    public EntityConfiguration getDefaultConfiguration() {
        return EntityConfiguration.builder()
                .withLevel(100)
                .withHealth(250_000_000)
                .withDamage(200_000)
                .build();
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.WARDEN;
    }

    @Override
    public String getEntityName() {
        return "Warden";
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_HELMET), 1000, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_CHESTPLATE), 1000, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_LEGGINGS), 1000, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_BOOTS), 1000, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_ECHO_SHARD), 80, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_ECHO_SHARD), 500, this),
                new QuantityLootDrop(_plugin.getItemService().getCustomItem(Material.ECHO_SHARD), 2, 7, this)
        );
    }
}
