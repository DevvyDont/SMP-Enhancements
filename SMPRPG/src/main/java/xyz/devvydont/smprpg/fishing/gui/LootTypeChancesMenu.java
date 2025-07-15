package xyz.devvydont.smprpg.fishing.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.entity.fishing.SeaCreature;
import xyz.devvydont.smprpg.fishing.loot.FishingLootType;
import xyz.devvydont.smprpg.fishing.loot.FishingLootTypeSelector;
import xyz.devvydont.smprpg.fishing.utils.FishingContext;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.blueprints.fishing.FishBlueprint;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.*;

/**
 * A menu that allows a player to view their current "fishing context", so they know what they are rolling for.
 */
public class LootTypeChancesMenu extends MenuBase {

    // GUI positions.
    public static final int INFO = 47;
    public static final int FISH = 19;
    public static final int CREATURE = FISH + 2;
    public static final int TREASURE = CREATURE + 2;
    public static final int JUNK = TREASURE + 2;

    public LootTypeChancesMenu(@NotNull Player player) {
        super(player, 6);
        render();
    }

    public LootTypeChancesMenu(@NotNull Player player, MenuBase parentMenu) {
        super(player, 6, parentMenu);
        render();
    }

    /**
     * Spoofs a context to simulate fishing odds. It is important to note that we are using the player's location as a
     * "hook" location, obviously since a hook does not exist in this context.
     * @return A new context instance to help simulate fishing odds.
     */
    public FishingContext makeContext() {
        return new FishingContext(this.player, null, this.player.getLocation(), null);
    }

    /**
     * Render the menu.
     */
    public void render() {
        this.sounds.setMenuOpen(Sound.ITEM_BUCKET_FILL_FISH, 1, 1.5f);
        this.sounds.setMenuOpenSub(Sound.ITEM_BUCKET_EMPTY_FISH, 1, .5f);
        this.setBorderFull();

        this.setBackButton(49);
        // Place buttons describing odds
        this.setButton(INFO, InterfaceUtil.getNamedItemWithDescription(
                Material.PAPER,
                create("Information", GOLD),
                EMPTY,
                merge(create("This menu displays every type of reward you")),
                merge(create("can hook while fishing. Each loot type has its")),
                merge(create("own "), create("rules", RED), create(", tied to your "), create("attributes", GOLD), create(", "), create("biome", DARK_GREEN), create(", and even")),
                merge(create("the "), create("temperature", RED), create(" of the environment. Upgrade your gear,")),
                merge(create("explore the world, and master the art of fishing")),
                merge(create("to catch and collect the "), create("rarest finds", LIGHT_PURPLE), create("."))
        ), e -> {
            playInvalidAnimation();
        });

        // We need a type selector to tell us what the odds are of stuff.
        var selector = new FishingLootTypeSelector(makeContext());
        var odds = selector.getLootTypeChances();

        this.setButton(FISH, InterfaceUtil.getNamedItemWithDescription(
                Material.COD,
                create("Fish", BLUE),
                ComponentUtils.EMPTY,
                merge(create("The heart of fishing! These aquatic critters are "), create("quite complex", RED), create(".")),
                merge(create("Some are common catches in "), create("any pond", AQUA), create(" while others require")),
                merge(create("specific "), create("biomes", DARK_GREEN), create(", "), create("temperatures", RED), create(", or higher "), create("catch quality", LIGHT_PURPLE), create(" to reel in.")),
                merge(create("Sell them, cook them, or collect them all!")),
                EMPTY,
                merge(create("Current fish chance: "), formatOdds(odds.getOrDefault(FishingLootType.FISH, 0d))),
                merge(create("After catching a fish, the rarity is randomly rolled with the following odds:")),
                formatRarityOdds(ItemRarity.COMMON),
                formatRarityOdds(ItemRarity.UNCOMMON),
                formatRarityOdds(ItemRarity.RARE),
                formatRarityOdds(ItemRarity.EPIC),
                formatRarityOdds(ItemRarity.LEGENDARY),
                EMPTY,
                create("Click to dive deeper!", YELLOW)
        ), e -> {
            new FishingPoolViewerMenu(this.player, this, FishingLootType.FISH).openMenu();
        });

        this.setButton(CREATURE, InterfaceUtil.getNamedItemWithDescription(
                Material.DROWNED_SPAWN_EGG,
                create("Sea Creatures", SeaCreature.NAME_COLOR),
                EMPTY,
                merge(create("Not everything lurking underwater is friendly.")),
                merge(create("Sea creatures", SeaCreature.NAME_COLOR), create(" are "), create("hostile", RED), create(" mobs that can be pulled")),
                merge(create("from the depths when your "), create("Sea Creature Chance", GOLD), create(" gets higher.")),
                merge(create("Defeat them to earn generous "), create("fishing experience", AQUA), create(" and "), create("rare", LIGHT_PURPLE), create(" crafting materials!")),
                EMPTY,
                merge(create("Current sea creature chance: "), formatOdds(odds.getOrDefault(FishingLootType.CREATURE, 0d))),
                EMPTY,
                create("Click to dive deeper!", YELLOW)

                ), e -> {
            new FishingPoolViewerMenu(this.player, this, FishingLootType.CREATURE).openMenu();
        });

        this.setButton(TREASURE, InterfaceUtil.getNamedItemWithDescription(
                Material.GOLD_INGOT,
                create("Treasure", GOLD),
                EMPTY,
                merge(create("Hidden beneath the surface lie "), create("valuable treasures", GOLD)),
                merge(create("waiting to be uncovered. Treasure is your main source")),
                merge(create("of "), create("profit", GREEN), create(" and "), create("power", YELLOW), create(" while fishing. "), create("Higher "), create("Treasure Chance", GOLD)),
                merge(create("stats can improve your odds of hooking something "), create("special", LIGHT_PURPLE), create(".")),
                EMPTY,
                merge(create("Current treasure chance: "), formatOdds(odds.getOrDefault(FishingLootType.TREASURE, 0d))),
                EMPTY,
                create("Click to dive deeper!", YELLOW)

        ), e -> {
            new FishingPoolViewerMenu(this.player, this, FishingLootType.TREASURE).openMenu();
        });

        this.setButton(JUNK, InterfaceUtil.getNamedItemWithDescription(
                Material.DEAD_BUSH,
                create("Junk", RED),
                EMPTY,
                merge(create("Every fisherman knows the struggle — sometimes, you just pull up "), create("trash", RED), create(".")),
                merge(create("Junk loot includes "), create("mostly useless", RED), create(" odds and ends… but occasionally")),
                merge(create("hides materials used in crafting or gear upgrades.")),
                merge(create("Having a higher "), create("Luck", GOLD), create(" stat will cause you to fish up junk "), create("less often", GREEN), create(".")),
                EMPTY,
                merge(create("Current junk chance: "), formatOdds(odds.getOrDefault(FishingLootType.JUNK, 0d))),
                EMPTY,
                create("Click to dive deeper!", YELLOW)

        ), e -> {
            new FishingPoolViewerMenu(this.player, this, FishingLootType.JUNK).openMenu();
        });

    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(Component.text("Fishing Guide"));
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    /**
     * Chooses a color for a certain probability.
     * @param odds The odds to check a color for.
     * @return The color to use.
     */
    public static TextColor chooseColorForOdds(double odds) {

        if (odds <= 0)
            return DARK_GRAY;

        if (odds <= .5)
            return LIGHT_PURPLE;

        if (odds <= 1)
            return DARK_RED;

        if (odds <= 5)
            return RED;

        if (odds <= 10)
            return GOLD;

        if (odds <= 20)
            return YELLOW;

        return GREEN;
    }

    /**
     * Formats an "odds" component to display for all the different types of things you can catch.
     * @param odds The odds of something occurring.
     * @return A formatted component of the percent chance.
     */
    public static Component formatOdds(double odds) {
        var str = String.format("%.2f%%", odds);
        return create(str, chooseColorForOdds(odds));
    }

    /**
     * Shortcut method used for rendering a fish rarity chance line.
     * @param itemRarity The rarity.
     * @return A chat component.
     */
    public static Component formatRarityOdds(ItemRarity itemRarity) {
        return merge(
                create("* "),
                itemRarity.applyDecoration(create(itemRarity.name())),
                create(": "),
                formatOdds(FishBlueprint.probability(itemRarity)*100)
        );
    }
}
