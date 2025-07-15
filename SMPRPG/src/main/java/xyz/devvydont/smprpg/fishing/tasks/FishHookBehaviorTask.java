package xyz.devvydont.smprpg.fishing.tasks;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.Component;
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
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged.LureEnchantment;
import xyz.devvydont.smprpg.fishing.utils.FishingPredicates;
import xyz.devvydont.smprpg.fishing.utils.HookEffectOptions;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.Random;

import static xyz.devvydont.smprpg.fishing.utils.FishingPredicates.*;

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
     * How fast the fish should approach the hook during the approach phase.
     */
    private static final double FISH_APPROACH_SPEED = 0.15;

    /**
     * How wildly random the fish should act while approaching the hook.
     */
    private static final double FISH_APPROACH_VOLATILITY = 0.1;

    /**
     * The maximum amount of time in ticks that we can spend waiting for a fish to enter the "lured" state.
     * This is before the Lure enchantment is applied.
     */
    private static long MAX_FISH_WAIT_TIME = TickTime.seconds(30);

    /**
     * The minimum amount of time in ticks that we can spend waiting for a fish to enter the "lured" state.
     * This is before the Lure enchantment is applied.
     */
    private static long MIN_FISH_WAIT_TIME = TickTime.seconds(5);

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
     * Represents default hook options. This is mostly just a fallback.
     */
    public static final HookEffectOptions DEFAULT_OPTIONS = new HookEffectOptions(
            IMPOSSIBLE_PREDICATE,
            Particle.BUBBLE,
            Particle.BUBBLE_POP,
            Particle.DRIPPING_WATER,
            Sound.ENTITY_GENERIC_SPLASH
    );

    /**
     * Hook options for when we are lava fishing.
     */
    public static final HookEffectOptions LAVA_OPTIONS = new HookEffectOptions(
            LAVA_PREDICATE,
            Particle.LAVA,
            Particle.SMOKE,
            Particle.FLAME,
            Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED
    );

    /**
     * Hook options for when we are void fishing.
     */
    public static final HookEffectOptions VOID_OPTIONS = new HookEffectOptions(
            VOID_PREDICATE,
            Particle.SCULK_SOUL,
            Particle.PORTAL,
            Particle.DRAGON_BREATH,
            Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE
    );

    /**
     * Hook options for when we don't know what we are fishing.
     */
    public static final HookEffectOptions COMPLEX_OPTIONS = new HookEffectOptions(
            FishingPredicates.COMPLEX_PREDICATE,
            Particle.SCULK_SOUL,
            Particle.PORTAL,
            Particle.DRAGON_BREATH,
            Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE
    );

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
    private HookEffectOptions options = DEFAULT_OPTIONS;

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
     * The angle at which the fish will approach. From there, it will be randomly adjusted to simulate sporadic
     * swimming towards the hook.
     */
    private double approachAngle = 0;

    /**
     * How many ticks the player has to interact to trigger a fish catch. This is only positive when in the bite state.
     */
    private long ticksAllowedToCatch = -1;

    /**
     * A modifier for how quickly to lure fish. This is directly tied to the "Lure" enchantment of the fishing rod
     * that performed this cast. This should only be determined once and only once when we cast.
     */
    private int lureFactor = 0;

    public FishHookBehaviorTask(FishHook hook) {
        this.hook = hook;
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
        handleState();
    }

    /**
     * Determines when to spawn in a fish. This is affected by Lure.
     * @return How many ticks until the next fish spawns in.
     */
    public long decideNextFishTime() {
        var ticks = new Random().nextLong(MIN_FISH_WAIT_TIME, MAX_FISH_WAIT_TIME+1);
        ticks -= TickTime.seconds(LureEnchantment.getDecreaseTime(lureFactor));
        return Math.clamp(ticks, TickTime.SECOND, MAX_FISH_WAIT_TIME);
    }

    /**
     * Handles all logic necessary for state changes. This is mainly just tick timings.
     * @param state The state to change to.
     */
    public void setState(HookBehaviorState state) {

        this.state = state;
        switch (this.state) {
            case BOBBING -> this.ticksUntilFish = decideNextFishTime();
            case BITE -> makeFishBite();
            case FISH_APPROACHING -> spawnFish();
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
     * Sets the {@link HookEffectOptions} that this hook will abide by. This includes its location predicate and
     * particle effect options.
     * @param options The options to follow.
     */
    public void setOptions(HookEffectOptions options) {
        this.options = options;
    }

    /**
     * Sets the level of lure to use for this event. This should directly reflect the level of lure of the fishing rod.
     * @param lure The level of lure to use.
     */
    public void setLure(int lure) {
        this.lureFactor = lure;
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
        if (this.options.Predicate() == IMPOSSIBLE_PREDICATE)
            return IFishingRod.FishingFlag.NORMAL;
        if (this.options.Predicate() == LAVA_PREDICATE)
            return IFishingRod.FishingFlag.LAVA;
        if (this.options.Predicate() == VOID_PREDICATE)
            return IFishingRod.FishingFlag.VOID;

        // If we have the complex predicate, it is a bit annoying. We have to determine it from the block.
        return switch (this.hook.getLocation().getBlock().getType()) {
            case AIR -> IFishingRod.FishingFlag.VOID;
            case LAVA -> IFishingRod.FishingFlag.LAVA;
            default -> IFishingRod.FishingFlag.NORMAL;
        };
    }

    /**
     * Get the "effect" options that this task should use. Dependent on the state of the cast.
     * @return The hook effect options.
     */
    public HookEffectOptions getOptionsFromCurrentState() {
        return switch (getFlagFromCurrentState()) {
            case LAVA -> LAVA_OPTIONS;
            case VOID -> VOID_OPTIONS;
            default -> DEFAULT_OPTIONS;
        };
    }

    /**
     * Called from the instance that started this task. Implements the logic for when a player caused a fishing
     * event where this particular {@link FishHook} is involved. No need to check.
     * This is a point of interception for the player to interact with this custom fishhook, i.e. reeling in.
     * @param event The {@link PlayerFishEvent} event.
     */
    public void handleFishingEvent(PlayerFishEvent event) {

        // We actually only ever need to handle custom logic for when the player successfully reels in a fish.
        // We can know if they are reeling in by seeing if they perform the entity reel in event.
        if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))
            return;

        // Simulates a catch!
        if (this.ticksAllowedToCatch >= 0) {
            hook.setHookedEntity(null);
            if (anchor != null)
                hook.teleport(anchor);
            var caughtEvent = new PlayerFishEvent(event.getPlayer(), null, event.getHook(), event.getHand(), PlayerFishEvent.State.CAUGHT_FISH).callEvent();
            if (!caughtEvent) {
                hook.setHookedEntity(hookMount);
                this.setState(HookBehaviorState.BOBBING);
                return;
            }
            this.setState(HookBehaviorState.REELING);  // It's important we update the state before calling the event.
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

        // Spawn a particle to make it seem like a fish is approaching.
        if (this.ticksUntilBite % 2 == 0)
            return;

        var loc = hook.getLocation();
        loc.setY(loc.getY() + 0.5);
        var radius = this.ticksUntilBite * FISH_APPROACH_SPEED;
        var angle = approachAngle + (Math.random() * FISH_APPROACH_VOLATILITY * 2 - FISH_APPROACH_VOLATILITY);
        loc.setX(hook.getX() + Math.cos(angle) * radius);
        loc.setZ(hook.getZ() + Math.sin(angle) * radius);

        if (!this.options.Predicate().check(loc.clone().subtract(0, 1, 0)))
            return;

        new ParticleBuilder(getOptionsFromCurrentState().FishParticle())
                .location(loc)
                .receivers(25)
                .extra(0)
                .spawn();
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
            if (this.getOwner() != null)
                this.getOwner().sendMessage(ComponentUtils.error("This rod does not work in water!"));
            return;
        }

        // Use our predicate to determine the validity of the location.
        if (!this.options.Predicate().check(hook.getLocation()))
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

        // Spawn in some random particles around us.
        new ParticleBuilder(getOptionsFromCurrentState().IdleParticle())
                .location(hook.getLocation().add(0, .5, 0))
                .receivers(25)
                .offset(2, 0, 2)
                .extra(0)
                .spawn();

    }

    /**
     * Spawns a fish. This is called when the ticks to spawn fish reaches 0.
     */
    private void spawnFish() {
        var owner = this.getOwner();
        if (owner == null)
            return;

        var event = new PlayerFishEvent(owner, null, hook, PlayerFishEvent.State.LURED).callEvent();
        if (!event) {
            setState(HookBehaviorState.BOBBING);
            return;
        }

        this.ticksUntilFish = -1;
        this.ticksUntilBite = (int)TickTime.seconds(3);
        this.approachAngle = Math.random() * 2 * Math.PI;
    }

    /**
     * Makes a fish appear to bite. This will make the bobber get pulled down, make a noise, and spawn particles.
     */
    private void makeFishBite() {

        var owner = this.getOwner();
        if (owner == null)
            return;

        var event = new PlayerFishEvent(owner, null, hook, PlayerFishEvent.State.BITE).callEvent();
        if (!event) {
            setState(HookBehaviorState.BOBBING);
            return;
        }

        this.ticksAllowedToCatch = TickTime.seconds(2);
        hook.getWorld().playSound(hook.getLocation(), getOptionsFromCurrentState().SplashSound(), 1, 1);
        if (hookMount == null)
            return;

        new ParticleBuilder(getOptionsFromCurrentState().CatchParticle())
                .location(hook.getLocation().add(0, .5, 0))
                .receivers(25)
                .offset(.25, 0, .25)
                .count(10)
                .spawn();

        hookMount.teleport(hookMount.getLocation().add(0, -1.5, 0));
    }

    /**
     * Using the location of the hook, determine if we are currently at the "surface".
     * The surface is defined as the highest block at or above the fishing hook that satisfies the predicate.
     * @return The block that is considered the surface depending on where we are currently at.
     */
    public boolean isCurrentlySurfaced() {
        // We are surfaced if the block above us doesn't satisfy the predicate.
        return !this.options.Predicate().check(
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
        return hookMount;
    }
}
