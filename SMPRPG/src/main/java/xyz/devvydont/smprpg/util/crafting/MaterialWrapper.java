package xyz.devvydont.smprpg.util.crafting;


import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

/**
 * Util class to streamline working with vanilla Material and our CustomItemType
 */
public class MaterialWrapper {

    private Material vanilla = null;
    private CustomItemType custom = null;

    public MaterialWrapper(Material vanilla) {
        this.vanilla = vanilla;
    }

    public MaterialWrapper(CustomItemType custom) {
        this.custom = custom;
    }

    public Material getVanilla() {
        return vanilla;
    }

    public CustomItemType getCustom() {
        return custom;
    }

    public boolean isCustom() {
        return this.custom != null;
    }

    public boolean isVanilla() {
        return this.vanilla != null;
    }

    /**
     * Resolve a display safe name of this material
     *
     * @return
     */
    public String name() {

        if (isCustom())
            return custom.name;

        return MinecraftStringUtils.getTitledString(vanilla.name());
    }

    /**
     * Resolve the component that displays for the item title
     *
     * @return
     */
    public Component component() {
        if (isCustom())
            return custom.rarity.applyDecoration(Component.text(name()));

        return ItemRarity.ofVanillaMaterial(vanilla).applyDecoration(Component.text(name()));
    }

    /**
     * Resolve a string version of the enum to use for key based purposes
     *
     * @return
     */
    public String key() {
        if (isCustom())
            return custom.name().toLowerCase();
        return vanilla.name().toLowerCase();
    }

    /**
     * Resolves the "material" this item will render as. (Custom items still have an associated vanilla material)
     *
     * @return
     */
    public Material material() {

        if (isVanilla())
            return vanilla;

        return custom.material;
    }

    /**
     * Given an item service, actually create an item stack.
     *
     * @param itemService
     * @return
     */
    public ItemStack get(ItemService itemService) {

        if (isCustom())
            return itemService.getCustomItem(custom);

        ItemStack item = new ItemStack(vanilla);
        itemService.ensureItemStackUpdated(item);
        return item;
    }
}
