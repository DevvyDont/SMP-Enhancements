package xyz.devvydont.smprpg.util.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.economy.CustomItemCoin;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Arrays;
import java.util.List;

public class ItemUtil {

    public static final CustomItemType[] COINS = {
            CustomItemType.COPPER_COIN,
            CustomItemType.SILVER_COIN,
            CustomItemType.GOLD_COIN,
            CustomItemType.PLATINUM_COIN,
            CustomItemType.EMERALD_COIN,
            CustomItemType.AMETHYST_COIN,
            CustomItemType.ENCHANTED_COIN
    };

    /**
     * Given an amount of emeralds, return how many coins we should return
     *
     * @param emeralds
     */
    public static int emeraldsToCoins(int emeralds) {
        return (int) Math.round(Math.pow(emeralds+2, 2.5));
    }

    /**
     * Given a number of emeralds, return an item stack of coins that is worth
     *
     * @return
     */
    public static ItemStack determineBestVillagerCurrencyConversion(ItemService itemService, int emeralds) {

        int coinTarget = emeraldsToCoins(emeralds);

        // Loop through all the coins types and do math to see which coin we can make have the highest stack size
        // while still capturing all the value of the emeralds.
        for (CustomItemType coinType : COINS) {

            CustomItemCoin coin = (CustomItemCoin) itemService.getBlueprint(coinType);

            // If this coin is unable to capture the full value of the emeralds in 50, skip
            if (coin.getValue() * 50 < coinTarget)
                continue;

            // We have a good coin to use.

            int stackSize = (int) Math.ceil((double)coinTarget / coin.getValue());
            ItemStack coinItem = coin.generate();
            coinItem.setAmount(stackSize);
            return coinItem;
        }

        // Oh boy we failed to find a suitable coin to satisfy a trade worth 50 million coins.....
        SMPRPG.getPlugin(SMPRPG.class).getLogger().severe(String.format("Failed to convert trade of %d emeralds to coins. Defaulting to 60m coins!", emeralds));
        ItemStack coin = itemService.getBlueprint(CustomItemType.ENCHANTED_COIN).generate();
        coin.setAmount(60);
        return coin;
    }

    /**
     * Given an item stack that is meant to be present in a villager trade, return a replacement for the item
     * if it is needed. If the item is valid, it is simply returned back.
     * Mainly used to convert emeralds into coins.
     *
     * @param itemService
     * @param itemStack
     * @return
     */
    public static ItemStack checkVillagerItem(ItemService itemService, ItemStack itemStack) {

        // We are only checking for vanilla emeralds, eliminate any other case
        SMPItemBlueprint blueprint = itemService.getBlueprint(itemStack);

        // Custom items are fine to stay
        if (!blueprint.isCustom())
            return itemStack;

        // If the vanilla item we have is not an emerald, it is fine to stay
        if (!itemStack.getType().equals(Material.EMERALD))
            return itemStack;

        // This emerald should be converted to coins
        return determineBestVillagerCurrencyConversion(itemService, itemStack.getAmount());
    }

    public static List<Material> getDiamondEquipment() {
        return Arrays.asList(
                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_SWORD,
                Material.DIAMOND_AXE,
                Material.DIAMOND_PICKAXE,
                Material.DIAMOND_SHOVEL,
                Material.DIAMOND_HOE
        );
    }

    public static List<Material> getNetheriteEquipment() {
        return Arrays.asList(
                Material.NETHERITE_HELMET,
                Material.NETHERITE_CHESTPLATE,
                Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_BOOTS,
                Material.NETHERITE_SWORD,
                Material.NETHERITE_AXE,
                Material.NETHERITE_PICKAXE,
                Material.NETHERITE_SHOVEL,
                Material.NETHERITE_HOE
        );
    }

}
