package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledWither extends VanillaEntity implements Listener {

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
    public double calculateBaseHealth() {
        return 150_000;
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return 1.0;
    }

    @Override
    public double calculateBaseDamageMultiplier() {
        return 1.0;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_HELMET), 15, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_CHESTPLATE), 15, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_LEGGINGS), 15, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.FORSAKEN_BOOTS), 15, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(Material.NETHER_STAR), 2, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.PREMIUM_NETHER_STAR), 15, this),
                new ChancedItemDrop(plugin.getItemService().getCustomItem(CustomItemType.ENCHANTED_NETHER_STAR), 200, this)
        );
    }

    @Override
    public boolean hasVanillaDrops() {
        return false;
    }
}
