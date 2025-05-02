package xyz.devvydont.smprpg.items.blueprints.sets.copper;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.SwordRecipe;
import xyz.devvydont.smprpg.util.items.ToolsUtil;

import java.util.Collection;
import java.util.List;


public class CopperSword extends CustomAttributeItem implements ICraftable, IBreakableEquipment {

    public CopperSword(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(Material.STONE_SWORD)),
                new MultiplicativeAttributeEntry(Attribute.ATTACK_SPEED, ItemSword.SWORD_ATTACK_SPEED_DEBUFF+.1)
        );
    }

    @Override
    public int getPowerRating() {
        return 5;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return ToolsUtil.COPPER_TOOL_DURABILITY;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new SwordRecipe(this,
                itemService.getCustomItem(Material.COPPER_INGOT),
                itemService.getCustomItem(Material.STICK),
                generate()
        ).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.COPPER_INGOT)
        );
    }

}
