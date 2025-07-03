package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TransmissionWand extends CustomAttributeItem implements IHeaderDescribable, Listener, ICraftable, IModelOverridden {

    // The mana cost to use this item.
    public static final int COST = 100;

    // Items that will force the teleportation event to not fire in favor of interacting with the block.
    // Not ideal, but Material#isInteractable() is deprecated >_<
    public static final Set<Material> INTERACTABLE_BLOCK_BLACKLIST = Set.of(
            Material.CHEST,
            Material.TRAPPED_CHEST,
            Material.BARREL,
            Material.ENDER_CHEST,
            Material.CRAFTING_TABLE,
            Material.FURNACE,
            Material.BLAST_FURNACE,
            Material.SMOKER,
            Material.ANVIL,
            Material.ENCHANTING_TABLE,
            Material.GRINDSTONE,
            Material.LECTERN,
            Material.SMITHING_TABLE,
            Material.STONECUTTER,
            Material.CARTOGRAPHY_TABLE,
            Material.LOOM,
            Material.BELL,
            Material.NOTE_BLOCK,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.STONE_BUTTON,
            Material.OAK_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.CRIMSON_BUTTON,
            Material.WARPED_BUTTON,
            Material.MANGROVE_BUTTON,
            Material.CHERRY_BUTTON,
            Material.BAMBOO_BUTTON,
            Material.IRON_DOOR,
            Material.OAK_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.CRIMSON_DOOR,
            Material.WARPED_DOOR,
            Material.MANGROVE_DOOR,
            Material.CHERRY_DOOR,
            Material.BAMBOO_DOOR,
            Material.ACACIA_TRAPDOOR,
            Material.BAMBOO_TRAPDOOR,
            Material.COPPER_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.MANGROVE_TRAPDOOR,
            Material.CHERRY_TRAPDOOR,
            Material.OXIDIZED_COPPER_TRAPDOOR,
            Material.EXPOSED_COPPER_TRAPDOOR,
            Material.WARPED_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            Material.IRON_TRAPDOOR,
            Material.LEVER
    );

    public TransmissionWand(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.EQUIPMENT;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 100)
        );
    }

    @Override
    public int getPowerRating() {
        return 50;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), "transmission_wand_recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        var recipe = new ShapedRecipe(this.getRecipeKey(), generate());
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        recipe.shape(" m ", " r ", " r ");
        recipe.setIngredient('m', ItemService.generate(CustomItemType.ENCHANTED_ENDER_PEARL));
        recipe.setIngredient('r', ItemService.generate(CustomItemType.DRACONIC_CRYSTAL));
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(ItemStack.of(Material.ENDER_PEARL));
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                AbilityUtil.getAbilityComponent("Instant Transmission"),
                ComponentUtils.create("Right click to instantly"),
                ComponentUtils.create("teleport where you're looking!"),
                AbilityUtil.getManaCostComponent(COST)
        );
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.DIAMOND_SHOVEL;
    }

    private boolean doPlayerTeleport(Player player) {

        Location old = player.getEyeLocation();

        boolean foundSpot = false;
        int distance = 8;

        while (!foundSpot && distance > 2){

            distance--;

            if (old.getWorld().rayTraceBlocks(old, old.getDirection(), distance) == null)
                foundSpot = true;
        }

        if (!foundSpot){
            player.sendMessage(ComponentUtils.error("No free spot ahead of you!"));
            return false;
        }

        Location newLocation = old.add(old.getDirection().normalize().multiply(distance));
        player.teleport(newLocation);
        player.getWorld().playEffect(old, Effect.ENDER_SIGNAL, 1);
        player.getWorld().playEffect(newLocation, Effect.ENDER_SIGNAL, 0);
        player.getWorld().playSound(newLocation, Sound.ENTITY_ENDER_EYE_DEATH, .4f, 1);
        player.getWorld().playSound(old, Sound.ENTITY_ENDERMAN_TELEPORT, .4f, 1);
        return true;
    }

    @EventHandler
    public void onInteractWithWand(PlayerInteractEvent event) {

        // This item?
        if (!isItemOfType(event.getItem()))
            return;

        // Enough mana?
        var player = SMPRPG.getInstance().getEntityService().getPlayerInstance(event.getPlayer());
        if (player.getMana() < COST) {
            SMPRPG.getInstance().getActionBarService().addActionBarComponent(event.getPlayer(), ActionBarService.ActionBarSource.MISC, ComponentUtils.create("NO MANA", NamedTextColor.RED), 1);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, .3f, .5f);
            return;
        }

        // Valid interact event?
        if (!event.getAction().isRightClick())
            return;
        if (event.getClickedBlock() != null && INTERACTABLE_BLOCK_BLACKLIST.contains(event.getClickedBlock().getType()))
            return;

        // Subtract mana and teleport.
        if (!this.doPlayerTeleport(event.getPlayer()))
            return;
        player.useMana(COST);
        SMPRPG.getInstance().getActionBarService().addActionBarComponent(event.getPlayer(),
                ActionBarService.ActionBarSource.MISC,
                ComponentUtils.merge(ComponentUtils.create("Instant Transmission!", NamedTextColor.GOLD), ComponentUtils.SPACE, ComponentUtils.create("-" + COST + Symbols.MANA, NamedTextColor.AQUA)),
                1);
    }
}
