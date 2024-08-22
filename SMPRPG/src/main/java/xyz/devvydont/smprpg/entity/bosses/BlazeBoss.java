package xyz.devvydont.smprpg.entity.bosses;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomBossInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class BlazeBoss extends CustomBossInstance {

    public BlazeBoss(SMPRPG plugin, Entity entity, CustomEntityType type) {
        super(plugin, entity, type);
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        updateBaseAttribute(Attribute.GENERIC_SCALE, 9);
        updateBaseAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(

                // Gear drops
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_HELMET), 115, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_CHESTPLATE), 115, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_LEGGINGS), 115, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_BOOTS), 115, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_BLADE), 145, this),
                new ChancedItemDrop(ItemService.getItem(CustomItemType.INFERNO_SHORTBOW), 145, this)
        );
    }
}
