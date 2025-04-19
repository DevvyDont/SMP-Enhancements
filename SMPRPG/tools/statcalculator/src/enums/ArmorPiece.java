package enums;

import java.util.Arrays;

public enum ArmorPiece {

    HELMET(21),
    CHESTPLATE(36),
    LEGGINGS(28),
    BOOTS(15)
    ;

    /**
     * Sums all the weights of the pieces together to be used for weight distribution calculations.
     */
    public static final int TOTAL_WEIGHT = Arrays.stream(ArmorPiece.values()).mapToInt(piece -> piece.Weight).sum();

    /**
     * The individual contribution of stats this armor piece should contribute to.
     */
    public final int Weight;

    ArmorPiece(int weight) {
        Weight = weight;
    }

    /**
     * Calculate how much of the given stat this piece should have based on its rarity.
     * Note that the stat pool parameter should be the TOTAL ARMOR stat allocation, and should
     * not include any other sources (such as skills or enchantments).
     *
     * An example usage of this function is working out in another part of the program that a
     * level 25 player should have 200 health from armor, then inquiring this function on how much health
     * level 25 legendary leggings should have. We would call this function using:
     * ArmorPiece.LEGGINGS.calculateStatTarget(200, ItemRarity.LEGENDARY)
     * and be met with the result of .28 * 200 * 1.2 (weight% * total pool * rarity budget) =~ 67
     * @param totalStatPool The total amount of stats the entire armor set should contribute to.
     * @param rarity The rarity of the armor. Effects the base effectiveness to give the armor.
     * @return What stat to give this armor based on the given total stat pool and rarity.
     */
    public double calculateStatTarget(double totalStatPool, ItemRarity rarity) {
        float weight = (float)this.Weight / TOTAL_WEIGHT;
        return weight * totalStatPool * rarity.Budget;
    }
}
