package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import io.papermc.paper.datacomponent.item.Equippable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.IEquippableOverride;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

import java.util.Collection;
import java.util.List;

public class NeptuneHelmet extends NeptuneArmorSet implements IBreakableEquipment, ICraftable, IEquippableOverride {

    public NeptuneHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Equippable getEquipmentOverride() {
        return IEquippableOverride.generateDefault(EquipmentSlot.HEAD);
    }

    @Override
    public boolean wantNerfedSellPrice() {
        return false;
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.NETHERITE_HELMET);
    }

    @Override
    public int getHealth() {
        return (int) ItemArmor.getHealthFromMaterial(Material.NETHERITE_HELMET)+10;
    }

    public int getStrength() {
        return 25;
    }


    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()/100.0),
                new AdditiveAttributeEntry(AttributeWrapper.OXYGEN_BONUS, NeptuneArmorSet.OXYGEN_BONUS),
                new ScalarAttributeEntry(AttributeWrapper.BURNING_TIME, -.1)
        );
    }

    @Override
    public int getPowerRating() {
        return NeptuneArmorSet.POWER_LEVEL;
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
        return new HelmetRecipe(this, itemService.getCustomItem(CustomItemType.JUPITERS_ARTIFACT), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.JUPITER_CRYSTAL)
        );
    }

}
