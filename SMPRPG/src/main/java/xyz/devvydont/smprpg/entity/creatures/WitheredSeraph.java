package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class WitheredSeraph<T extends LivingEntity> extends CustomEntityInstance<T> {

    public WitheredSeraph(T entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public WitheredSeraph(Entity entity, CustomEntityType type) {
        super(entity, type);
    }

    @Override
    public void setup() {
        super.setup();

        if (_entity.getEquipment() == null)
            return;

        _entity.getEquipment().setItemInMainHand(getAttributelessItem(Material.NETHERITE_HOE));
        _entity.getEquipment().setChestplate(getAttributelessItem(Material.NETHERITE_CHESTPLATE));
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        this.updateBaseAttribute(Attribute.SCALE, 1.2);
        this.updateBaseAttribute(Attribute.MOVEMENT_SPEED, .25);
    }


    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.OBSIDIAN), 5, this),
                new QuantityLootDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.BONE), 1, 2, this)
        );
    }
}
