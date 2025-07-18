package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

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
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.SwordRecipe;

import java.util.Collection;
import java.util.List;

public class NeptuneTrident extends CustomAttributeItem implements IBreakableEquipment, ICraftable {

    @Override
    public boolean wantNerfedSellPrice() {
        return false;
    }

    public NeptuneTrident(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(Material.NETHERITE_SWORD)-10),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemSword.SWORD_ATTACK_SPEED_DEBUFF),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 20)
        );
    }

    @Override
    public int getPowerRating() {
        return NeptuneArmorSet.POWER_LEVEL;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TRIDENT;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return NeptuneArmorSet.DURABILITY;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new SwordRecipe(this, itemService.getCustomItem(CustomItemType.PLUTOS_ARTIFACT), itemService.getCustomItem(CustomItemType.DIAMOND_TOOL_ROD), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.PLUTO_FRAGMENT)
        );
    }
}
