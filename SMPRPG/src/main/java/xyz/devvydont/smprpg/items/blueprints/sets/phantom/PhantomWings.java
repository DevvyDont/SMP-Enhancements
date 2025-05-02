package xyz.devvydont.smprpg.items.blueprints.sets.phantom;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;

import java.util.Collection;
import java.util.List;

public class PhantomWings extends CustomAttributeItem implements IBreakableEquipment, ICraftable {

    public PhantomWings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 10)
        );
    }

    @Override
    public int getPowerRating() {
        return 10;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHESTPLATE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.CHEST;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_MEMBRANE), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(Material.PHANTOM_MEMBRANE)
        );
    }

    @Override
    public int getMaxDurability() {
        return 100;
    }
}
