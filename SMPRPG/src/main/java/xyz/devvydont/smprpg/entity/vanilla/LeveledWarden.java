package xyz.devvydont.smprpg.entity.vanilla;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledWarden extends BossInstance {


    public LeveledWarden(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
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
    public int getDefaultLevel() {
        return 100;
    }

    @Override
    public double calculateBaseHealth() {
        return 250_000_000;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 100_000;
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
    public TextColor determineNametagColor() {
        return NamedTextColor.DARK_PURPLE;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_HELMET), 1000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_CHESTPLATE), 1000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_LEGGINGS), 1000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PRELUDE_BOOTS), 1000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_ECHO_SHARD), 80, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_ECHO_SHARD), 500, this),
                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.ECHO_SHARD), 2, 7, this)
        );
    }
}
