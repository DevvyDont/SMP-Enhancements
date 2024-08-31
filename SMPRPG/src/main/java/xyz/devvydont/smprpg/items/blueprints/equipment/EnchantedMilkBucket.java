package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.ChargedItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class EnchantedMilkBucket extends ChargedItemBlueprint implements Listener, Craftable {

    public EnchantedMilkBucket(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        return List.of(
                ComponentUtils.create("Consume to cleanse ").append(ComponentUtils.create("ALL", NamedTextColor.GOLD)),
                ComponentUtils.create("status effects you have!")
        );
    }

    @Override
    public int maxCharges(ItemMeta meta) {
        return 32;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @EventHandler
    public void onConsumeEnchantedMilk(PlayerItemConsumeEvent event) {

        if (!isItemOfType(event.getItem()))
            return;

        ItemStack milk = event.getItem();
        useCharge(event.getPlayer(), milk);
        event.getPlayer().getInventory().setItem(event.getHand(), milk);
        event.setCancelled(true);
        event.getPlayer().clearActivePotionEffects();
        SMPRPG.getInstance().getActionBarService().addActionBarComponent(event.getPlayer(), ActionBarService.ActionBarSource.MISC, ComponentUtils.create("CLEANSED!", NamedTextColor.GREEN), 2);
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("mmm", "mmm", "mmm");
        recipe.setIngredient('m', itemService.getCustomItem(Material.MILK_BUCKET));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.BUCKET)
        );
    }
}
