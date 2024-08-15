package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledZombie extends VanillaEntity {

    public LeveledZombie(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public void setup() {
        super.setup();

        // Remove enchantments from zombie's items
        if (entity instanceof LivingEntity living && living.getEquipment() != null && !living.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
            ItemStack item = living.getEquipment().getItemInMainHand();
            item.removeEnchantments();
            living.getEquipment().setItemInMainHand(item);
        }
    }

}
