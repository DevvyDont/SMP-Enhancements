package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.util.Vector;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.Collection;
import java.util.List;

public class GrapplingHook extends CustomAttributeItem implements IHeaderDescribable, Listener, ICraftable {

    public static final int COOLDOWN = 2;

    public static final float VELOCITY_DAMPENING_FACTOR = .3f;

    public static final float MAX_HORIZONTAL_VELOCITY = 4f;

    public static final float MIN_VERTICAL_VELOCITY = 1f;
    public static final float MAX_VERTICAL_VELOCITY = 2f;

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                AbilityUtil.getAbilityComponent("Grapple"),
                ComponentUtils.create("Use to propel yourself"),
                ComponentUtils.create("when reeling in!"),
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

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.FALL_DAMAGE_MULTIPLIER, -.9),
                new AdditiveAttributeEntry(AttributeWrapper.SAFE_FALL, 15)
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
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

        // Dampen the velocity
        dir.multiply(VELOCITY_DAMPENING_FACTOR);

        // Make sure we didn't hit a limit
        dir.setX(Math.min(Math.max(-MAX_HORIZONTAL_VELOCITY, dir.getX()), MAX_HORIZONTAL_VELOCITY));
        dir.setY(Math.min(Math.max(MIN_VERTICAL_VELOCITY, dir.getY()), MAX_VERTICAL_VELOCITY));
        dir.setZ(Math.min(Math.max(-MAX_HORIZONTAL_VELOCITY, dir.getZ()), MAX_HORIZONTAL_VELOCITY));

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
        if (event.getState().equals(PlayerFishEvent.State.REEL_IN) || event.getState().equals(PlayerFishEvent.State.IN_GROUND) || event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            use(event, GrapplingState.REEL);
            return;
        }

        // Every other state is not allowed
        event.setCancelled(true);
    }
}
