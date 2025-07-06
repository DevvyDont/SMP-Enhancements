package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class WoodlandExile<T extends LivingEntity> extends CustomEntityInstance<T> {

    public WoodlandExile(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public WoodlandExile(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();
        removeEquipment();
        setNoDropEquipment();

        if (_entity.getEquipment() != null)
            if (getEntityType().equals(CustomEntityType.WOODLAND_BERSERKER))
                _entity.getEquipment().setItemInMainHand(getAttributelessItem(Material.IRON_AXE));
            else
                _entity.getEquipment().setItemInMainHand(getAttributelessItem(Material.CROSSBOW));
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.POTATO_CHIP), 2, this),
                new ChancedItemDrop(ItemService.generate(Material.EMERALD), 5, this),
                new ChancedItemDrop(ItemService.generate(Material.EMERALD_BLOCK), 90, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_EMERALD), 1250, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.ENCHANTED_EMERALD_BLOCK), 80_000, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.STRENGTH_CHARM), 750, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.LUCKY_CHARM), 750, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.SPEED_CHARM), 850, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.EXILED_CROSSBOW), 700, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.EXILED_AXE), 700, this)
        );
    }
}
