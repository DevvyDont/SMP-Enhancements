package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class MansionSpider extends CustomEntityInstance {

    public MansionSpider(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ARAXYS_HELMET), 750, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ARAXYS_CHESTPLATE), 750, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ARAXYS_LEGGINGS), 750, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ARAXYS_BOOTS), 750, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ARAXYS_CLAW), 750, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.STRING), 2, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.SPIDER_EYE), 2, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_STRING), 35, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_SPIDER_EYE), 55, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_STRING), 125, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_SPIDER_EYE), 225, this)
        );
    }
}