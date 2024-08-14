package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterfaceRecipe extends PrivateInterface {

    public InterfaceRecipe(SMPRPG plugin, Player owner) {
        super(plugin, owner);
    }

    @Override
    public int getNumRows() {
        return 5;
    }

    @Override
    public void initializeDefaultState() {
        super.initializeDefaultState();
        fill(InterfaceUtil.getInterfaceBorder());
    }

    public void showRecipe(Craftable craftable) {

        int CORNER = 10;
        int RESULT = 23;
        int REQUIREMENTS = 25;

        CraftingRecipe recipe = craftable.getCustomRecipe();
        this.inventoryView.setTitle("Recipe for: " + PlainTextComponentSerializer.plainText().serialize(recipe.getResult().displayName()));
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

                    this.inventory.setItem(y*9+x+CORNER, item);
                    x+= 1;
                }
                y += 1;
                x = 0;
            }
            this.inventory.setItem(RESULT, recipe.getResult());
            this.inventory.setItem(REQUIREMENTS, getRequirements(craftable.unlockedBy()));
        }

    }

    public ItemStack getRequirements(Collection<ItemStack> requirements) {

        ItemStack paper = InterfaceUtil.getNamedItem(Material.PAPER, Component.text("Crafting Requirements").color(NamedTextColor.RED));
        paper.editMeta(meta -> {
            List<Component> lore = new ArrayList<>();
            lore.add(Component.empty());
            lore.add(ComponentUtil.getDefaultText("The following items must be"));
            lore.add(ComponentUtil.getDefaultText("discovered to unlock this recipe"));
            lore.add(Component.empty());
            for (ItemStack item : requirements)
                lore.add(ComponentUtil.getDefaultText("- ").append(item.displayName()));
            meta.lore(ChatUtil.cleanItalics(lore));
            meta.setEnchantmentGlintOverride(true);
        });
        plugin.getItemService().setIgnoreMetaUpdate(paper);
        return paper;
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        super.handleInventoryClick(event);
        event.setCancelled(true);
    }
}
