package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public class EnchantmentSkillReward extends SkillReward {

    private final CustomEnchantment enchantment;

    public EnchantmentSkillReward(CustomEnchantment enchantment) {
        super(enchantment.getSkillRequirement());
        this.enchantment = enchantment;
    }

    public CustomEnchantment getEnchantment() {
        return enchantment;
    }

    public Component getHoverComponent() {
        CustomEnchantment clone = getEnchantment().build(1);
        CustomEnchantment max = getEnchantment().build(clone.getMaxLevel());
        Component ret = clone.getDisplayName().color(NamedTextColor.LIGHT_PURPLE)
                .append(ComponentUtils.create("\n")
                        .append(clone.getDescription())
                        .append(ComponentUtils.create(" (Lv. 1)", NamedTextColor.DARK_GRAY)));
        if (clone.getMaxLevel() > 1)
            ret = ret.append(ComponentUtils.create("\n").append(max.getDescription()).append(ComponentUtils.create(" (Lv. " + clone.getMaxLevel() + ")", NamedTextColor.DARK_GRAY)));

        ret = ret.append(ComponentUtils.create("\n\n"));
        ret = ret.append(ComponentUtils.create("Item Type: ").append(ComponentUtils.create(MinecraftStringUtils.getTitledString(clone.getItemTypeTag().key().asMinimalString().replace("/", " ")), NamedTextColor.GOLD)));
        return ret;
    }

    @Override
    public Component getDisplayName() {
        return ComponentUtils.create("Unlocked ").decoration(TextDecoration.BOLD, false)
                .append(enchantment.getDisplayName().color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true).hoverEvent(getHoverComponent()));
    }
}
