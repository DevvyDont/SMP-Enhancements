package xyz.devvydont.smprpg.items.blueprints.sets.emberclad;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.*;

public class BoilingPickaxe extends CustomAttributeItem implements Listener, IHeaderDescribable, IBreakableEquipment, ICraftable {

    public static final Map<Material, Material> autoSmeltMap = new HashMap<>();

    static {
        autoSmeltMap.put(Material.RAW_GOLD, Material.GOLD_INGOT);
        autoSmeltMap.put(Material.RAW_IRON, Material.IRON_INGOT);
        autoSmeltMap.put(Material.RAW_COPPER, Material.COPPER_INGOT);
        autoSmeltMap.put(Material.COBBLESTONE, Material.STONE);
        autoSmeltMap.put(Material.COBBLED_DEEPSLATE, Material.DEEPSLATE);
        autoSmeltMap.put(Material.SAND, Material.GLASS);
        autoSmeltMap.put(Material.CLAY_BALL, Material.BRICK);
    }

    public BoilingPickaxe(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return 48_000 * itemStack.getAmount();
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        List<Component> components = new ArrayList<>();
        components.add(AbilityUtil.getAbilityComponent("Boiling Touch (Passive)"));
        components.add(ComponentUtils.create("Automatically ").append(ComponentUtils.create("smelts", NamedTextColor.RED)).append(ComponentUtils.create(" blocks broken")));
        return components;
    }

    @Override
    public void updateItemData(ItemMeta meta) {
        super.updateItemData(meta);
        meta.removeEnchant(Enchantment.SILK_TOUCH);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 45),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemPickaxe.PICKAXE_ATTACK_SPEED_DEBUFF),
                new MultiplicativeAttributeEntry(AttributeWrapper.MINING_SPEED, .10)
        );
    }

    @Override
    public int getPowerRating() {
        return 35;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return 50_000;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreakSmeltableBlock(BlockDropItemEvent event) {

        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;

        if (!isItemOfType(event.getPlayer().getInventory().getItemInMainHand()))
            return;

        // We broke a block with this pickaxe. Replace any items that need to be smelted
        for (Item drop : event.getItems()) {

            ItemStack itemstack = drop.getItemStack();
            SMPItemBlueprint blueprint = itemService.getBlueprint(itemstack);
            if (blueprint.isCustom())
                continue;

            Material smeltedMaterial = autoSmeltMap.get(itemstack.getType());
            if (smeltedMaterial == null)
                continue;

            // Transform the item stack.
            ItemStack updatedItem = itemstack.withType(smeltedMaterial);
            blueprint = itemService.getBlueprint(updatedItem);
            blueprint.updateItemData(updatedItem);
            drop.setItemStack(updatedItem);
        }

    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                "iii",
                " s ",
                " s "
        );
        recipe.setIngredient('i', itemService.getCustomItem(CustomItemType.BOILING_INGOT));
        recipe.setIngredient('s', itemService.getCustomItem(CustomItemType.OBSIDIAN_TOOL_ROD));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.BOILING_INGOT),
                itemService.getCustomItem(CustomItemType.OBSIDIAN_TOOL_ROD)
        );
    }
}
