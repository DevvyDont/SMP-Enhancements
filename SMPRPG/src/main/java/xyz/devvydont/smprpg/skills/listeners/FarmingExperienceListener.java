package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.LeveledPlayer;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

import java.util.Collection;

public class FarmingExperienceListener implements Listener {

    final SMPRPG plugin;

    public static int getExperienceValue(ItemStack item) {

        int experience = switch (item.getType()) {

            case FLOWERING_AZALEA, FLOWERING_AZALEA_LEAVES, POPPY, ROSE_BUSH, TALL_GRASS, SHORT_GRASS, SEAGRASS,
                 TALL_SEAGRASS, DANDELION, BLUE_ORCHID, ALLIUM, AZURE_BLUET, RED_TULIP, ORANGE_TULIP, PINK_TULIP,
                 WHITE_TULIP, OXEYE_DAISY, CORNFLOWER, LILY_OF_THE_VALLEY, LILY_PAD, PINK_PETALS, LILAC, PEONY, KELP, KELP_PLANT -> 1;

            case PITCHER_PLANT, PITCHER_CROP, PITCHER_POD -> 2;

            case COCOA_BEANS -> 1;

            case GLOW_BERRIES, SWEET_BERRIES -> 3;

            case BROWN_MUSHROOM, RED_MUSHROOM, DEAD_BUSH -> 3;

            case SPORE_BLOSSOM -> 7;

            case BAMBOO -> 2;

            case BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK -> 21;

            case SEA_PICKLE -> 5;

            case CRIMSON_FUNGUS, WARPED_FUNGUS -> 4;

            case WITHER_ROSE -> 6;

            case MELON -> 6;
            case MELON_SLICE -> 1;
            case MELON_SEEDS -> 1;

            case PUMPKIN -> 6;
            case PUMPKIN_SEEDS -> 1;

            case BEETROOT, BEETROOTS -> 3;
            case BEETROOT_SEEDS -> 1;

            case TORCHFLOWER, TORCHFLOWER_CROP -> 7;
            case TORCHFLOWER_SEEDS -> 1;

            case WHEAT -> 4;
            case WHEAT_SEEDS -> 1;

            case CARROT, CARROTS, POTATO, POTATOES -> 3;

            case NETHER_WART -> 6;

            case SUGAR_CANE -> 2;
            case CACTUS -> 5;

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

    private int getExperienceForDrops(Collection<ItemStack> drops, World.Environment environment) {

        double multiplier = switch (environment) {
            case NETHER -> 1.2;
            case THE_END -> 1.5;
            default -> 1;
        };

        // Loop through every drop from breaking this block and award XP
        int exp = 0;
        for (ItemStack item : drops)
            exp += getExperienceValue(item);
        return (int) (exp * multiplier);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHarvestBlock(BlockBreakEvent event) {

        boolean isAgeable = event.getBlock().getBlockData() instanceof Ageable ageable;

        // If this block is marked as skill invalid, we have some things we need to do.
        if (ChunkUtil.isBlockSkillInvalid(event.getBlock())) {

            // Mark the block as valid again. Block breaks mean this is valid now.
            ChunkUtil.markBlockSkillValid(event.getBlock());

            // If this block does not have age states, we don't have to consider anything. past this point
            if (!isAgeable)
                return;
        }

        // Loop through every drop from breaking this block and award XP
        int exp = getExperienceForDrops(event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer()), event.getPlayer().getWorld().getEnvironment());
        if (exp <= 0)
            return;

        // If the block is ageable don't give xp unless it is fully matured
        if (event.getBlock().getBlockData() instanceof Ageable ageable)
            if (ageable.getMaximumAge() != ageable.getAge())
                return;

        LeveledPlayer player = plugin.getEntityService().getPlayerInstance(event.getPlayer());
        player.getFarmingSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.HARVEST);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onGrow(BlockGrowEvent event) {
        ChunkUtil.markBlockSkillValid(event.getBlock());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPistonShove(BlockPistonExtendEvent event) {

        // Carry over the validity of blocks in the direction that the piston is extending.
        for (Block block : event.getBlocks()) {

            Block newPosition = block.getRelative(event.getDirection());

            // If the current block we are checking is not valid for skills, then carry it over to the position we are
            // extending to. Otherwise, we can just ignore
            if (ChunkUtil.isBlockSkillInvalid(block))
                ChunkUtil.markBlockSkillInvalid(newPosition);
        }

    }

    /**
     * Used to give experience to sugar cane/kelp/bamboo breaks. We completely override vanilla behavior
     * so we can properly hook into the "chain break" effect to give experience
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreakBambooSugarCane(BlockBreakEvent event) {

        Material blockType = event.getBlock().getType();

        // Listen to when sugar cane or bamboo is broken
        if (!(blockType.equals(Material.SUGAR_CANE) || blockType.equals(Material.BAMBOO) || blockType.equals(Material.KELP_PLANT) || blockType.equals(Material.CACTUS)))
            return;

        // We are going to manually break all the blocks.
        event.setCancelled(true);
        SkillInstance farming = plugin.getEntityService().getPlayerInstance(event.getPlayer()).getFarmingSkill();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();

        // Loop through every block above this and do the same a certain amount of ticks later
        for (int y = event.getBlock().getY(); y <= event.getBlock().getWorld().getMaxHeight(); y++) {

            int yOffset = y - event.getBlock().getY();

            Block block = event.getBlock().getRelative(BlockFace.UP, yOffset);
            // If this block doesn't match the original block, stop checking
            if (!block.getType().equals(blockType))
                return;

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                block.getWorld().playSound(block.getLocation(), block.getBlockSoundGroup().getBreakSound(), 1, 1);
                Collection<ItemStack> laterDrops = block.getDrops(tool);
                if (!ChunkUtil.isBlockSkillInvalid(block))
                    farming.addExperience(getExperienceForDrops(laterDrops, block.getWorld().getEnvironment()), SkillExperienceGainEvent.ExperienceSource.HARVEST);
                block.setType(Material.AIR, false);
                for (ItemStack drop : laterDrops)
                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
                ChunkUtil.markBlockSkillValid(block);
            }, yOffset);

        }

    }

}
