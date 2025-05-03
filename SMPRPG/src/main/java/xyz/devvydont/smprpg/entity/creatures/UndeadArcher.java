package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class UndeadArcher<T extends LivingEntity> extends CustomEntityInstance<T> {

    public UndeadArcher(Entity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public UndeadArcher(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();

        if (!(_entity instanceof LivingEntity living))
            return;

        living.getEquipment().setLeggings(null);
        living.getEquipment().setBoots(null);

        living.getEquipment().setHelmet(getAttributelessItem(Material.CHAINMAIL_HELMET));
        living.getEquipment().setChestplate(getAttributelessItem(Material.CHAINMAIL_CHESTPLATE));

        living.getEquipment().setItemInMainHand(getAttributelessItem(Material.BOW));
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_HELMET), 390, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_CHESTPLATE), 415, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_LEGGINGS), 390, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_BOOTS), 380, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_KUNAI), 420, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.BONE), 2, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.COOKIE), 2, this),
                new ChancedItemDrop(_plugin.getItemService().getCustomItem(Material.ARROW), 2, this)
        );
    }
}
