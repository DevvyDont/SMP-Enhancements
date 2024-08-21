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
        return 1450;
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
    public String getEntityName() {
        return "Wither";
    }

    @Override
    public double calculateBaseHealth() {
        return 300_000;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.SOUL_SAND), 1, 2, this),
                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.OBSIDIAN), 1, 3, this),
                new QuantityLootDrop(plugin.getItemService().getCustomItem(Material.COAL), 1, 3, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.WITHER_SKELETON_SKULL), 300, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.COMPRESSED_OBSIDIAN), 14, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_OBSIDIAN), 90, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.NETHER_STAR), 4, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_NETHER_STAR), 65, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_NETHER_STAR), 900, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_HELMET), 120, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_CHESTPLATE), 120, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_LEGGINGS), 120, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_BOOTS), 120, this),

                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.NETHERITE_INGOT), 100, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.NETHERITE_BLOCK), 1100, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_NETHERITE), 13_000, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
