package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Spider;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class MansionSpider extends CustomEntityInstance<Spider> {

    public MansionSpider(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public MansionSpider(Spider entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.COTTON_CANDY), 2, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ARAXYS_HELMET), 750, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ARAXYS_CHESTPLATE), 750, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ARAXYS_LEGGINGS), 750, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ARAXYS_BOOTS), 750, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ARAXYS_CLAW), 750, this),
                new ChancedItemDrop(ItemService.generate(Material.STRING), 2, this),
                new ChancedItemDrop(ItemService.generate(Material.SPIDER_EYE), 2, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_STRING), 110, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_SPIDER_EYE), 70, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_STRING), 1100, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_SPIDER_EYE), 700, this)
        );
    }
}
