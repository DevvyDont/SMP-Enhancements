package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.fishing.events.FishingLootGenerateEvent;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.ItemService;

/**
 * Responsible for awarding fishing experience for general fishing events.
 */
public class FishingExperienceListener implements Listener {

    final SMPRPG plugin;

    public FishingExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Get the multiplier of skill experience to apply to a skill experience reward for fishing up a fish.
     * @param rarity The rarity of the fish.
     * @return An experience multiplier.
     */
    private int getFishRarityMultiplier(ItemRarity rarity) {
        var x = rarity.ordinal() + 1;
        return x * x;
    }

    /**
     * Simply listen for when a fish loot event happens. Fishing EXP is on the reward itself.
     * We should also give a bonus for fishing higher quality fish.
     * @param event The {@link FishingLootGenerateEvent} event that provides us with relevant context.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void __onFish(FishingLootGenerateEvent event) {

        // All we need to do is give the player experience.
        var exp = event.getCalculationResult().Reward().Element().getFishingExperience();

        // As a bonus, let's give the player extra experience if they fished up a rare fish.
        var isFish = event.getCalculationResult().Type().Element().equals(FishingLootType.FISH);
        if (isFish && event.getCaughtEntity() instanceof Item item)
            exp *= getFishRarityMultiplier(ItemService.blueprint(item.getItemStack()).getRarity(item.getItemStack()));

        // Award.
        var player = SMPRPG.getService(EntityService.class).getPlayerInstance(event.getFishingContext().getPlayer());
        player.getFishingSkill().addExperience(exp, SkillExperienceGainEvent.ExperienceSource.FISH);

    }


}
