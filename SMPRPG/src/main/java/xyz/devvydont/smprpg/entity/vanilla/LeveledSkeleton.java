package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledSkeleton extends VanillaEntity {

    public LeveledSkeleton(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public void setup() {
        super.setup();
        if (entity instanceof LivingEntity living && living.getEquipment() != null)
            living.getEquipment().setItemInMainHand(plugin.getItemService().getCustomItem(Material.BOW));
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return .7;
    }
}
