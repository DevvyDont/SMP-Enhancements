package xyz.devvydont.smprpg.fishing.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.fishing.SeaCreature;
import xyz.devvydont.smprpg.fishing.loot.*;
import xyz.devvydont.smprpg.fishing.pools.FishingLootPool;
import xyz.devvydont.smprpg.fishing.pools.FishingRewardRegistry;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.Collection;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.*;

/**
 * This menu allows players to view the actual current fishing loot pool that they have access to, given their
 * current stats. As a bonus, they can also view every globally registered loot in a pool, and their requirements.
 * This menu can only be accessed via a submenu of the {@link LootTypeChancesMenu} fishing menu.
 */
public class FishingPoolViewerMenu extends MenuBase {

    private final int MAX_PAGE_SIZE = 28;

    private FishingLootType type;
    private int offset;  // The offset is how far to "skip" when displaying items. Used for pagination.

    public FishingPoolViewerMenu(@NotNull Player player, MenuBase parentMenu, FishingLootType type) {
        super(player, 6, parentMenu);
        this.type = type;
        this.sounds.setMenuClose(Sound.ITEM_BUCKET_EMPTY, 1, .5f);
        render();
    }

    /**
     * Renders this menu based on its current state.
     */
    public void render() {

        this.clear();
        this.setBorderEdge();

        var loot = getPossibleLoot(offset);
        var lootIterator = loot.iterator();
        // Loop through every slot in this inventory. If the slot is empty, populate it with a display.
        for (int i = 0; i < this.getInventorySize(); i++) {

            if (this.getItem(i) != null)
                continue;

            if (!lootIterator.hasNext())
                break;

            this.setSlot(i, generateLootDisplay(lootIterator.next()));
        }

        // Back + pagination.
        this.setBackButton(49);
        this.setButton(45, InterfaceUtil.getNamedItem(Material.SPECTRAL_ARROW, create("Previous Page", AQUA)), e -> {
            changePage(-1);
            this.sounds.playPagePrevious();
        });
        this.setButton(53, InterfaceUtil.getNamedItem(Material.SPECTRAL_ARROW, create("Next Page", AQUA)), e -> {
            changePage(1);
            this.sounds.playPageNext();
        });
    }

    public void changePage(int amount) {
        var allPossibleItems = getPossibleLoot();
        this.offset += MAX_PAGE_SIZE * amount;

        // Handle both cases for offset over/underflow.
        if (this.offset < 0) {
            this.offset = Math.max(0, allPossibleItems.size()-MAX_PAGE_SIZE);
        }
        if (this.offset > allPossibleItems.size())
            this.offset = 0;

        render();
    }

    /**
     * Gets the current fishing loot type this menu is going to render.
     * @return The {@link FishingLootType} type currently set.
     */
    public FishingLootType getType() {
        return type;
    }

    /**
     * Sets the current fishing loot type this menu is going to render. If you want changes to be visually reflected,
     * also call {@link FishingPoolViewerMenu#render()} immediately after.
     * @param type The new loot type to view.
     */
    public void setType(FishingLootType type) {
        this.type = type;
    }

    /**
     * Generates an item to display that represents a fishing loot item.
     * @param loot The loot to show information about.
     * @return A fully formatted item.
     */
    private ItemStack generateLootDisplay(FishingLootBase loot) {

        // Based on the type of loot this is, we can come up with a name.
        Component name = null;
        if (loot instanceof ItemStackFishingLoot itemLoot)
            name = itemLoot.getItem().getData(DataComponentTypes.ITEM_NAME);
        else if (loot instanceof SeaCreatureFishingLoot seaCreatureLoot)
            name = create(seaCreatureLoot.getCustomEntityType().getName(), SeaCreature.NAME_COLOR);
        else
            SMPRPG.broadcastToOperatorsCausedBy(this.player, create("Failed to resolve FishingLoot class name handler for " + loot.getClass().getCanonicalName() + " in the " + this.getClass().getCanonicalName() + "GUI", RED));

        if (name == null)
            name = create("???", RED);

        // Various calculator utilities that provide useful information.
        var ctx = new FishingContext(this.player, null, this.player.getLocation(), null);
        var lootPool = new FishingLootPool(ctx, this.getType());
        var selector = new FishingLootTypeSelector(ctx);
        var chance = lootPool.getLootChance(loot);

        // Draft up a description. We need information like weight, requirements and rewards.
        var item = InterfaceUtil.getNamedItem(loot.getDisplayMaterial(), name);
        var lore = new ArrayList<Component>();
        lore.add(EMPTY);
        lore.add(create("Rewards", GOLD));
        lore.add(merge(create("* "), create(loot.getFishingExperience() + " Fishing skill experience", AQUA)));
        lore.add(merge(create("* "), create(loot.getMinecraftExperience() + " Minecraft experience", DARK_GREEN)));
        var reqs = loot.getRequirements();
        if (!reqs.isEmpty()) {
            lore.add(EMPTY);
            lore.add(create("Requirements", GOLD));
            for (var req : reqs)
                lore.add(merge(create("* "), req.display(), SPACE, req.passes(ctx) ? create(Symbols.CHECK, GREEN) : create(Symbols.X, RED)));
        }
        lore.add(EMPTY);
        lore.add(create("Your Rates", GOLD));
        lore.add(merge(create("- Weight: "), create(loot.getWeight(), YELLOW)));
        lore.add(merge(create("- Chance"), create(" (for type)", DARK_GRAY), create(": "), LootTypeChancesMenu.formatOdds(chance*100)));
        lore.add(merge(create("- Probability"), create(" (per cast)", DARK_GRAY), create(": "), LootTypeChancesMenu.formatOdds(chance*selector.getBaseChance(this.type))));
        lore.add(create("Affected by your current attributes and location!", DARK_GRAY));

        item.lore(ComponentUtils.cleanItalics(lore));
        return item;
    }

    /**
     * Gets the possible fishing loot from the global registry, ignoring all conditions.
     * @param offset The offset of items to start at, if desired. Useful for pagination.
     * @param limit the maximum amount of items to return, if desired. Useful for pagination. If this ends up going out
     *              of bounds, the rest of the items in the list will be returned, meaning the size of items you get
     *              is unpredictable.
     * @return A collection of fishing loot.
     */
    private Collection<FishingLootBase> getPossibleLoot(int offset, int limit) {
        var items = FishingRewardRegistry.getRegisteredRewards(getType());
        var max = items.size();
        if (offset >= items.size())
            return new ArrayList<>();
        return items.stream().toList().subList(offset, Math.min(offset + limit, max));
    }

    /**
     * Gets all possible fishing loot. Defaults to no offset, making this a good default page state.
     * @return A collection of possible fish loot from the global pool.
     */
    private Collection<FishingLootBase> getPossibleLoot() {
        return getPossibleLoot(0, MAX_PAGE_SIZE);
    }

    /**
     * Gets all global possible fishing loot, with an offset provided. Forces the max page size restriction.
     * @param offset The offset to query items for.
     * @return Possible fishing loot.
     */
    private Collection<FishingLootBase> getPossibleLoot(int offset) {
        return getPossibleLoot(offset, MAX_PAGE_SIZE);
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        playInvalidAnimation();
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(Component.text(MinecraftStringUtils.getTitledString(this.type.name() + " Information")));
    }
}
