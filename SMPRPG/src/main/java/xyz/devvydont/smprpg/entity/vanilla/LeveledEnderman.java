package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class LeveledEnderman extends VanillaEntity implements Listener {

    public static final int MINIMUM_LEVEL = 40;
    public static final int END_MINIMUM_LEVEL = 50;

    public LeveledEnderman(SMPRPG plugin, LivingEntity entity) {
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
        return 2.0;
    }

}
