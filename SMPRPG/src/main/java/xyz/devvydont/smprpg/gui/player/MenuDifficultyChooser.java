package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.ProfileDifficulty;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.services.DifficultyService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuDifficultyChooser extends MenuBase {

    private boolean locked = false;

    public MenuDifficultyChooser(@NotNull Player player) {
        super(player, 4);
        render();
    }

    /**
     * Locks the player to view this menu requiring them to make a decision.
     */
    public void lock() {
        locked = true;
        this.player.setInvulnerable(true);
    }

    /**
     * Unlocks the player from the menu, meaning they are allowed to close it and not make a decision.
     */
    public void unlock() {
        locked = false;
        this.player.setInvulnerable(false);
    }

    public void render() {

        this.setBorderFull();

        // All we need to do is add a couple buttons to set our difficulty and tell the player what it does.
        this.setButton(13, generateDifficultyButton(ProfileDifficulty.STANDARD), e -> handleChoice(ProfileDifficulty.STANDARD));
        this.setButton(11, generateDifficultyButton(ProfileDifficulty.EASY), e -> handleChoice(ProfileDifficulty.EASY));
        this.setButton(15, generateDifficultyButton(ProfileDifficulty.HARD), e -> handleChoice(ProfileDifficulty.HARD));

        this.setBackButton(31);
    }

    private void handleChoice(ProfileDifficulty difficulty) {

        // If this is an invalid difficulty, don't do anything.
        if (!isAllowedToSwitchTo(difficulty)) {
            this.playInvalidAnimation();
            return;
        }

        if (locked)
            unlock();

        this.closeMenu();

        if (difficulty == SMPRPG.getService(DifficultyService.class).getDifficulty(this.player)) {
            player.sendMessage(ComponentUtils.alert("Already playing on that difficulty. Nothing changed."));
            return;
        }

        SMPRPG.getService(DifficultyService.class).setDifficulty(this.player, difficulty);
        Bukkit.broadcast(
                ComponentUtils.alert(
                        ComponentUtils.create("!", NamedTextColor.GOLD),
                        ComponentUtils.merge(
                                ComponentUtils.create(this.player.getName(), difficulty.Color),
                                ComponentUtils.create(" has chosen the path of the "),
                                ComponentUtils.create(difficulty.Display, difficulty.Color),
                                ComponentUtils.create("!")
                        )
                )
        );
    }

    private ItemStack generateDifficultyButton(ProfileDifficulty difficulty) {

        var material = matchMaterialToDifficulty(difficulty);
        if (!isAllowedToSwitchTo(difficulty))
            material = Material.RED_DYE;

        var item = new ItemStack(material);
        var lore = new ArrayList<Component>();

        item.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        item.editMeta(meta -> meta.displayName(ComponentUtils.create(difficulty.Display, difficulty.Color).decoration(TextDecoration.ITALIC, false)));
        item.editMeta(meta -> meta.setEnchantmentGlintOverride(true));

        lore.add(ComponentUtils.EMPTY);
        if (!isAllowedToSwitchTo(difficulty)) {
            lore.add(ComponentUtils.create("You cannot switch to this difficulty since you already chose an easier one!", NamedTextColor.RED));
            lore.add(ComponentUtils.EMPTY);
        }

        if (SMPRPG.getService(DifficultyService.class).getDifficulty(this.player) == difficulty) {
            lore.add(ComponentUtils.create("You are playing on this difficulty!", NamedTextColor.GOLD));
            lore.add(ComponentUtils.EMPTY);
        }

        lore.addAll(matchSummaryToDifficulty(difficulty));
        lore.add(ComponentUtils.EMPTY);
        lore.addAll(matchModifiersToDifficulty(difficulty));

        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.merge(
                ComponentUtils.create("Click to set your difficulty to "),
                ComponentUtils.create(difficulty.Display, difficulty.Color),
                ComponentUtils.create("!")
        ));
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Once chosen, you may only lower your difficulty!", NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true));
        if (this.isAllowedToFreelyChange())
            lore.add(ComponentUtils.create("**You have permission to freely change your difficulty!", NamedTextColor.GREEN));
        item.lore(ComponentUtils.cleanItalics(lore));
        return item;
    }

    /**
     * Checks if the viewer is allowed to freely change difficulties with no restrictions. This is considered an
     * admin action since it could be abused for drop rate manipulation.
     * @return
     */
    private boolean isAllowedToFreelyChange() {
        return this.player.isOp() || this.player.permissionValue("smprpg.difficulty.ignorerestrictions").toBooleanOrElse(false);
    }

    private boolean isAllowedToSwitchTo(ProfileDifficulty difficulty) {

        // Permission override
        if (isAllowedToFreelyChange())
            return true;

        var current = SMPRPG.getService(DifficultyService.class).getDifficulty(this.player);
        // Hasn't chosen yet
        if (current.equals(ProfileDifficulty.NOT_CHOSEN))
            return true;

        return difficulty.ordinal() <= current.ordinal();
    }

    private Collection<Component> matchSummaryToDifficulty(ProfileDifficulty difficulty) {

        return switch (difficulty) {

            case EASY -> List.of(ComponentUtils.merge(
                        ComponentUtils.create("For players who want")
                    ),
                    ComponentUtils.merge(
                            ComponentUtils.create("a "),
                            ComponentUtils.create("relaxed and casual experience", difficulty.Color),
                            ComponentUtils.create(" with friends.")
                    ),
                    ComponentUtils.create("Great for a chill time!")
            );

            case STANDARD -> List.of(
                    ComponentUtils.create("Balanced for players who want"),
                    ComponentUtils.merge(
                            ComponentUtils.create("the "),
                            ComponentUtils.create("core MMO SMP experience", difficulty.Color),
                            ComponentUtils.create(".")
                    ),
                    ComponentUtils.create("Not too hard, but not too easy.")
            );

            case HARD -> List.of(
                    ComponentUtils.create("A hardcore mode designed for players"),
                    ComponentUtils.merge(
                            ComponentUtils.create("who want to "),
                            ComponentUtils.create("truly test themselves", difficulty.Color),
                            ComponentUtils.create(".")
                    ),
                    ComponentUtils.create("Not for the faint of heart.")
            );

            default -> List.of(ComponentUtils.create("Unknown summary for difficulty: " + difficulty));
        };
    }

    private Collection<Component> matchModifiersToDifficulty(ProfileDifficulty difficulty) {

        return switch (difficulty) {

            case EASY -> List.of(
                    ComponentUtils.create("* -50% Incoming Damage", NamedTextColor.GREEN),
                    ComponentUtils.create("* +25% Skill Experience", NamedTextColor.GREEN),
                    ComponentUtils.create("* Ignore Dimension Requirements", NamedTextColor.GREEN),
                    ComponentUtils.create("* -50% Drop Rate Chance", NamedTextColor.RED)
            );

            case STANDARD -> List.of(
                    ComponentUtils.create("* No changes to any gameplay systems", NamedTextColor.YELLOW),
                    ComponentUtils.create("* The intended and balanced experience!", NamedTextColor.YELLOW)
            );

            case HARD -> List.of(
                    ComponentUtils.create("* +100% Drop Rate Chance", NamedTextColor.GREEN),
                    ComponentUtils.create("* +50% Vanilla Experience from Orbs", NamedTextColor.GREEN),
                    ComponentUtils.create("* x2 Incoming Damage", NamedTextColor.RED),
                    ComponentUtils.create("* -50% Base Stats", NamedTextColor.RED),
                    ComponentUtils.create("* -50% Stats from Skills", NamedTextColor.RED),
                    ComponentUtils.create("* -25% Skill Experience", NamedTextColor.RED)
            );

            default -> List.of();
        };

    }

    private Material matchMaterialToDifficulty(ProfileDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> Material.CORNFLOWER;
            case STANDARD -> Material.IRON_SWORD;
            case HARD -> Material.NETHER_STAR;
            default -> Material.BARRIER;
        };
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.create("Choose Your Adventure!", NamedTextColor.GOLD));
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    @Override
    protected void handleInventoryClosed(InventoryCloseEvent event) {
        if (event.getPlayer().equals(this.player) && this.locked) {
            Bukkit.getScheduler().runTaskLater(SMPRPG.getInstance(), () -> {
                openMenu();
                playInvalidAnimation();
            }, 0);
        }
    }
}
