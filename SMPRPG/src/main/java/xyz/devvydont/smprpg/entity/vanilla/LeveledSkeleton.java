package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.services.ItemService;

public class LeveledSkeleton extends VanillaEntity<Skeleton> {

    public static NamespacedKey key = new NamespacedKey("smprpg", "skeleton_nerfed_bow");

    /**
     * Skeletons are annoying as fuck. Give them a bow that does -30% damage.
     * @return A bow for the skeletons to wield.
     */
    public static ItemStack getSkeletonBow() {
        var bow = ItemService.generate(Material.BOW);
        bow.editMeta(meta -> meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(key, -.3, AttributeModifier.Operation.MULTIPLY_SCALAR_1)));
        return bow;
    }

    public LeveledSkeleton(Skeleton entity) {
        super(entity);
    }

    @Override
    public void setup() {
        super.setup();
        _entity.getEquipment().setItemInMainHand(getSkeletonBow());
    }
}
