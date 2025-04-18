package xyz.devvydont.treasureitems.blueprints.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import xyz.devvydont.treasureitems.blueprints.CustomItemBlueprint;
import xyz.devvydont.treasureitems.util.ComponentUtils;

import java.util.List;

public class StrengthCharmBlueprint extends CustomItemBlueprint {

    public static final float STRENGTH_MIN_BOUND = .1f;
    public static final float STRENGTH_MAX_BOUND = 1f;

    private float getRandomStrength() {
        float r = (float) (Math.random() * (STRENGTH_MAX_BOUND - STRENGTH_MIN_BOUND) + STRENGTH_MIN_BOUND);
        // Round it to the nearest hundredths place
        return (float) Math.round(r * 100) / 100;
    }

    public float getStrength(ItemStack itemStack) {

        try {
            return (float) itemStack.getItemMeta().getAttributeModifiers(Attribute.ATTACK_DAMAGE).stream().findFirst().get().getAmount();
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        Repairable itemMeta = (Repairable) item.getItemMeta();

        itemMeta.displayName(Component.text("Strength Charm", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

        itemMeta.setEnchantmentGlintOverride(true);

        float str = getRandomStrength();
        itemMeta.addAttributeModifier(Attribute.ATTACK_DAMAGE, generateAttributeModifier(Attribute.ATTACK_DAMAGE, EquipmentSlotGroup.HAND, AttributeModifier.Operation.ADD_SCALAR, str));

        itemMeta.setRepairCost(1000);
        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    public int calculateScore(ItemStack itemStack) {
        // Return a number from 0-100 depending on the speed of the item
        return (int) Math.floor((getStrength(itemStack) - STRENGTH_MIN_BOUND) / (STRENGTH_MAX_BOUND - STRENGTH_MIN_BOUND) * 100);
    }

    @Override
    protected List<Component> getExtraLore() {

        return List.of(
                ComponentUtils.merge(Component.text("Hold to become ", NamedTextColor.GRAY), Component.text("strong", NamedTextColor.RED), Component.text("!", NamedTextColor.GRAY)),
                Component.empty(),
                Component.text("Also functions as a normal", NamedTextColor.GRAY),
                ComponentUtils.merge(Component.text("Totem of Undying", NamedTextColor.YELLOW), Component.text("!", NamedTextColor.GRAY))
        );
    }

    @Override
    public String key() {
        return "strength_totem";
    }

}
