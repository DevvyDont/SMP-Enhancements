package xyz.devvydont.smprpg.items.blueprints.sets.emberclad;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.*;

public class BoilingPickaxe extends CustomAttributeItem implements Listener, ToolBreakable {

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
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> components = new ArrayList<>(super.getDescriptionComponent(meta));
        components.add(Component.empty());
        components.add(AbilityUtil.getAbilityComponent("Boiling Touch (Passive)"));
        components.add(ComponentUtil.getDefaultText("Automatically ").append(ComponentUtil.getColoredComponent("smelts", NamedTextColor.RED)).append(ComponentUtil.getDefaultText(" blocks broken")));
        return components;
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
        meta.setFireResistant(true);
        meta.removeEnchant(Enchantment.SILK_TOUCH);
        updateLore(meta);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 250),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemPickaxe.PICKAXE_ATTACK_SPEED_DEBUFF),
                new MultiplicativeAttributeEntry(Attribute.PLAYER_BLOCK_BREAK_SPEED, .10)
        );
    }

    @Override
    public int getPowerRating() {
        return 40;
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
            blueprint.updateMeta(updatedItem);
            drop.setItemStack(updatedItem);
        }

    }
}
