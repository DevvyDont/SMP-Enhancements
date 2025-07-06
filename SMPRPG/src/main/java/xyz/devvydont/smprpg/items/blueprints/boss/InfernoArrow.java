package xyz.devvydont.smprpg.items.blueprints.boss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ChatService;
import xyz.devvydont.smprpg.services.DropsService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class InfernoArrow extends CustomItemBlueprint implements IHeaderDescribable, ISellable, Listener {

    public enum InfernoSpawnResult {
        SUCCESS("Success!"),
        WRONG_DIMENSION("This only works in the Nether!"),
        NO_LAVA("This arrow must be shot into lava!"),
        NO_ROOM("There is not enough space!"),
        NOT_ENABLED("You are not prepared for this encounter yet...")
        ;

        final String message;
        InfernoSpawnResult(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // How much should the arrow velocity be dampened by when colliding with a block and reflecting?
    private static final double ARROW_VELOCITY_BLOCK_DAMPENING = .2;
    private static final double ARROW_VELOCITY_ENTITY_DAMPENING = .5;

    private static final boolean ALLOW_SPAWNING = true;

    public InfernoArrow(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {

        Component bossName = ComponentUtils.create("Infernal Phoenix", NamedTextColor.DARK_PURPLE);
        Component instructions = ComponentUtils.create("shot in lava", NamedTextColor.GOLD);
        // If boss spawns aren't enabled, obfuscate the instructions
        if (!ALLOW_SPAWNING){
            bossName = bossName.decorate(TextDecoration.OBFUSCATED);
            instructions = instructions.decorate(TextDecoration.OBFUSCATED);
        }

        List<Component> components = new ArrayList<>();
        components.add(ComponentUtils.create("Used to summon an ")
                .append(bossName));
        components.add(ComponentUtils.create("when ")
                .append(instructions)
                .append(ComponentUtils.create(" in the "))
                .append(ComponentUtils.create("Nether", NamedTextColor.RED)));
        return components;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public int getWorth(ItemStack item) {
        return 20_000 * item.getAmount();
    }

    /**
     * Checks if a certain block in the world is a valid spot to spawn an inferno boss.
     *
     * @param block The block in the world to check
     * @return an enum representing the state of the validity of the spawn location, SUCCESS if we should allow it
     */
    private InfernoSpawnResult checkLocationValidity(Block block) {

        // Are we in the nether?
        if (!block.getWorld().getEnvironment().equals(World.Environment.NETHER))
            return InfernoSpawnResult.WRONG_DIMENSION;

        // Is this block submerged in lava?
        if (!block.getType().equals(Material.LAVA))
            return InfernoSpawnResult.NO_LAVA;

        // Is there room to spawn this boss?
        // todo figure out some raycasting checks maybe to determine the validity of the spot?
        // todo for now just see if we can fit 25 blocks above us for room for expansion
        for (int y = 1; y <= 40; y++)
            if (!block.getRelative(BlockFace.UP, 10).getType().equals(Material.AIR))
                return InfernoSpawnResult.NO_ROOM;
        // end todo

        // Is the boss enabled?
        if (!ALLOW_SPAWNING)
            return InfernoSpawnResult.NOT_ENABLED;

        // Passed all checks!
        return InfernoSpawnResult.SUCCESS;
    }

    /*
     * Decide what to do when this arrow is initially shot. For now, we just add a cute little glow :p
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onShootInfernoArrow(EntityShootBowEvent event) {

        // Is the projectile our arrow?
        if (!(event.getProjectile() instanceof SpectralArrow arrow))
            return;

        if (!isItemOfType(arrow.getItemStack()))
            return;

        ItemStack item = arrow.getItemStack();
        item.editMeta( meta -> {
            if (event.getEntity() instanceof Player player)
                SMPRPG.getService(DropsService.class).setOwner(meta, player);
            SMPRPG.getService(DropsService.class).setExpiryTimestamp(meta, System.currentTimeMillis() + DropsService.getMillisecondsUntilExpiry(getDefaultRarity()));
        });
        arrow.setItemStack(item);
        SMPRPG.getService(DropsService.class).getTeam(ItemRarity.LEGENDARY).addEntity(arrow);  // give it a glow :p
        arrow.setGlowing(true);
        arrow.setHitSound(Sound.BLOCK_AMETHYST_BLOCK_BREAK);
        arrow.setInvulnerable(true);
    }

    /*
     * We need to disallow our inferno arrow from being lost to being shot at enemies + in lava to burn
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInfernoArrowCollideWithEntity(ProjectileHitEvent event) {


        // Is the projectile our arrow?
        if (!(event.getEntity() instanceof SpectralArrow arrow))
            return;

        if (!isItemOfType(arrow.getItemStack()))
            return;

        // This arrow is an inferno arrow. Decide what to do what an inferno arrow collides with something.
        // If we hit an entity, this interaction is completely nullified.
        if (event.getHitEntity() != null) {
            event.setCancelled(true);
            arrow.setVelocity(arrow.getVelocity().multiply(-1).multiply(ARROW_VELOCITY_ENTITY_DAMPENING));
            arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1.5f);
            return;
        }

        // If we didn't hit a block, then we can't handle any additional logic. (Player shot us into the void?)
        Block block = event.getHitBlock();
        if (block == null)
            return;

        // Check if we should refund the arrow back to the player because they used it incorrectly.
        InfernoSpawnResult result = checkLocationValidity(arrow.getLocation().getBlock());

        // Is this location invalid?
        if (!result.equals(InfernoSpawnResult.SUCCESS)){
            // Delete the arrow entity and spawn the corresponding item stack so they don't lose it.
            Item drop = block.getWorld().dropItemNaturally(arrow.getLocation(), arrow.getItemStack());
            drop.setVelocity(arrow.getVelocity().multiply(-1).multiply(ARROW_VELOCITY_BLOCK_DAMPENING));
            arrow.remove();
            if (event.getEntity().getShooter() instanceof Player player)
                player.sendMessage(ComponentUtils.error(result.getMessage()));
            return;
        }

        // Was this a player?
        if (!(event.getEntity().getShooter() instanceof Player player))
            return;

        var boss = SMPRPG.getService(EntityService.class).spawnCustomEntity(CustomEntityType.INFERNAL_PHOENIX, arrow.getLocation());
        if (boss == null || !boss.getEntity().isValid()) {
            player.sendMessage(ComponentUtils.error("Something went wrong and the boss was not spawned."));
            return;
        }

        arrow.remove();

        // Summon the boss!
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, .3f);
        var name = SMPRPG.getService(ChatService.class).getPlayerDisplay(player);

        Bukkit.broadcast(ComponentUtils.merge(
                name,
                ComponentUtils.create(" summoned an"),
                boss.getNameComponent(),
                ComponentUtils.SYMBOL_EXCLAMATION
        ));

        for (int i = 0; i < 5; i++)
            Bukkit.getScheduler().runTaskLater(SMPRPG.getInstance(), () -> boss.getEntity().getWorld().createExplosion(boss.getEntity().getLocation().add(0, 5, 0), 5.0f, false, false), i*3);
    }
}
