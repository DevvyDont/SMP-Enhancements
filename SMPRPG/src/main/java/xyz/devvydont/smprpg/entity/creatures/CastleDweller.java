package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;

public class CastleDweller extends CustomEntityInstance {

    public CastleDweller(SMPRPG plugin, Entity entity, CustomEntityType entityType) {
        super(plugin, entity, entityType);
    }

    public static final Material[] WEAPONS = {Material.WOODEN_SHOVEL, Material.WOODEN_AXE, Material.STONE_SHOVEL, Material.STONE_HOE, Material.GOLDEN_SHOVEL};

    @Override
    public void setup() {
        super.setup();
        ZombieVillager zv = (ZombieVillager) entity;
        zv.setVillagerProfession(Villager.Profession.values()[(int) (Math.random()*Villager.Profession.values().length)]);

        if (Math.random() < .75)
            zv.getEquipment().setItemInMainHand(getAttributelessItem(WEAPONS[(int) (Math.random()*WEAPONS.length)]));
    }
}
