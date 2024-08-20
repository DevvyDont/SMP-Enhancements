package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledEnderman extends VanillaEntity implements Listener {

    public static final int MINIMUM_LEVEL = 50;
    public static final int END_MINIMUM_LEVEL = 60;

    public LeveledEnderman(SMPRPG plugin, Entity entity) {
        super(plugin, entity);
    }

    @Override
    public int getDefaultLevel() {

        int level = MINIMUM_LEVEL;

        if (entity.getWorld().getEnvironment().equals(World.Environment.THE_END))
            level = END_MINIMUM_LEVEL;

        level += getLevelDistanceBoost();
        return Math.min(level, 99);
    }

    @Override
    public double calculateBaseHealthMultiplier() {
        return 1.2;
    }

    public boolean canDropCrystal() {
        return getEntity().getWorld().getBiome(getEntity().getLocation()).equals(Biome.THE_END) &&
                Math.abs(getEntity().getLocation().getX()) <= 156 &&
                Math.abs(getEntity().getLocation().getZ()) <= 156;
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        List<LootDrop> drops = new java.util.ArrayList<>(List.of(
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.PREMIUM_ENDER_PEARL), 50, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.ENCHANTED_ENDER_PEARL), 250, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.UNSTABLE_HELMET), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.UNSTABLE_CHESTPLATE), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.UNSTABLE_LEGGINGS), 1000, this),
                new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(CustomItemType.UNSTABLE_BOOTS), 1000, this)
        ));

        if (canDropCrystal())
            drops.add(new ChancedItemDrop(SMPRPG.getInstance().getItemService().getCustomItem(Material.END_CRYSTAL), 50, this));

        return drops;

    }
}
