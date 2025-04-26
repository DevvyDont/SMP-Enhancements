package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

public class EnchantmentSkillReward implements ISkillReward {

    private final CustomEnchantment enchantment;

    public EnchantmentSkillReward(CustomEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    private Component getHoverComponent() {
        CustomEnchantment clone = enchantment.build(1);
        CustomEnchantment max = enchantment.build(clone.getMaxLevel());
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
    public void remove(Player player, SkillType skill) {

    }

    @Override
    public void apply(Player player, SkillType skill) {

    }

    @Override
    public Component generateRewardComponent(Player player) {
        return ComponentUtils.create("Unlocked ").decoration(TextDecoration.BOLD, false)
                .append(enchantment.getDisplayName().color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, true).hoverEvent(getHoverComponent()));
    }
}
