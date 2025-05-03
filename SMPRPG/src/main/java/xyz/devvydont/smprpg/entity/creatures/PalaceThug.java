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

public class PalaceThug<T extends LivingEntity> extends CustomEntityInstance<T> {

    public PalaceThug(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public PalaceThug(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();
        removeEquipment();
        setNoDropEquipment();
        if (_entity.getEquipment() != null) {
            _entity.getEquipment().setHelmet(getAttributelessItem(Material.DIAMOND_BLOCK));
            _entity.getEquipment().setBoots(getAttributelessItem(Material.IRON_BOOTS));
            _entity.getEquipment().setItemInOffHand(getAttributelessItem(Material.COPPER_BLOCK));
            _entity.getEquipment().setItemInMainHand(getAttributelessItem(Material.DIAMOND_SWORD));
        }
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.STOLEN_APPLE), 3, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_HELMET), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_CHESTPLATE), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_LEGGINGS), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_BOOTS), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.REAVER_KNIFE), 1250, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.COAL), 4, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_COAL), 500, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.DIAMOND_BLOCK), 30, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.COPPER_BLOCK), 20, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_DIAMOND_BLOCK), 60_000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_COPPER_BLOCK), 45_000, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.BONE), 3, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PREMIUM_BONE), 60, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_BONE), 700, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.OBSIDIAN), 4, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.COMPRESSED_OBSIDIAN), 75, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_OBSIDIAN), 500, this),

                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.WITHER_SKELETON_SKULL), 300, this)
        );
    }
}
