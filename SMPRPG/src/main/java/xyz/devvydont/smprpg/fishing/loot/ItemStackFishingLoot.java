package xyz.devvydont.smprpg.fishing.loot;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.fishing.loot.requirements.FishingLootRequirement;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.fishing.FishBlueprint;
import xyz.devvydont.smprpg.services.DropsService;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.Random;

/**
 * Represents a loot item that is an item. Is essentially just an item you can fish up.
 */
public class ItemStackFishingLoot extends FishingLootBase {

    private static final Random RNG = new Random();

    private final ItemStack item;
    private final int minAmount;
    private final int maxAmount;

    private final String key;

    public ItemStackFishingLoot(ItemStack reward, int min, int max, int weight, int fishingExperience, int minecraftExperience, Collection<FishingLootRequirement> requirements) {
        super(weight, fishingExperience, minecraftExperience, requirements);
        this.item = reward;
        this.minAmount = min;
        this.maxAmount = max;

        key = ItemService.blueprint(reward) instanceof CustomItemBlueprint custom ? custom.getCustomItemType().getKey() : reward.getType().name().toLowerCase();
    }

    @Override
    public Entity generate(FishingContext ctx) {

        // Generate the item and flag it as owned by the player so drop protection applies to it.
        var reward = item.clone();

        // Post-processing for fish items. It needs a rarity.
        if (SMPRPG.getService(ItemService.class).getBlueprint(reward) instanceof FishBlueprint blueprint)
            blueprint.setRarity(reward, blueprint.pickRandomRarity());

        reward.setAmount(RNG.nextInt(minAmount, maxAmount+1));
        SMPRPG.getService(DropsService.class).addDefaultLootFlags(reward, ctx.getPlayer());

        // Spawn and return!
        return ctx.getLocation().getWorld().dropItem(ctx.getLocation(), reward);
    }

    /**
     * Gets the {@link NamespacedKey} that can be used to reference this loot for things
     * like {@link PersistentDataContainer}s.
     *
     * @return A unique identifying key.
     */
    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SMPRPG.getInstance(), key);
    }

    public ItemStack getItem() {
        return item;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    @Override
    public Material getDisplayMaterial() {

        // Try and query multiple aspects of the item first, but use its raw material as a fallback always.
        var key = this.item.getData(DataComponentTypes.ITEM_MODEL);
        if (key == null)
            return this.item.getType();
        var match = Material.matchMaterial(key.asString());
        if (match == null)
            return this.item.getType();
        return match;
    }

    /**
     * A utility builder to help in the construction of item stack fishing loot.
     */
    public static class Builder extends FishingLootBuilder<ItemStackFishingLoot, Builder> {

        @NotNull ItemStack item;
        private int min = 1;
        private int max = 1;

        public Builder(Material material) {
            this.item = ItemService.generate(material);
        }

        public Builder(CustomItemType type) {
            this.item = ItemService.generate(type);
        }

        /**
         * Set the max stack size this item can drop as.
         * @param min The new minimum.
         * @return Returns back the builder instance.
         */
        public Builder withMinimumAmount(int min) {
            this.min = min;
            return this;
        }

        /**
         * Set the max stack size this item can drop as.
         * @param max The new maximum.
         * @return Returns back the builder instance.
         */
        public Builder withMaximumAmount(int max) {
            this.max = max;
            return this;
        }

        @Override
        public ItemStackFishingLoot build() {
            if (min > max)
                throw new IllegalStateException("Minimum amount must be less than (or equal to) maximum amount");
            return new ItemStackFishingLoot(item, min, max, weight, experienceReward, minecraftExperience, requirements);
        }
    }
}
