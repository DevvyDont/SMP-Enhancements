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

public class LeveledBlaze extends VanillaEntity {

    public LeveledBlaze(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.CHILI_PEPPER), 3, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.EMBERCLAD_HELMET), 1250, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.EMBERCLAD_CHESTPLATE), 1250, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.EMBERCLAD_LEGGINGS), 1250, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.EMBERCLAD_BOOTS), 1250, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.EMBERCLAD_AXE), 1750, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.BOILING_PICKAXE), 4000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.BOILING_INGOT), 3000, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.BLAZE_ROD), 2, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PREMIUM_BLAZE_ROD), 35, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_BLAZE_ROD), 500, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.INFERNO_ARROW), 250, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
