package xyz.devvydont.smprpg.items.blueprints.vanilla;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemEnchantedBook extends VanillaItemBlueprint implements IHeaderDescribable {

    public ItemEnchantedBook(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        var footer = new ArrayList<Component>();
        footer.add(ComponentUtils.EMPTY);
        footer.add(ComponentUtils.create("Combine this with either"));
        footer.add(ComponentUtils.create("equipment or another"));
        footer.add(ComponentUtils.create("Enchanted Book", NamedTextColor.LIGHT_PURPLE).append(ComponentUtils.create(" of")));
        footer.add(ComponentUtils.create("the same type in an anvil!"));
        return footer;
    }

    /**
     * Books can only contain one enchantment.
     */
    @Override
    public int getMaxAllowedEnchantments(ItemStack item) {
        return 1;
    }

    @Nullable
    public Enchantment getEnchantment(ItemStack item) {

        var meta = item.getItemMeta();

        if (!(meta instanceof EnchantmentStorageMeta storage))
            return null;

        if (storage.getStoredEnchants().isEmpty())
            return null;

        return storage.getStoredEnchants().keySet().iterator().next();
    }

    @Override
    public List<Component> getEnchantsComponent(ItemStack item) {

        var meta = item.getItemMeta();

        // Get a copy of this item meta as if it were enchanted the normal way
        EnchantmentStorageMeta clone = (EnchantmentStorageMeta) meta.clone();
        for (Map.Entry<Enchantment, Integer> entry : clone.getStoredEnchants().entrySet())
            clone.addEnchant(entry.getKey(), entry.getValue(), true);
        var cloneItem = item.clone();
        cloneItem.setItemMeta(clone);

        return super.getEnchantsComponent(cloneItem);
    }

    public EnchantmentStorageMeta getMeta(ItemMeta meta) {
        return (EnchantmentStorageMeta) meta;
    }

    /**
     * Make this book have an item name reflecting the enchantment that is stored on it
     */
    @Override
    public String getItemName(ItemStack item) {

        var meta = item.getItemMeta();

        // If we have more than one enchantment, we don't have the proper update to do this yet
        EnchantmentStorageMeta enchantmentMeta = getMeta(meta);
        if (enchantmentMeta.getStoredEnchants().size() > 1)
            return super.getItemName(item);

        // Edge case, is there somehow no enchant on this book?
        if (enchantmentMeta.getStoredEnchants().isEmpty())
            return "Enchanted Book (???)";

        Enchantment enchantment = enchantmentMeta.getStoredEnchants().keySet().iterator().next();
        int level = enchantmentMeta.getStoredEnchants().get(enchantment);
        return String.format("Enchanted Book (%s)", PlainTextComponentSerializer.plainText().serialize(enchantment.displayName(level)));
    }

    /**
     * Try to guesstimate how rare this book is based on what enchantments it has stored on it.
     */
    @Override
    public ItemRarity getRarity(ItemStack item) {

        var meta = item.getItemMeta();

        // If this item isn't setup properly then do default behavior
        if (getMeta(meta).getStoredEnchants().size() > 1 || getMeta(meta).getStoredEnchants().isEmpty())
            return super.getRarity(item);

        // Get the enchantment stored on this item and analyze its properties
        Enchantment enchantment = getMeta(meta).getStoredEnchants().keySet().iterator().next();
        int level = getMeta(meta).getStoredEnchants().get(enchantment);

        // If the level is over the level cap for the enchantment, this book is divine
        if (level > enchantment.getMaxLevel())
            return ItemRarity.DIVINE;

        // If this enchantment is at its max level but is lower than lvl 3, it is rare
        if (level == enchantment.getMaxLevel() && level < 3)
            return ItemRarity.RARE;

        // Lerp the rarity from uncommon to legendary based on what level the enchantment is
        return switch (level) {
            case 0 -> ItemRarity.COMMON;
            case 1, 2 -> ItemRarity.UNCOMMON;
            case 3, 4 -> ItemRarity.RARE;
            case 5, 6, 7 -> ItemRarity.EPIC;
            case 8, 9 -> ItemRarity.LEGENDARY;
            case 10 -> ItemRarity.MYTHIC;
            default -> ItemRarity.DIVINE;
        };
    }

    @Override
    public ItemRarity getDefaultRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public void updateItemData(ItemMeta meta) {
        super.updateItemData(meta);
        meta.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS);

        // Enchanted books can only have one enchantment on them. Pick a random one if it has multiple
        if (meta instanceof EnchantmentStorageMeta enchantmentStorageMeta && enchantmentStorageMeta.getStoredEnchants().size() > 1) {
            Enchantment toKeep = enchantmentStorageMeta.getStoredEnchants().keySet().stream().toList().get((int) (Math.random()*enchantmentStorageMeta.getStoredEnchants().size()));
            int level = enchantmentStorageMeta.getStoredEnchants().get(toKeep);

            // Remove all enchants
            for (Enchantment enchantment : enchantmentStorageMeta.getStoredEnchants().keySet().stream().toList())
                enchantmentStorageMeta.removeStoredEnchant(enchantment);

            // Apply the one we want to keep and reupdate the meta
            enchantmentStorageMeta.addStoredEnchant(toKeep, level, true);
            updateItemData(meta);
        }
    }
}
