package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Spider;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledSpider extends VanillaEntity<Spider> {

    public LeveledSpider(Spider entity) {
        super(entity);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ARAXYS_HELMET), 25_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ARAXYS_CHESTPLATE), 25_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ARAXYS_LEGGINGS), 25_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ARAXYS_BOOTS), 25_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PREMIUM_STRING), 500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_STRING), 4_000, this)
        );
    }

}
