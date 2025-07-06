package xyz.devvydont.smprpg.entity.creatures;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ZombieVillager;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.base.CustomEntityInstance;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CastleDweller extends CustomEntityInstance<ZombieVillager> {

    public CastleDweller(Entity entity, CustomEntityType type) {
        this((ZombieVillager) entity, type);
    }

    public CastleDweller(ZombieVillager entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    public static final Material[] WEAPONS = {Material.WOODEN_SHOVEL, Material.WOODEN_AXE, Material.STONE_SHOVEL, Material.STONE_HOE, Material.GOLDEN_SHOVEL};

    @Override
    public void setup() {
        super.setup();
        var professions = new java.util.ArrayList<>(RegistryAccess.registryAccess().getRegistry(RegistryKey.VILLAGER_PROFESSION).stream().toList());
        Collections.shuffle(professions);
        var profession = professions.getFirst();
        _entity.setVillagerProfession(profession);

        _entity.getEquipment().setHelmet(null);
        _entity.getEquipment().setChestplate(null);
        _entity.getEquipment().setLeggings(null);
        _entity.getEquipment().setBoots(null);

        _entity.getEquipment().setHelmet(getAttributelessItem(Material.IRON_HELMET));

        if (Math.random() < .75)
            _entity.getEquipment().setItemInMainHand(getAttributelessItem(WEAPONS[(int) (Math.random()*WEAPONS.length)]));

        if (Math.random() < .33)
            _entity.getEquipment().setChestplate(getAttributelessItem(Material.GOLDEN_CHESTPLATE));
    }


    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(ItemService.generate(CustomItemType.MYSTBLOOM_HELMET), 400, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.MYSTBLOOM_CHESTPLATE), 425, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.MYSTBLOOM_LEGGINGS), 400, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.MYSTBLOOM_BOOTS), 390, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.MYSTBLOOM_KUNAI), 430, this),
                new ChancedItemDrop(ItemService.generate(Material.ROTTEN_FLESH), 3, this),
                new ChancedItemDrop(ItemService.generate(CustomItemType.STALE_BREAD), 3, this)
        );
    }
}
