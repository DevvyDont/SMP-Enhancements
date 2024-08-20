package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledWitherSkeleton extends VanillaEntity {

    public LeveledWitherSkeleton(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_HELMET), 2500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_CHESTPLATE), 2500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_LEGGINGS), 2500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_BOOTS), 2500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_KNIFE), 3000, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.COAL), 4, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_COAL), 750, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.DIAMOND_BLOCK), 45, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.COPPER_BLOCK), 30, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_DIAMOND_BLOCK), 80_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_COPPER_BLOCK), 65_000, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.BONE), 2, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PREMIUM_BONE), 80, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_BONE), 900, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.OBSIDIAN), 9, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.COMPRESSED_OBSIDIAN), 165, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_OBSIDIAN), 1250, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.WITHER_SKELETON_SKULL), 300, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
