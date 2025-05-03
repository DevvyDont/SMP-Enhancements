package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledSkeleton extends VanillaEntity<Skeleton> {

    public LeveledSkeleton(Skeleton entity) {
        super(entity);
    }

    @Override
    public void setup() {
        super.setup();
        _entity.getEquipment().setItemInMainHand(_plugin.getItemService().getCustomItem(Material.BOW));
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return .7;
    }
}
