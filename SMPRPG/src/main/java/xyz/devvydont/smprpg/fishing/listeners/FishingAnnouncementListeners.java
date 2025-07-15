package xyz.devvydont.smprpg.fishing.listeners;

import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.fishing.SeaCreature;
import xyz.devvydont.smprpg.fishing.events.FishingLootGenerateEvent;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;
import xyz.devvydont.smprpg.fishing.loot.SeaCreatureFishingLoot;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.blueprints.fishing.FishBlueprint;
import xyz.devvydont.smprpg.services.ChatService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.*;

/**
 * Handles instances where we should announce fishing events. Mainly, rare drops and rare catches.
 * The reason this is separated from normal rare drop events, is due to the fact that fishing works much more differently
 * and EVERY drop in fishing has a "low drop rate" due to the item pool nature being so complex.
 */
public class FishingAnnouncementListeners extends ToggleableListener {

    /**
     * Default announcement message generation behavior. This only applies to events where no succeeding events after
     * this make any modifications. Generates default component for when something is caught, based on the calculation
     * result.
     * @param event The {@link FishingLootGenerateEvent} event that provides relevant context.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    private void __onGenerateDefaultAnnouncement(final FishingLootGenerateEvent event) {

        // Handle the simple cases first. Trash does nothing.
        if (event.getCalculationResult().Type().Element().equals(FishingLootType.JUNK))
            return;

        // Sea creature? We just alert player.
        if (event.getCalculationResult().Reward().Element() instanceof SeaCreatureFishingLoot) {
            var entity = SMPRPG.getService(EntityService.class).getEntityInstance(event.getCaughtEntity());
            event.setAnnounceMode(FishingLootGenerateEvent.AnnounceMode.PLAYER);
            event.setComponentOverride(alert(merge(
                    create("WHOA!!!", SeaCreature.NAME_COLOR).decoration(TextDecoration.BOLD, true),
                    create(" You fished up a "),
                    entity.getPowerComponent().append(SPACE).append(entity.getNameComponent()).hoverEvent(merge(
                            create("You had a "),
                            create(String.format("%.2f%%", event.getCalculationResult().probability()*100), LIGHT_PURPLE),
                            create(" chance of fishing this up!")
                    )),
                    create("!"),
                    create(" Defeat it!", RED).decoration(TextDecoration.BOLD, true)
            ), GOLD));
            return;
        }

        // We should have an item. Just eliminate any other cases for now.
        if (!(event.getCaughtEntity() instanceof Item item))
            return;

        // The rarity is super important in telling us how to display this item.
        var itemStack = item.getItemStack();
        var blueprint = SMPRPG.getService(ItemService.class).getBlueprint(itemStack);
        var rarity = blueprint.getRarity(itemStack);

        // Honestly, don't even bother with common items. Treasure items should never be common anyway...
        if (rarity.equals(ItemRarity.COMMON))
            return;

        // Work out the audience based on how rare this was. UNCOMMON we can just tell the player.
        if (rarity.ordinal() >= ItemRarity.UNCOMMON.ordinal())
            event.setAnnounceMode(FishingLootGenerateEvent.AnnounceMode.PLAYER);

        // EPIC probably deserves a server announcement.
        if (rarity.ordinal() >= ItemRarity.EPIC.ordinal())
            event.setAnnounceMode(FishingLootGenerateEvent.AnnounceMode.SERVER);

        // This is either treasure or fish. We determine the prefix based on the type.
        var prefix = event.getCalculationResult().Type().Element().equals(FishingLootType.TREASURE) ?
                "TREASURE!" :
                "CATCH!";

        // This is probably silly, but if it's legendary add more exclamation points.
        if (rarity.ordinal() >= ItemRarity.LEGENDARY.ordinal())
            prefix = prefix + "!!";

        // Pre-pend the rarity and add a color.
        var prefixComponent = create(rarity.name() + " " + prefix, rarity.color).decoration(TextDecoration.BOLD, true);

        // Work out the name to use. This is important depending on the audience.
        var name = event.getAnnounceMode().equals(FishingLootGenerateEvent.AnnounceMode.SERVER) ?
                SMPRPG.getService(ChatService.class).getPlayerDisplay(event.getFishingContext().getPlayer()) :
                create("You", AQUA);

        // Make the message.
        var itemComponent = blueprint.getNameComponent(itemStack).hoverEvent(itemStack.asHoverEvent());

        // Consider the rarity probability if it was a fish.
        var rarityProbability = 1.0;
        if (blueprint instanceof FishBlueprint)
            rarityProbability *= FishBlueprint.probability(rarity);

        var chance = create(String.format("(%.2f%%)", event.getCalculationResult().probability() * rarityProbability * 100), DARK_GRAY);
        var message = alert(merge(
                prefixComponent,
                SPACE,
                name,
                create(" fished up a "),
                itemComponent,
                create("! "),
                chance
        ), GOLD);

        event.setComponentOverride(message);
    }

    /**
     * The standard announcement for fishing up a rare item. Simply checks for component overrides, or generates
     * default ones. This is what should come last in execution time.
     * @param event The {@link FishingLootGenerateEvent} that provides relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onFishedUpRareItem(final FishingLootGenerateEvent event) {

        // First, determine the audience from the announcement type.
        Audience audience = Audience.empty();
        if (event.getAnnounceMode().equals(FishingLootGenerateEvent.AnnounceMode.SERVER))
            audience = Audience.audience(Bukkit.getOnlinePlayers());
        else if (event.getAnnounceMode().equals(FishingLootGenerateEvent.AnnounceMode.PLAYER))
            audience = event.getFishingContext().getPlayer();

        // Next, figure out what we are actually sending.
        var component = event.getComponentOverride() != null ?
                event.getComponentOverride() :
                null;  // todo, default component

        // Send component if one existed.
        if (component != null) {
            audience.sendMessage(component);
            audience.playSound(Sound.sound().type(SoundEventKeys.ENTITY_CHICKEN_EGG).build());
        }
    }

}
