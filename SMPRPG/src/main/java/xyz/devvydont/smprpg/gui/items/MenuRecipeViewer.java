package xyz.devvydont.smprpg.gui.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuRecipeViewer extends MenuBase {

    public static final int ROWS = 5;

    // Hard set positions
    public static final int CORNER = 10;
    public static final int RESULT = 23;
    public static final int REQUIREMENTS = 25;

    private final Craftable blueprint;
    private final ItemStack result;

    /**
     * Default constructor initialized from within the MenuItemBrowser typically when a craftable item was clicked.
     *
     * @param player The player viewing the recipe.
     * @param parentMenu The menu calling this menu. Typically, the MenuItemBrowser.
     * @param blueprint The Craftable interface implementer responsible for this.
     * @param itemStack The item that was clicked on.
     */
    public MenuRecipeViewer(@NotNull Player player, MenuBase parentMenu, Craftable blueprint, ItemStack itemStack) {
        super(player, ROWS, parentMenu);
        this.blueprint = blueprint;
        this.result = itemStack;
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        super.handleInventoryOpened(event);
        event.titleOverride(ComponentUtils.merge(ComponentUtils.create("Recipe for: "), result.displayName()));
        this.render();
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        super.handleInventoryClicked(event);
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    /**
     * When an ingredient is clicked, if the blueprint of that ingredient is craftable itself as well then open
     * another layer of this menu with its recipe.
     *
     * @param itemStack The ItemStack clicked as a crafting component
     */
    private void handleIngredientClick(ItemStack itemStack) {

        // Retrieve the blueprint of this item. If it is craftable, enter another recipe layer
        SMPItemBlueprint clickedBlueprint = SMPRPG.getInstance().getItemService().getBlueprint(itemStack);
        if (!(clickedBlueprint instanceof Craftable craftable)) {
            this.playInvalidAnimation();
            return;
        }

        this.openSubMenu(new MenuRecipeViewer(player, this, craftable, itemStack));
    }

    /**
     * Renders the recipe completely
     */
    public void renderRecipe() {

        CraftingRecipe recipe = blueprint.getCustomRecipe();

        // Depending on the type of recipe we have, go ahead and attempt to render the recipe procedure.
        // todo, this should be abstracted to allow different types of recipes, not just shaped ones.
        if (recipe instanceof ShapedRecipe shaped) {

            int x = 0;
            int y = 0;
            for (String row : shaped.getShape()) {
                for (char ingredient : row.toCharArray()) {
                    RecipeChoice choice = shaped.getChoiceMap().get(ingredient);
                    ItemStack item;
                    if (choice instanceof RecipeChoice.ExactChoice exact)
                        item = exact.getItemStack();
                    else if (choice instanceof RecipeChoice.MaterialChoice materialChoice)
                        item = materialChoice.getItemStack();
                    else
                        item = new ItemStack(Material.AIR);

                    // If this ingredient can be crafted, insert the craftable tooltip.
                    if (SMPRPG.getInstance().getItemService().getBlueprint(item) instanceof Craftable)
                        item.editMeta(meta -> meta.lore(ComponentUtils.cleanItalics(ComponentUtils.insertComponents(meta.lore(), ComponentUtils.EMPTY, ComponentUtils.create("Click to view recipe!", NamedTextColor.YELLOW)))));

                    this.setButton(y*9+x+CORNER, item, event -> handleIngredientClick(item));
                    x+= 1;
                }
                y += 1;
                x = 0;
            }
        }

    }

    /**
     * Generates an item stack that shows what "requirements" an item has in order to craft. This is subject to change,
     * but for now it simply displays what item "discovers" the recipe upon picking up. A common example is how the
     * recipe for a copper chestplate is discovered when we pick up a copper ingot.
     *
     * @param requirements A collection of item stacks that is "required" to discover this recipe.
     * @return an item stack to be used as a display in the interface
     */
    public ItemStack getRequirements(Collection<ItemStack> requirements) {

        ItemStack paper = createNamedItem(Material.PAPER, ComponentUtils.create("Crafting Requirements", NamedTextColor.RED));
        paper.editMeta(meta -> {
            List<Component> lore = new ArrayList<>();
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("The following items must be"));
            lore.add(ComponentUtils.create("discovered to unlock this recipe"));
            lore.add(ComponentUtils.EMPTY);
            for (ItemStack item : requirements)
                lore.add(ComponentUtils.create("- ").append(item.displayName()));
            meta.lore(ComponentUtils.cleanItalics(lore));
            meta.setEnchantmentGlintOverride(true);
        });
        SMPRPG.getInstance().getItemService().setIgnoreMetaUpdate(paper);
        return paper;
    }

    public void render() {

        this.clear();
        this.setBorderFull();

        this.renderRecipe();

        // Misc buttons
        this.setSlot(RESULT, result);
        this.setSlot(REQUIREMENTS, getRequirements(blueprint.unlockedBy()));

        // Utility buttons
        this.setButton((ROWS-1)*9+4, BUTTON_BACK, (e) -> this.openParentMenu());
    }
}
