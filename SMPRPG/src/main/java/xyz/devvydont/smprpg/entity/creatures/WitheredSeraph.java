package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;

public class WitheredSeraph extends CustomEntityInstance {

    public WitheredSeraph(SMPRPG plugin, LivingEntity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();

        if (entity.getEquipment() == null)
            return;

        entity.getEquipment().setItemInMainHand(getAttributelessItem(Material.NETHERITE_HOE));
        entity.getEquipment().setChestplate(getAttributelessItem(Material.NETHERITE_CHESTPLATE));
        setNoDropEquipment();
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        entity.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.2);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15);
    }
}
