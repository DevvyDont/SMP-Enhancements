package xyz.devvydont.smprpg.items.tasks;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.misc.ILocationPredicate;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.time.Duration;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.EMPTY;

/**
 * Monitors a fishhook every tick and causes it to behave like a vanilla hook in water. This task will do everything
 * in its power to make sure a hook behaves like a vanilla hook for lava/void fishing, making this class responsible
 * for the following things:
 * - Monitoring when to enter "void" or "lava" fishing mode
 * - Attach the hook to an armor stand and make the hook appear to bob up and down when in the "waiting for bite" state.
 * - Spawn particles when waiting for fish, and spawn particles to make it look like a fish is about to bite.
 * - Throw {@link org.bukkit.event.player.PlayerFishEvent} events to simulate vanilla behavior.
 */
public class FishHookBehaviorTask extends BukkitRunnable {

    /**
     * Adjusts how slow you want the "bobbing" animation of the fishhook to appear while waiting for a catch.
     * This constant is applied to the sine function that determines fishhook oscillation speed while cast.
     */
    private static final double BOBBING_DAMPENING = 5;

    /**
     * Adjusts how far up and down you want the "bobbing" animation of the fishhook to appear while waiting for a catch.
     * This constant is applied to the sine function that determines vertical fishhook offset while cast.
     */
    private static final double BOBBING_AMPLITUDE = 0.04;

    /**
     * Determine how "random" bobbing will appear to be. We don't want the bobbing to appear perfectly fluid, as that
     * would seem unnatural. We want a value small enough to make movement somewhat random, but not sporadic.
     */
    private static final double BOBBING_RANDOMNESS = 0.025;

    /**
     * How fast the bobber should ascend if the above block is considered a valid anchor location.
     * Think about when you cast a line at the bottom of the ocean. The bobber will ascend to the surface.
     * Similarly, the bobber should "ascend" to the top of a lava pool or to y=0 when void fishing.
     */
    private static final double BOBBING_ASCEND_SPEED = 0.05;

    /**
     * Determines how far to "shove" the bobber relative to the anchor point. This provides some breathing room for
     * the bobber to "bob" without appearing to float above lava.
     */
    private static final double ANCHOR_VERTICAL_OFFSET = -0.2;

    /**
     * Helps manage state for fishing behavior.
     */
    public enum HookBehaviorState {
        SEARCHING,  // Searching for anchor.
        BOBBING,  // Awaiting fish.
        FISH_APPROACHING,  // The fish is coming...
        BITE,  // Fish bite the hook, reel in.
        REELING  // We are successfully reeling in. This is moments before the lifecycle of this task ends.
    }

    /**
     * Meant to be used as a fallback if no suitable predicate exists. Will always fail, allowing for default behavior.
     */
    public static final ILocationPredicate IMPOSSIBLE_PREDICATE = e -> false;

    /**
     * The predicate that lava rods typically use. Checks if the current block is lava.
     */
    public static final ILocationPredicate LAVA_PREDICATE = e -> e.getBlock().getType().equals(Material.LAVA);

    /**
     * The predicate that void rods typically use. Checks if the current block is in the void.
     */
    public static final ILocationPredicate VOID_PREDICATE = e -> e.getWorld().getEnvironment().equals(World.Environment.THE_END) && e.getBlock().getY() <= 0;

    /**
     * Complex predicate allows both lava and void fishing to work at the same time.
     */
    public static final ILocationPredicate COMPLEX_PREDICATE = e -> LAVA_PREDICATE.check(e) || VOID_PREDICATE.check(e);

    /**
     * Creates a new instance, starts running the task every tick, and returns in back. This is essentially just
     * a shortcut to make task instantiation easier.
     * @param hook The {@link FishHook} to simulate behavior for.
     * @return A newly created and now running task. No need to start it.
     */
    public static FishHookBehaviorTask create(FishHook hook) {
        var instance = new FishHookBehaviorTask(hook);
        instance.runTaskTimer(SMPRPG.getInstance(), TickTime.INSTANTANEOUSLY, TickTime.TICK);
        return instance;
    }

    /**
     * The fishhook. The entire task revolves around the state of this.
     */
    private final FishHook hook;

    /**
     * The state of the hook. Somewhat mirrors the vanilla state, but since we can't modify the state of the hook
     * ourselves we store it here. Typically, the hook will have nothing special assigned to it while we are in
     * control of it because Minecraft thinks the hook is just attached to an entity.
     */
    private HookBehaviorState state = HookBehaviorState.SEARCHING;

    /**
     * The predicate that this task is going to abide by. This determines what are valid "fishing spots".
     */
    private ILocationPredicate predicate;

    /**
     * The anchor represents the "anchor" point of the hook. When the hook finds a valid point to attach to,
     * it will bob up and down on this point. Null until the hook finds a valid spot to anchor itself.
     */
    private @Nullable Location anchor = null;

    /**
     * The entity that the hook attaches to so it can fake bobbing physics when an anchor is found.
     * Before you ask questions, no you cannot modify the hook manually by calling teleport() and setPhysics(false) or
     * anything like that. I've tried everything :(
     */
    private @Nullable ArmorStand hookMount = null;

    /**
     * Used as an input to a sine function to simulate "up and down" bobbing while waiting for a catch.
     * The value of this doesn't really mean anything, we just need a number increasing at a constant rate
     * to use as an input to a sine function.
     */
    private int bobTicks = 0;

    /**
     * How many ticks until the bobber is going to "spawn" a fish.
     */
    private long ticksUntilFish = Integer.MAX_VALUE;

    /**
     * How many ticks until the bobber is going to register a "bite".
     */
    private long ticksUntilBite = Integer.MAX_VALUE;

    /**
     * How many ticks the player has to interact to trigger a fish catch. This is only positive when in the bite state.
     */
    private long ticksAllowedToCatch = -1;

    public FishHookBehaviorTask(FishHook hook) {
        this.hook = hook;
        this.predicate = IMPOSSIBLE_PREDICATE;
    }

    @Override
    public void run() {

        // If the hook is gone, we can kill the task. The player caused the hook to vanish from the world.
        if (!hook.isValid()) {
            this.cleanupHookMount();
            cancel();
            return;
        }

        // Handle the state that this task is currently in. Runs every tick.
        sendTitle(EMPTY, Component.text(this.state.toString()));
        handleState();
    }

    /**
     * Handles all logic necessary for state changes. This is mainly just tick timings.
     * @param state The state to change to.
     */
    public void setState(HookBehaviorState state) {

        this.state = state;
        switch (this.state) {
            case BOBBING -> {
                this.ticksUntilFish = TickTime.seconds(5);
            }
            case BITE -> {
                makeFishBite();
            }
            case FISH_APPROACHING -> {
                spawnFish();
            }
            case SEARCHING -> {
                this.ticksUntilBite = Integer.MAX_VALUE;
                this.ticksUntilFish = Integer.MAX_VALUE;
                this.ticksAllowedToCatch = -1;
            }
            case REELING -> {
                this.ticksAllowedToCatch = -1;
                this.ticksUntilFish = Integer.MAX_VALUE;
                this.ticksUntilBite = Integer.MAX_VALUE;
                this.cleanupHookMount();
                for (var flag : IFishingRod.FishingFlag.values())
                    this.hook.removeMetadata(flag.toString(), SMPRPG.getInstance());
                this.hook.setMetadata(this.getFlagFromCurrentState().toString(), new FixedMetadataValue(SMPRPG.getInstance(), true));
            }
        }
    }

    /**
     * Sets the {@link ILocationPredicate} that the fishing hook logic will follow.
     * @param predicate The predicate that determines valid fishing hook locations.
     */
    public void setPredicate(ILocationPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Depending on the state of this task, perform some action.
     */
    public void handleState() {
        switch (state) {
            case SEARCHING -> handleSearch();
            case BOBBING -> handleBobbing();
            case FISH_APPROACHING -> handleFishApproaching();
            case BITE -> handleFishBite();
        }
    }

    /**
     * Resolves the fishing flag this fishing event is performing from the state of the predicate.
     * We need this logic in place so we can determine loot pools correctly.
     * @return The {@link xyz.devvydont.smprpg.items.interfaces.IFishingRod.FishingFlag} this task is operating on.
     */
    public IFishingRod.FishingFlag getFlagFromCurrentState() {
        if (this.predicate == IMPOSSIBLE_PREDICATE)
            return IFishingRod.FishingFlag.NORMAL;
        if (this.predicate == LAVA_PREDICATE)
            return IFishingRod.FishingFlag.LAVA;
        if (this.predicate == VOID_PREDICATE)
            return IFishingRod.FishingFlag.VOID;

        // If we have the complex predicate, it is a bit annoying. We have to determine it from the block.
        return switch (this.hook.getLocation().getBlock().getType()) {
            case AIR -> IFishingRod.FishingFlag.VOID;
            case LAVA -> IFishingRod.FishingFlag.LAVA;
            default -> IFishingRod.FishingFlag.NORMAL;
        };
    }

    /**
     * Called from the instance that started this task. Implements the logic for when a player caused a fishing
     * event where this particular {@link FishHook} is involved. No need to check.
     * This is a point of interception for the player to interact with this custom fishhook, i.e. reeling in.
     * @param event
     */
    public void handleFishingEvent(PlayerFishEvent event) {

        // We actually only ever need to handle custom logic for when the player successfully reels in a fish.
        // We can know if they are reeling in by seeing if they perform the entity reel in event.
        if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))
            return;

        // Simulates a catch!
        if (this.ticksAllowedToCatch >= 0) {
            this.setState(HookBehaviorState.REELING);  // It's important we update the state before calling the event.
            new PlayerFishEvent(event.getPlayer(), null, event.getHook(), event.getHand(), PlayerFishEvent.State.CAUGHT_FISH).callEvent();
        }
    }

    /**
     * Logic ran every tick while we are searching for an anchor spot.
     * Checks if our current location is suitable. Once we are anchored, we can switch to the bobbing state!
     */
    private void handleSearch() {
        attemptAnchor();
        if (this.isAnchored())
            this.setState(HookBehaviorState.BOBBING);
    }

    /**
     * Logic ran every tick while we are bobbing. In this state we are "idling", waiting for a fish to spawn in.
     * We control the amount of ticks until a fish spawns in.
     */
    private void handleBobbing() {
        this.ticksUntilFish--;
        this.doIdleBob();
        if (this.ticksUntilFish <= 0)
            this.setState(HookBehaviorState.FISH_APPROACHING);
    }

    /**
     * Logic ran every tick while a fish is approaching. We still need to bob, but we also now need to spawn particles
     * to emulate a fish swimming to the bobber.
     */
    private void handleFishApproaching() {
        this.ticksUntilBite--;
        this.doIdleBob();
        if (this.ticksUntilBite <= 0)
            this.setState(HookBehaviorState.BITE);
    }

    /**
     * Logic ran every tick while the fish is biting. During this state, the player can reel in to catch the fish!
     * If they miss the window, we reset back to bobbing.
     */
    private void handleFishBite() {
        this.ticksAllowedToCatch--;
        var mount = getOrCreateMount();
        mount.teleport(mount.getLocation().add(0, (Math.random()-.5) * .2, 0));
        if (this.ticksAllowedToCatch <= 0) {
            this.unanchor();
            this.setState(HookBehaviorState.SEARCHING);
            var owner = this.getOwner();
            if (owner != null)
                new PlayerFishEvent(owner, null, hook, PlayerFishEvent.State.FAILED_ATTEMPT).callEvent();
        }
    }

    /**
     * Removes the armor stand that the hook attaches to from the world.
     */
    private void cleanupHookMount() {
        this.hook.setHookedEntity(null);
        if (this.hookMount != null)
            this.hookMount.remove();
        this.hookMount = null;
    }

    /**
     * Retrieve the player that is responsible for this hook, if they exist. This will only be null if the entity
     * who "fired" this fishing rod is not a player, or if somehow this hook was created by unforeseen circumstances.
     * @return The player who owns this hook. Can be null.
     */
    private @Nullable Player getOwner() {
        if (hook.getShooter() instanceof Player player)
            return player;
        return null;
    }

    /**
     * Checks if this hook is currently anchored on a point.
     * @return true if we are anchored.
     */
    private boolean isAnchored() {
        return this.anchor != null;
    }

    /**
     * "Anchors" this hook to a location. This is accomplished by storing the desired location within the block.
     * @param location The location to anchor to.
     */
    public void anchor(Location location) {

        // Now that we found it, we want our anchor point to be the top of the lava. It is important to note that
        // the "top" of the block is actually 1 block above it, since the Y coordinate is actually the bottom.
        anchor = hook.getLocation().set(
                hook.getLocation().getX(),
                location.getBlock().getY()+1,
                hook.getLocation().getZ()
        );

        // Now actually shift the anchor to be a bit "inside" the lava, since we want there to be room for bobbing.
        anchor.add(0, ANCHOR_VERTICAL_OFFSET, 0);
        this.bobTicks = 0;
    }

    /**
     * Unanchors this hook. This will cause the hook to revert to vanilla physics behavior.
     */
    public void unanchor() {
        this.anchor = null;
    }

    /**
     * Runs if the current anchor spot is not valid or hasn't been found yet.
     * Analyzes the state of the hook and determines if this is a proper spot to anchor.
     */
    private void attemptAnchor() {

        // If we touch water, and we don't have the water flag, we die.
        if (!this.hook.hasMetadata(IFishingRod.FishingFlag.NORMAL.toString()) && this.hook.getLocation().getBlock().getType().equals(Material.WATER)) {
            this.hook.remove();
            this.hook.getWorld().playSound(this.hook.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1.25f);
            return;
        }

        // Use our predicate to determine the validity of the location.
        if (!this.predicate.check(hook.getLocation()))
            return;

        // Check if we are on the surface. If we aren't, we have to ascend a bit. The armor stand also needs to exist.
        var mount = this.getOrCreateMount();
        if (!this.isCurrentlySurfaced()) {
            mount.teleport(mount.getLocation().add(0, BOBBING_ASCEND_SPEED, 0));
            return;
        }

        // We are surfaced! We can anchor.
        this.anchor(hook.getLocation());
    }


    /**
     * Makes the hook appear as if it is bobbing.
     */
    private void doIdleBob() {

        // If the anchor or hook mount doesn't exist, we can't do it.
        if (this.hookMount == null || this.anchor == null)
            return;

        this.bobTicks++;

        // Generate a Y offset using trig and randomness. This will make the bobber appear to bob up and down.
        var yOffset = Math.sin(bobTicks / BOBBING_DAMPENING) * BOBBING_AMPLITUDE;
        yOffset += (Math.random() - .5) * BOBBING_RANDOMNESS;

        this.hookMount.teleport(this.anchor.clone().add(0, yOffset, 0));
    }

    /**
     * Spawns a fish. This is called when the ticks to spawn fish reaches 0.
     */
    private void spawnFish() {
        this.ticksUntilFish = -1;
        this.ticksUntilBite = (int)TickTime.seconds(3);
        var owner = this.getOwner();
        if (owner != null)
            new PlayerFishEvent(owner, null, hook, PlayerFishEvent.State.LURED).callEvent();
    }

    /**
     * Makes a fish appear to bite. This will make the bobber get pulled down, make a noise, and spawn particles.
     */
    private void makeFishBite() {
        this.ticksAllowedToCatch = TickTime.seconds(2);
        hook.getWorld().playSound(hook.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1, 1);
        if (hookMount == null)
            return;

        new ParticleBuilder(Particle.LAVA)
                .location(hook.getLocation().add(0, .5, 0))
                .receivers(25)
                .offset(.25, 0, .25)
                .count(10)
                .spawn();

        hookMount.teleport(hookMount.getLocation().add(0, -1.5, 0));
        var owner = this.getOwner();
        if (owner != null)
            new PlayerFishEvent(owner, null, hook, PlayerFishEvent.State.BITE).callEvent();
    }

    /**
     * Using the location of the hook, determine if we are currently at the "surface".
     * The surface is defined as the highest block at or above the fishing hook that satisfies the predicate.
     * @return The block that is considered the surface depending on where we are currently at.
     */
    public boolean isCurrentlySurfaced() {
        // We are surfaced if the block above us doesn't satisfy the predicate.
        return !this.predicate.check(
                hook.getLocation().getBlock().getRelative(BlockFace.UP).getLocation().toCenterLocation()
        );
    }

    /**
     * Gets the instance of the armor stand that the hook can mount to, or returns it if it already exists.
     * @return The {@link ArmorStand} for the hook to attach to.
     */
    private ArmorStand getOrCreateMount() {
        if (this.hookMount != null)
            return this.hookMount;
        this.hookMount = createHookMount();
        return this.hookMount;
    }

    /**
     * Creates the mount for the hook to attach to. This method will also assign the instance variable and return it.
     * Deletes the old mount if it exists.
     * @return an ArmorStand instance that is ready for the hook to be attached to.
     */
    private ArmorStand createHookMount() {

        if (hookMount != null)
            hookMount.remove();

        hookMount = hook.getWorld().spawn(hook.getLocation().add(0, 0, 0), ArmorStand.class, as -> {
            as.setInvulnerable(true);
            as.setMarker(true);
            as.setGravity(false);
            as.setPersistent(false);
            as.setInvisible(true);
        });

        // Attach the armor stand to the hook.
        hook.setHookedEntity(hookMount);
        sendTitle(EMPTY, ComponentUtils.create("ATTACH!", GREEN));
        return hookMount;
    }

    /**
     * Shows a title to whoever threw the hook. Used for debugging, since title showing is a bit verbose.
     * @param title The title of the title to send.
     * @param subtitle The subtitle of the title to send.
     */
    private void sendTitle(Component title, Component subtitle) {
        var d0 = Duration.ofSeconds(0);
        var d1 = Duration.ofSeconds(1);
        if (getOwner() != null)
            getOwner().showTitle(Title.title(title, subtitle, Title.Times.times(d0, d1, d0)));
    }
}
