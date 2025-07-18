package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Creeper;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledCreeper extends VanillaEntity<Creeper> {

    public LeveledCreeper(Creeper entity) {
        super(entity);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.SMITE_HELMET), 250, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.SMITE_CHESTPLATE), 250, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.SMITE_LEGGINGS), 250, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.SMITE_BOOTS), 250, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_GUNPOWDER), 50, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_GUNPOWDER), 1000, this)
        );
    }
}
