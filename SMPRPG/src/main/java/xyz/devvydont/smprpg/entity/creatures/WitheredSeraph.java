package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;

public class WitheredSeraph extends CustomEntityInstance {

    public WitheredSeraph(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();

        if (!(entity instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        living.getEquipment().setItemInMainHand(getAttributelessItem(Material.NETHERITE_HOE));
        living.getEquipment().setChestplate(getAttributelessItem(Material.NETHERITE_CHESTPLATE));
        setNoDropEquipment();
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        this.updateBaseAttribute(Attribute.GENERIC_SCALE, 1.2);
        this.updateBaseAttribute(Attribute.GENERIC_MOVEMENT_SPEED, .25);
    }
}
