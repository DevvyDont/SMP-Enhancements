package xyz.devvydont.smprpg.skills.listeners;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.block.impl.CraftReed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillGlobals;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

public class FarmingExperienceListener implements Listener {

    final SMPRPG plugin;

    public static int getExperienceValue(ItemStack item) {

        int experience = switch (item.getType()) {

            case FLOWERING_AZALEA, FLOWERING_AZALEA_LEAVES, POPPY, ROSE_BUSH, TALL_GRASS, SHORT_GRASS, SEAGRASS,
                 TALL_SEAGRASS, DANDELION, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP, ORANGE_TULIP, PINK_TULIP,
                 WHITE_TULIP, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, LILY_PAD, PINK_PETALS, LILAC, PEONY, KELP -> 1;

            case PITCHER_PLANT, PITCHER_CROP, PITCHER_POD -> 2;

            case COCOA_BEANS -> 1;

            case GLOW_BERRIES, SWEET_BERRIES -> 6;

            case BROWN_MUSHROOM, RED_MUSHROOM, DEAD_BUSH -> 3;

            case SPORE_BLOSSOM -> 34;

            case BAMBOO -> 2;

            case BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK -> 21;

            case SEA_PICKLE -> 45;

            case CRIMSON_FUNGUS, WARPED_FUNGUS -> 65;

            case WITHER_ROSE -> 20;

            case MELON -> 12;
            case MELON_SEEDS -> 1;

            case PUMPKIN -> 10;
            case PUMPKIN_SEEDS -> 1;

            case BEETROOT, BEETROOTS -> 3;
            case BEETROOT_SEEDS -> 1;

            case TORCHFLOWER, TORCHFLOWER_CROP -> 15;
            case TORCHFLOWER_SEEDS -> 1;

            case WHEAT -> 4;
            case WHEAT_SEEDS -> 1;

            case CARROT, CARROTS, POTATO, POTATOES -> 5;

            case NETHER_WART -> 7;

            case SUGAR_CANE -> 3;
            case CACTUS -> 7;

            default -> 0;
        };

        return experience * item.getAmount();

    }

    public FarmingExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHarvestCrop(PlayerHarvestBlockEvent event) {

        if (event.isCancelled())
            return;

        int exp = 0;
        for (ItemStack item : event.getItemsHarvested())
            exp += getExperienceValue(item);

        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        player.getFarmingSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.HARVEST);
    }

    @EventHandler
    public void onHarvestBlock(BlockBreakEvent event) {

        if (event.isCancelled())
            return;

        int exp = 0;
        for (ItemStack item : event.getBlock().getDrops())
            exp += getExperienceValue(item);

        if (exp <= 0)
            return;

        // If the block is ageable don't give xp unless it is fully matured
        if (event.getBlock().getBlockData() instanceof Ageable ageable)
            if (ageable.getMaximumAge() != ageable.getAge())
                return;

        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        player.getFarmingSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.HARVEST);
    }

}
