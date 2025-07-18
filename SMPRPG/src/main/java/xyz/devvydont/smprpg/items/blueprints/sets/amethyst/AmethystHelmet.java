package xyz.devvydont.smprpg.items.blueprints.sets.amethyst;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;

public class AmethystHelmet extends AmethystArmorSet {

    public AmethystHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.LEATHER_HELMET);
    }

    @Override
    public int getHealth() {
        return (int) ItemArmor.getHealthFromMaterial(Material.DIAMOND_HELMET);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HEAD;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.DUNE;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_AMETHYST), generate()).build();
    }
}
