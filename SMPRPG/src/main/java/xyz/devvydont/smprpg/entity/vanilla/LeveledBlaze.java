package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledBlaze extends VanillaEntity<Blaze> {

    public LeveledBlaze(Blaze entity) {
        super(entity);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.CHILI_PEPPER), 3, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.CYRAX_HELMET), 2500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.CYRAX_CHESTPLATE), 2500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.CYRAX_LEGGINGS), 2500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.CYRAX_BOOTS), 2500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.CYRAX_BOW), 2500, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.BOILING_PICKAXE), 4000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.BOILING_INGOT), 3000, this),

                new ChancedItemDrop(ItemService.generate(Material.BLAZE_ROD), 2, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.PREMIUM_BLAZE_ROD), 35, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_BLAZE_ROD), 500, this),

                new ChancedItemDrop(ItemService.generate(CustomItemType.INFERNO_ARROW), 1000, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
