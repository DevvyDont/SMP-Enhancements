package xyz.devvydont.smprpg.fishing.listeners;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.fishing.calculator.FishLootCalculator;
import xyz.devvydont.smprpg.fishing.events.FishingLootGenerateEvent;
import xyz.devvydont.smprpg.fishing.tasks.FishHookBehaviorTask;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.items.interfaces.IFishingRod;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;
import xyz.devvydont.smprpg.util.persistence.KeyStore;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Implements the logic and behavior override for fishing to make it functional.
 * Not only do we need to fully override the loot pool by replacing item entities for loot, but we also need to
 * implement the logic for lava and void fishing to work.
 * The idea with lava fishing is when a bobber flies out into the world, we check it every tick and wait for it
 * to become engulfed in lava. When that occurs, we can attach it to an invisible armor stand and make it appear
 * like its bobbing, and simulate normal water fishing behavior by spawning particles, playing sounds and throwing
 * {@link org.bukkit.event.player.PlayerFishEvent} events manually. Doing it this way will make sure our plugin
 * (and any other ones in fact!) will react to fishing events as if vanilla Minecraft supported lava fishing.
 */
public class FishingBehaviorListeners extends ToggleableListener {

    /**
     * Maps fishing hook entity UUIDs to fishing hook behavior tasks. Used so we can track fishing hook events.
     */
    private final Map<UUID, FishHookBehaviorTask> tasks = new HashMap<>();

    /**
     * Handle for when a player hooks up something. We need to completely override the entity that is attached since
     * our usage of the luck stat completely breaks vanilla fishing drops. We are going to accomplish this by using
     * our fishing calculator and supplying it with a fishing context to do all the work for us.
     * @param event The {@link PlayerFishEvent} that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void __onFish(PlayerFishEvent event) {

        // If the player didn't catch a fish, we don't care. Vanilla is the same up until this point.
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;

        // We caught a fish! This is where our plugin completely takes over. Generate context and calculate.
        var ctx = new FishingContext(event);
        var calculator = new FishLootCalculator();
        var loot = calculator.roll(ctx);

        // This is where things get fucky... We don't have a simple event.setCaught() method.
        // Delete the entity on the fishing hook, and attach a newly generated one.
        if (event.getCaught() != null)
            event.getCaught().remove();

        // Let the plugin know and/or make modifications.
        var _new = loot.Reward().Element().generate(ctx);
        if (_new == null)
            return;
        var fishLootEvent = new FishingLootGenerateEvent(ctx, _new, loot);
        if (!fishLootEvent.callEvent()){
            _new.remove();
            return;
        }

        // The event did not cancel. Did we override an entity? If so, remove the old one and add the new one.
        if (!fishLootEvent.getCaughtEntity().equals(_new)) {
            _new.remove();
            _new = fishLootEvent.getCaughtEntity();
        }

        event.getHook().setHookedEntity(_new);

        // This is also very strange, but if this is a water rod, we need to pull the entity to simulate normal fish loot physics. No idea why.
//        if (ctx.getFlags().contains(IFishingRod.FishingFlag.NORMAL))
//            event.getHook().pullHookedEntity();
        event.getHook().pullHookedEntity();
    }

    /**
     * Only handle the case where we start a cast with a lava rod. When this happens, we can attach a task to
     * the fishhook that monitors its behavior every tick.
     * @param event The {@link PlayerFishEvent} that provides us with relevant context.
     */
    @EventHandler(ignoreCancelled = true)
    private void __onStartFishWithSpecialRod(PlayerFishEvent event) {

        // If this hook is associated with a task already, offset the logic to the task. Not this.
        var hook = tasks.get(event.getHook().getUniqueId());
        if (hook != null) {
            hook.handleFishingEvent(event);
            return;
        }

        // We know we don't have a task. If a normal fishing event fires, this is probably a safe time to
        // force tag the fishing hook as a normal fishing hook so it doesn't catch lava/void fish in normal circumstances.
        if (event.getState().equals(PlayerFishEvent.State.LURED)) {
            for (var flag : IFishingRod.FishingFlag.values())
                event.getHook().removeMetadata(flag.toString(), SMPRPG.getInstance());
            event.getHook().setMetadata(IFishingRod.FishingFlag.NORMAL.toString(), new FixedMetadataValue(SMPRPG.getInstance(), true));
        }

        // Filter out events where we aren't casting a fishing line to start the process.
        if (!event.getState().equals(PlayerFishEvent.State.FISHING))
            return;

        // Filter out events where the fishing rod is not a special rod. This has multiple steps.
        if (event.getHand() == null)
            return;

        var fishingRod = event.getPlayer().getInventory().getItem(event.getHand());
        if (fishingRod.getType().equals(Material.AIR))
            return;

        var blueprint = ItemService.blueprint(fishingRod);
        if (!(blueprint instanceof IFishingRod rodBlueprint))
            return;

        // We have two jobs. Start a ticking behavior task to simulate fishing in contexts where fishing isn't supported,
        // and tag the fishing hook with the flags that it's allowed to fish for.
        for (var flag : rodBlueprint.getFishingFlags())
            event.getHook().setMetadata(flag.name(), new FixedMetadataValue(SMPRPG.getInstance(), true));

        // If this rod is nothing special, we can simply ignore it. It will act like a vanilla fishing rod.
        if (rodBlueprint.getFishingFlags().contains(IFishingRod.FishingFlag.NORMAL) && rodBlueprint.getFishingFlags().size() == 1)
            return;

        hook = FishHookBehaviorTask.create(event.getHook());

        // Customize the hook's behavior to suit the fishing rod depending on the flags present.
        if (rodBlueprint.getFishingFlags().contains(IFishingRod.FishingFlag.LAVA) && rodBlueprint.getFishingFlags().contains(IFishingRod.FishingFlag.VOID))
            hook.setOptions(FishHookBehaviorTask.COMPLEX_OPTIONS);
        else if (rodBlueprint.getFishingFlags().contains(IFishingRod.FishingFlag.LAVA))
            hook.setOptions(FishHookBehaviorTask.LAVA_OPTIONS);
        else if (rodBlueprint.getFishingFlags().contains(IFishingRod.FishingFlag.VOID))
            hook.setOptions(FishHookBehaviorTask.VOID_OPTIONS);

        hook.setLure(fishingRod.getEnchantmentLevel(Enchantment.LURE));

        tasks.put(event.getHook().getUniqueId(), hook);
    }

    /**
     * Removes stale hooks from the world.
     * @param event The {@link EntityRemoveFromWorldEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onHookDeath(EntityRemoveFromWorldEvent event) {
        var task = tasks.remove(event.getEntity().getUniqueId());
        if (task != null)
            task.cancel();
    }

    /**
     * Once again, it's possible that people can abuse our attribute system by dual-wielding fishing rods.
     * When someone casts a line while holding two fishing rods, we should obliterate their fishing catch chance.
     * Honestly, we can just run this for every fishing event. This will ensure we have proper attribute application
     * at any point in the fishing process.
     * @param event The {@link PlayerFishEvent} event that gives us context.
     */
    @EventHandler(priority = EventPriority.LOW)
    private void __onAttemptFishWithTwoFishingRods(PlayerFishEvent event) {
        // Remove the nerf, apply it if they are breaking the rules, and punish them by making bite time long or cancelling the event.
        var rating = AttributeService.getInstance().getOrCreateAttribute(event.getPlayer(), AttributeWrapper.FISHING_RATING);
        rating.removeModifier(KeyStore.FISHING_ATTRIBUTE_DUAL_WIELD_NERF);
        var main = event.getPlayer().getInventory().getItemInMainHand();
        var off = event.getPlayer().getInventory().getItemInOffHand();
        if (ItemService.blueprint(main) instanceof IFishingRod && ItemService.blueprint(off) instanceof IFishingRod) {
            rating.addModifier(new AttributeModifier(KeyStore.FISHING_ATTRIBUTE_DUAL_WIELD_NERF, -.9, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            event.getPlayer().sendMessage(ComponentUtils.error("You seem to be struggling handling two fishing rods at once... Are you sure you are gonna catch something like that??"));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_HURT, .5f, 1.25f);
            event.getHook().setWaitTime((int) TickTime.minutes(1), (int) TickTime.minutes(5));
            if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))
                event.setCancelled(true);
        }
        rating.save(event.getPlayer(), AttributeWrapper.FISHING_RATING);
    }

}
