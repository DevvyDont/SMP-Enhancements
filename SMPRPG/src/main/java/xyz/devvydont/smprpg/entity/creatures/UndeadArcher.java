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

public class UndeadArcher extends CustomEntityInstance {

    public UndeadArcher(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();

        if (!(entity instanceof LivingEntity living))
            return;

        living.getEquipment().setHelmet(getAttributelessItem(Material.CHAINMAIL_HELMET));
        living.getEquipment().setChestplate(getAttributelessItem(Material.CHAINMAIL_CHESTPLATE));
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_HELMET), 350, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_CHESTPLATE), 350, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_LEGGINGS), 350, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_BOOTS), 350, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.BONE), 2, this)
        );
    }
}
