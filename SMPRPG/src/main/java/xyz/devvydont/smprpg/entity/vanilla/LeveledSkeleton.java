package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Skeleton;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.services.AttributeService;

public class LeveledSkeleton extends VanillaEntity<Skeleton> {

    public LeveledSkeleton(Skeleton entity) {
        super(entity);
    }

    @Override
    public void setup() {
        super.setup();
        var attr = SMPRPG.getService(AttributeService.class).getAttribute(_entity, AttributeWrapper.STRENGTH);

        // Nerf the skeleton damage a bit. This is because skeleton base damage stacks on top of the damage from the bow.
        if (attr != null)
            attr.addModifier(new AttributeModifier(new NamespacedKey("smprpg", "skeleton_nerf"), -.3, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
    }
}
