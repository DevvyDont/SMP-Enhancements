package xyz.devvydont.smprpg.items.base;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

/**
 * Wrapper class for vanilla items to use so we don't have to establish 90% of the vanilla items in the game
 */
public class VanillaItemBlueprint extends SMPItemBlueprint {

    ItemStack item;

    public VanillaItemBlueprint(ItemService itemService, ItemStack item) {
        super(itemService);
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    /**
     * Vanilla items always use the vanilla classification resolver.
     */
    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.resolveVanillaMaterial(item.getType());
    }

    @Override
    public ItemRarity getRarity(ItemMeta meta) {
        return getDefaultRarity();
    }

    /**
     * Vanilla items always use the vanilla classification resolver.
     */
    @Override
    public ItemRarity getDefaultRarity() {
        return ItemRarity.ofVanillaMaterial(item.getType());
    }

    @Override
    public String getItemName(ItemMeta meta) {
        return MinecraftStringUtils.getTitledString(item.getType().name());
    }

    @Override
    public boolean wantFakeEnchantGlow() {
        return false;
    }

    @Override
    public boolean isItemOfType(ItemStack itemStack) {
        return this.item.isSimilar(itemStack);
    }

    /**
     * All items that descend from this class are vanilla.
     */
    @Override
    public boolean isCustom() {
        return false;
    }

    @Override
    public ItemStack generate() {
        ItemStack newItem = new ItemStack(item.getType());
        updateMeta(item);
        return newItem;
    }

}
