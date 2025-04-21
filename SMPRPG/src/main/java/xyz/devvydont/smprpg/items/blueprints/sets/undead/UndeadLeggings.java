package xyz.devvydont.smprpg.items.blueprints.sets.undead;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;

public class UndeadLeggings extends UndeadArmorSet {

    public UndeadLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getHealth() {
        return 25;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(Material.ROTTEN_FLESH), generate()).build();
    }
}
