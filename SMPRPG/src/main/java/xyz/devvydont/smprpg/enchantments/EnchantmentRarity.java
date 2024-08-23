package xyz.devvydont.smprpg.enchantments;

/*
 * Wrapper enum class for enchantments to use. Mainly used as a preset for weight calculations if desired
 */
public enum EnchantmentRarity {

    COMMON(5),
    UNCOMMON(4),
    RARE(3),

    CURSE(2),     // Bad enchants, kinda rare kinda not usually disappear after certain levels anyway
    BLESSING(1),  // Rarest enchants to get
    ARTIFACT(0);  // Unobtainable

    private int weight;

    EnchantmentRarity(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

}
