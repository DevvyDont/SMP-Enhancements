package xyz.devvydont.smprpg.util.crafting;


/**
 * Util class to help with constructing Compressable chains of items.
 * Accepts either vanilla MATERIAL or our CUSTOMITEMTYPE
 */
public class CompressionRecipeMember {

    private int amount = 9;
    private MaterialWrapper material;

    public CompressionRecipeMember(MaterialWrapper material) {
        this.material = material;
    }

    public CompressionRecipeMember(MaterialWrapper material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    /**
     * Amount of items that is required to compress to its next tier
     *
     * @return
     */
    public int getAmount() {
        return amount;
    }

    public MaterialWrapper getMaterial() {
        return material;
    }

    /**
     * Determines a unique recipe name to associate with the compression recipe of this item
     * Can pass in the compress flag as false to instead generate a key for the decompression recipe
     */
    public String generateRecipeName(boolean compress) {

        // Handle the case we are compressing
        if (compress)
            return material.key() + "-compression";

        // Handle the case we are decompressing
        return material.key() + "-decompression";
    }

    public String generateRecipeName() {
        return generateRecipeName(true);
    }
}
