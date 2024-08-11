package xyz.devvydont.smprpg.items.blueprints.vanilla;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemEnchantedBook extends VanillaItemBlueprint {


    public ItemEnchantedBook(ItemService itemService, ItemStack item) {
        super(itemService, item);
    }

    /**
     * Books can only contain one enchantment.
     *
     * @param meta
     * @return
     */
    @Override
    public int getMaxAllowedEnchantments(ItemMeta meta) {
        return 1;
    }

    @Override
    public boolean hasEnchants(ItemMeta meta) {
        return getMeta(meta).hasStoredEnchants();
    }

    @Override
    public List<Component> getEnchantsComponent(ItemMeta meta) {

        // Get a copy of this item meta as if it were enchanted the normal way
        EnchantmentStorageMeta clone = (EnchantmentStorageMeta) meta.clone();
        for (Map.Entry<Enchantment, Integer> entry : clone.getStoredEnchants().entrySet())
            clone.addEnchant(entry.getKey(), entry.getValue(), true);

        return super.getEnchantsComponent(clone);
    }

    public EnchantmentStorageMeta getMeta(ItemMeta meta) {
        return (EnchantmentStorageMeta) meta;
    }

    /**
     * Make this book have an item name reflecting the enchantment that is stored on it
     *
     * @param meta
     * @return
     */
    @Override
    public String getItemName(ItemMeta meta) {

        // If we have more than one enchantment, we don't have the proper update to do this yet
        EnchantmentStorageMeta enchantmentMeta = getMeta(meta);
        if (enchantmentMeta.getStoredEnchants().size() > 1)
            return super.getItemName(meta);

        // Edge case, is there somehow no enchant on this book?
        if (enchantmentMeta.getStoredEnchants().isEmpty())
            return "Enchanted Book (???)";

        Enchantment enchantment = enchantmentMeta.getStoredEnchants().keySet().iterator().next();
        int level = enchantmentMeta.getStoredEnchants().get(enchantment);
        return String.format("Enchanted Book (%s)", PlainTextComponentSerializer.plainText().serialize(enchantment.displayName(level)));
    }

    /**
     * Try to guesstimate how rare this book is based on what enchantments it has stored on it.
     *
     * @param meta
     * @return
     */
    @Override
    public ItemRarity getRarity(ItemMeta meta) {

        // If this item isn't setup properly then do default behavior
        if (getMeta(meta).getStoredEnchants().size() > 1 || getMeta(meta).getStoredEnchants().isEmpty())
            return super.getRarity(meta);

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
    public ItemRarity getRarity(ItemStack item) {
        return getRarity(item.getItemMeta());
    }

    @Override
    public ItemRarity getDefaultRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public List<Component> getFooterComponent(ItemMeta meta) {
        List<Component> footer = new ArrayList<>(super.getFooterComponent(meta));
        footer.add(Component.empty());
        footer.add(ComponentUtil.getDefaultText("Combine this with either"));
        footer.add(ComponentUtil.getDefaultText("equipment or another"));
        footer.add(ComponentUtil.getColoredComponent("Enchanted Book", NamedTextColor.LIGHT_PURPLE).append(ComponentUtil.getDefaultText(" of")));
        footer.add(ComponentUtil.getDefaultText("the same type in an anvil!"));
        return footer;
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
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
            updateMeta(meta);
        }
    }
}
