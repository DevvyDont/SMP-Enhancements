package xyz.devvydont.smprpg.entity.creatures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

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

        zv.getEquipment().setHelmet(null);
        zv.getEquipment().setChestplate(null);
        zv.getEquipment().setLeggings(null);
        zv.getEquipment().setBoots(null);

        zv.getEquipment().setHelmet(getAttributelessItem(Material.IRON_HELMET));

        if (Math.random() < .75)
            zv.getEquipment().setItemInMainHand(getAttributelessItem(WEAPONS[(int) (Math.random()*WEAPONS.length)]));

        if (Math.random() < .33)
            zv.getEquipment().setChestplate(getAttributelessItem(Material.GOLDEN_CHESTPLATE));
    }


    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_HELMET), 500, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_CHESTPLATE), 500, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_LEGGINGS), 500, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_BOOTS), 500, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.MYSTBLOOM_KUNAI), 500, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.ROTTEN_FLESH), 3, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.STALE_BREAD), 3, this)
        );
    }
}
