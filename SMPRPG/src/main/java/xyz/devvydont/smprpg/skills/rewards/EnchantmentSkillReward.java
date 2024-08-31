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
                .append(Component.text("\n")
                        .append(clone.getDescription())
                        .append(Component.text(" (Lv. 1)").color(NamedTextColor.DARK_GRAY)));
        if (clone.getMaxLevel() > 1)
            ret = ret.append(Component.text("\n").append(max.getDescription()).append(Component.text(" (Lv. " + clone.getMaxLevel() + ")").color(NamedTextColor.DARK_GRAY)));

        ret = ret.append(Component.text("\n\n"));
        ret = ret.append(ComponentUtils.getDefaultText("Item Type: ").append(Component.text(MinecraftStringUtils.getTitledString(clone.getItemTypeTag().key().asMinimalString().replace("/", " "))).color(NamedTextColor.GOLD)));
        return ret;
    }

    @Override
    public Component getDisplayName() {
        return ComponentUtils.getDefaultText("Unlocked ").decoration(TextDecoration.BOLD, false)
                .append(enchantment.getDisplayName().color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true).hoverEvent(getHoverComponent()));
    }
}
