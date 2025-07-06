package xyz.devvydont.smprpg.gui.enchantments;

import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * A menu used to view all the enchantments and their attributes in the game.
 */
public class EnchantmentMenu extends MenuBase {

    // How many rows this GUI will have.
    private static final int ROWS = 6;

    // List of the custom enchantment instances we are trying to display.
    protected List<CustomEnchantment> enchantments;
    // The page we are currently on. Used so we know how to "offset" the enchantments to display.
    private int page = 0;
    private boolean reverseResults = false;

    private EnchantmentSortMode sortMode = EnchantmentSortMode.DEFAULT;

    public EnchantmentMenu(@NotNull Player player) {
        super(player, ROWS);

        // Set up the enchantments. These are just a copy of all the enchantments in a list.
        enchantments = new ArrayList<>();
        enchantments.addAll(SMPRPG.getService(EnchantmentService.class).getCustomEnchantments());
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.create("Enchantments", NamedTextColor.BLACK));
        render();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        super.handleInventoryClicked(event);
        event.setCancelled(true);
    }

    /**
     * Given a custom enchantment instance, generate an itemstack to show in the GUI to describe it.
     *
     * @param enchantment A custom enchantment instance, the level bound to it does not matter.
     * @return An itemstack used to be a display for the enchantment
     */
    private ItemStack generateEnchantmentButton(CustomEnchantment enchantment) {
        ItemStack book = createNamedItem(Material.ENCHANTED_BOOK, enchantment.getDisplayName().color(enchantment.getEnchantColor()));

        // Start constructing the lore of the item, this is essentially an in depth description of the enchantment.
        List<Component> enchantmentDescription = new ArrayList<>();
        enchantmentDescription.add(ComponentUtils.EMPTY);

        // First the most important part. What does it do?
        enchantmentDescription.add(enchantment.getEnchantment().displayName(1).color(enchantment.getEnchantColor()));
        enchantmentDescription.add(enchantment.build(1).getDescription());
        // If this enchantment has more than one level, we should also show off what the "maxed" version of this enchant entails
        if (enchantment.getMaxLevel() > 1) {
            enchantmentDescription.add(ComponentUtils.EMPTY);
            enchantmentDescription.add(enchantment.getEnchantment().displayName(enchantment.getMaxLevel()).color(enchantment.getEnchantColor()));
            enchantmentDescription.add(enchantment.build(enchantment.getMaxLevel()).getDescription());
        }

        enchantmentDescription.add(ComponentUtils.EMPTY);

        // Now let's add misc. information about this enchantment to put at the bottom.
        enchantmentDescription.add(ComponentUtils.merge(ComponentUtils.create("Max Enchantment Level: "), ComponentUtils.create(String.valueOf(enchantment.getMaxLevel()), NamedTextColor.GREEN)));
        enchantmentDescription.add(ComponentUtils.merge(ComponentUtils.create("Enchantment Rarity Ranking: "), ComponentUtils.create(String.valueOf(enchantment.getWeight()), NamedTextColor.GREEN), ComponentUtils.create(" (Lower = Rarer)", NamedTextColor.DARK_GRAY)));
        enchantmentDescription.add(ComponentUtils.merge(ComponentUtils.create("Applicable Item Type: "), ComponentUtils.create(MinecraftStringUtils.getTitledString(enchantment.getItemTypeTag().key().asMinimalString().replace("/", " ")), NamedTextColor.GOLD)));

        // Any enchantment conflicts?
        if (!enchantment.getConflictingEnchantments().isEmpty()) {
            enchantmentDescription.add(ComponentUtils.EMPTY);
            enchantmentDescription.add(ComponentUtils.create("Conflicting Enchantments: "));
            for (TypedKey<Enchantment> conflict : enchantment.getConflictingEnchantments().values()) {
                Enchantment conflictEnchant = SMPRPG.getService(EnchantmentService.class).getEnchantment(conflict);
                CustomEnchantment conflictEnchantWrapper = SMPRPG.getService(EnchantmentService.class).getEnchantment(conflictEnchant);
                enchantmentDescription.add(ComponentUtils.merge(ComponentUtils.create("- "), conflictEnchantWrapper.getDisplayName().color(conflictEnchantWrapper.getEnchantColor())));
            }
        }

        enchantmentDescription.add(ComponentUtils.EMPTY);
        boolean isUnlocked = SMPRPG.getService(EntityService.class).getPlayerInstance(player).getMagicSkill().getLevel() >= enchantment.getSkillRequirement();
        enchantmentDescription.add(ComponentUtils.merge(ComponentUtils.create("Magic Skill Level Requirement: ", isUnlocked ? NamedTextColor.GRAY : NamedTextColor.RED), ComponentUtils.create(String.valueOf(enchantment.getSkillRequirement()), isUnlocked ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.DARK_RED)));

        book.editMeta(meta -> {
            meta.lore(ComponentUtils.cleanItalics(enchantmentDescription));
        });
        return book;
    }

    /**
     * Generates the button responsible for displaying the current sort mode.
     * @return An ItemStack representing the button used for determining sort behavior for the enchantments.
     */
    private @NotNull ItemStack generateSortButton() {

        ItemStack item = createNamedItem(Material.REPEATER, ComponentUtils.create("Change Sort Mode", NamedTextColor.GOLD));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.merge(ComponentUtils.create("Current Sort Mode: "), ComponentUtils.create(sortMode.display(), NamedTextColor.GREEN), reverseResults ? ComponentUtils.create(" (REVERSED)", NamedTextColor.DARK_GRAY) : ComponentUtils.EMPTY));
        lore.add(ComponentUtils.EMPTY);

        for (EnchantmentSortMode mode : EnchantmentSortMode.values())
            lore.add(ComponentUtils.create("> " + mode.display(), mode == sortMode ? NamedTextColor.GREEN : NamedTextColor.DARK_GRAY));

        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Click to cycle through sort modes!"));
        lore.add(ComponentUtils.create("Right click to reverse the sorting method!"));

        item.editMeta(meta -> {
            meta.lore(ComponentUtils.cleanItalics(lore));
        });
        return item;
    }

    /**
     * Resets this interface to its default state where we are displaying enchantments in default order.
     */
    public void render() {

        this.clear();
        this.setBorderEdge();

        // Using the page, generate an offset within the enchant list to know when to display from. If the offset is
        // too high, just assume we are jumping back to the first page.
        // If it is too low, set the offset to the last page.
        // Can be calculated by multiplying the page by the area of the displayable area
        int area = 7 * (ROWS-2);
        int lastPage = enchantments.size() / area;
        int offset = page * area;
        if (offset >= enchantments.size()) {
            page = 0;
            offset = 0;
        }
        if (offset < 0) {
            page = lastPage;
            offset = page * area;
        }

        // Attempt to fill as many slots w/ enchantments that we can
        for (int i = 0; i < getInventorySize(); i++) {

            // If we don't have another enchantment to render, abort
            if (offset >= enchantments.size())
                break;

            CustomEnchantment enchantment = enchantments.get(offset);

            // If this slot isn't empty, we shouldn't put something there
            if (this.getItem(i) != null)
                continue;

            this.setButton(i, generateEnchantmentButton(enchantment), (e) -> {
                playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE);
                openSubMenu(new EnchantmentSubMenu(player, this, enchantment));
            });

            offset++;
        }

        // Now set the slots for a next/prev page that increment/decrement the page and re-render
        int displayPage = page + 1;
        int displayPageMax = lastPage + 1;
        this.setButton((ROWS-1)*9, createNamedItem(Material.ARROW, ComponentUtils.create("Previous Page (" + displayPage + "/" + displayPageMax + ")", NamedTextColor.GOLD)), (e) -> {
            page--;
            render();
            this.sounds.playPagePrevious();
        });

        this.setButton((ROWS-1)*9+8, createNamedItem(Material.ARROW, ComponentUtils.create("Next Page (" + displayPage + "/" + displayPageMax + ")", NamedTextColor.GOLD)), (e) -> {
            page++;
            render();
            this.sounds.playPageNext();
        });

        // Sort mode button, when this is clicked the sort mode is changed and the page is re-rendered.
        this.setButton((ROWS-1)*9+6, generateSortButton(), (e) -> {

            // If this was a right click, instead flip the reverse flag instead of cycling the mode.
            if (e.isRightClick())
                reverseResults = !reverseResults;
            else
                sortMode = sortMode.next();

            // Sort all the enchantments based on the mode and reverse if desired
            enchantments = sortMode.sort(enchantments);
            if (reverseResults)
                enchantments = enchantments.reversed();

            render();
            playSound(Sound.BLOCK_DISPENSER_FAIL);
        });

        // Close button
        this.setButton((ROWS-1)*9+4, BUTTON_EXIT, (e) -> {
            closeMenu();
            this.sounds.playMenuClose();
        });

    }


}
