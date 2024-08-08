package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;

public class UndeadArcher extends CustomEntityInstance {

    public UndeadArcher(SMPRPG plugin, LivingEntity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    @Override
    public void setup() {
        super.setup();
        entity.getEquipment().setChestplate(getAttributelessItem(Material.CHAINMAIL_CHESTPLATE));
    }
}
