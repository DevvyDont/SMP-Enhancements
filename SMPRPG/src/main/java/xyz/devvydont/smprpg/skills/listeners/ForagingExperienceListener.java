package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

public class ForagingExperienceListener implements Listener {

    public static int getBaseExperienceForBlock(Block block) {

        double dimensionMultiplier = switch (block.getWorld().getEnvironment()) {
            case NETHER -> 1.5;
            case THE_END -> 2;
            default -> 1;
        };

        int exp = switch (block.getType()) {

            case CHORUS_FLOWER -> 50;
            case CHORUS_PLANT -> 30;

            case CRIMSON_STEM, WARPED_STEM -> 25;
            case STRIPPED_CRIMSON_STEM, STRIPPED_WARPED_STEM -> 20;
            case NETHER_WART_BLOCK, WARPED_WART_BLOCK -> 16;

            case ACACIA_LOG, BIRCH_LOG, OAK_LOG -> 15;
            case CHERRY_LOG, JUNGLE_LOG, SPRUCE_LOG -> 18;
            case DARK_OAK_LOG, MANGROVE_LOG -> 21;
            case STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BAMBOO_BLOCK, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_CHERRY_LOG, STRIPPED_CHERRY_WOOD, STRIPPED_CRIMSON_HYPHAE, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_MANGROVE_LOG, STRIPPED_MANGROVE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, STRIPPED_WARPED_HYPHAE -> 5;
            case ACACIA_PLANKS, BAMBOO_PLANKS, BIRCH_PLANKS, CHERRY_PLANKS, CRIMSON_PLANKS, DARK_OAK_PLANKS, JUNGLE_PLANKS, MANGROVE_PLANKS, OAK_PLANKS, SPRUCE_PLANKS, WARPED_PLANKS -> 4;

            default -> 0;
        };

        return (int) (exp * dimensionMultiplier);
    }


    final SMPRPG plugin;

    public ForagingExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * When a player places a block, we need to mark that block as unable to be farmed for experience
     *
     * @param event
     */
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {

        if (event.isCancelled())
            return;

        // When any block is placed, it is no longer able to earn skill experience
        ChunkUtil.markBlockSkillInvalid(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGainGeneralMiningExperience(BlockBreakEvent event) {

        if (event.isCancelled())
            return;

        // If this block isn't allowed to retrieve experience
        if (ChunkUtil.isBlockSkillInvalid(event.getBlock()))
            return;

        SkillInstance skill = SMPRPG.getService(EntityService.class).getPlayerInstance(event.getPlayer()).getWoodcuttingSkill();
        skill.addExperience(getBaseExperienceForBlock(event.getBlock()));
        ChunkUtil.markBlockSkillValid(event.getBlock());
    }

}
