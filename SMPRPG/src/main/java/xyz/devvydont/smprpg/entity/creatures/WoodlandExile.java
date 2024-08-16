package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class WoodlandExile extends CustomEntityInstance {

    public WoodlandExile(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();
        removeEquipment();
        setNoDropEquipment();
        if (entity instanceof LivingEntity living && living.getEquipment() != null) {
            if (getEntityType().equals(CustomEntityType.WOODLAND_BERSERKER))
                living.getEquipment().setItemInMainHand(getAttributelessItem(Material.IRON_AXE));
            else
                living.getEquipment().setItemInMainHand(getAttributelessItem(Material.CROSSBOW));
        }
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.POTATO_CHIP), 2, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.EMERALD), 5, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.EMERALD_BLOCK), 90, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_EMERALD), 1000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_EMERALD_BLOCK), 25_000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.STRENGTH_CHARM), 1250, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.LUCKY_CHARM), 1250, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.SPEED_CHARM), 5000, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.EXILED_CROSSBOW), 1400, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.EXILED_AXE), 1400, this)
        );
    }
}
