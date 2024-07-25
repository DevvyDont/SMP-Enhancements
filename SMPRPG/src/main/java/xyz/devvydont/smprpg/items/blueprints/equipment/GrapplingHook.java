package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.util.Vector;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.Collection;
import java.util.List;

public class GrapplingHook extends CustomItemBlueprint implements Listener, Craftable {

    public static final int COOLDOWN = 2;

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        return List.of(
                AbilityUtil.getAbilityComponent("Grapple"),
                ComponentUtil.getDefaultText("Use to propel yourself"),
                ComponentUtil.getDefaultText("when reeling in!"),
                AbilityUtil.getCooldownComponent(COOLDOWN + "s")
        );
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                "  T",
                " HS",
                "H S"
        );
        recipe.setIngredient('H', itemService.getCustomItem(Material.STICK));
        recipe.setIngredient('T', itemService.getCustomItem(Material.TRIPWIRE_HOOK));
        recipe.setIngredient('S', itemService.getCustomItem(CustomItemType.ENCHANTED_STRING));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.STRING)
        );
    }

    public enum GrapplingState {
        CAST,
        REEL
    }

    public GrapplingHook(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.EQUIPMENT;
    }

    private void use(PlayerFishEvent event, GrapplingState state) {

        // Handle the case we are sending the bobber out
        if (state.equals(GrapplingState.CAST)) {
            return;
        }

        // Handle the case where we are reeling
        Vector dir = event.getHook().getLocation().toVector().subtract(event.getPlayer().getLocation().toVector());
        dir.multiply(new Vector(.35, .25, .35));
        dir.setX(Math.min(2.5, dir.getX()));
        dir.setY(Math.min(1.5, dir.getY()));
        dir.setZ(Math.min(2.5, dir.getZ()));
        event.getPlayer().setVelocity(dir);
        event.getPlayer().setFallDistance(-30);
        event.getPlayer().setCooldown(Material.FISHING_ROD, COOLDOWN*20);
    }

    @EventHandler
    public void onUseGrapplingHook(PlayerFishEvent event) {

        if (event.getHand() == null)
            return;

        ItemStack fishingRod = event.getPlayer().getInventory().getItem(event.getHand());
        if (fishingRod.getType().equals(Material.AIR))
            return;

        if (!isItemOfType(fishingRod))
            return;

        // If the player is casting the hook like normal, allow the event to happen
        if (event.getState().equals(PlayerFishEvent.State.FISHING)) {
            use(event, GrapplingState.CAST);
            return;
        }

        // If the player is reeling in, allow the event to happen
        if (event.getState().equals(PlayerFishEvent.State.REEL_IN) || event.getState().equals(PlayerFishEvent.State.IN_GROUND)) {
            use(event, GrapplingState.REEL);
            return;
        }

        // Every other state is not allowed
        event.setCancelled(true);
    }
}