package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.BossInstance;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledWither extends BossInstance {

    public LeveledWither(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {
        return 40;
    }

    @Override
    public double calculateBaseAttackDamage() {
        return 950;
    }

    @Override
    public String getClassKey() {
        return VanillaEntity.VANILLA_CLASS_KEY;
    }

    @Override
    public EntityType getDefaultEntityType() {
        return EntityType.WITHER;
    }

    @Override
    public String getDefaultName() {
        return "Wither";
    }

    @Override
    public double calculateBaseHealth() {
        return 150_000;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_HELMET), 50, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_CHESTPLATE), 50, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_LEGGINGS), 50, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_BOOTS), 50, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.NETHER_STAR), 2, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_NETHER_STAR), 20, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_NETHER_STAR), 300, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
