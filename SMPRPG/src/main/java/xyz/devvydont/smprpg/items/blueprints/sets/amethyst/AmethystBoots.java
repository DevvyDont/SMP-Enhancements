package xyz.devvydont.smprpg.items.blueprints.sets.amethyst;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe;

public class AmethystBoots extends AmethystArmorSet {


    public AmethystBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.FEET;
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.LEATHER_BOOTS);
    }

    @Override
    public int getHealth() {
        return (int) ItemArmor.getHealthFromMaterial(Material.DIAMOND_BOOTS);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.SPIRE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new BootsRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_AMETHYST), generate()).build();
    }
}
