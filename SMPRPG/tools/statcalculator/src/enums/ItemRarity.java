package enums;

public enum ItemRarity {

    COMMON(.8f),
    UNCOMMON(.9f),
    RARE(1),
    EPIC(1.1f),
    LEGENDARY(1.2f),
    MYTHIC(1.3f),
    DIVINE(1.4f);

    /**
     * The total allotted gear "budget" for this rarity.
     * This allows gear with same level but different rarities to have varied stats that make sense based on its
     * rarity, meaning a legendary lvl. 20 chestplate will be 20% better than a lvl. 20 rare one by default.
     * Keep in mind there are more benefits to rarities in game, such as enchantment slots and reforge effectiveness.
     */
    public final float Budget;

    ItemRarity(float budget) {
        this.Budget = budget;
    }
}
