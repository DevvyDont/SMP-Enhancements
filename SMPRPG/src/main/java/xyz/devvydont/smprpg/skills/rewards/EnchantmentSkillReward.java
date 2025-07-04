package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.skills.SkillType;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.create;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.merge;

public class EnchantmentSkillReward implements ISkillReward {

    private final CustomEnchantment enchantment;

    public EnchantmentSkillReward(CustomEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    private Component getHoverComponent() {
        CustomEnchantment clone = enchantment.build(1);
        CustomEnchantment max = enchantment.build(clone.getMaxLevel());
        Component ret = clone.getDisplayName().color(LIGHT_PURPLE)
                .append(create("\n")
                        .append(clone.getDescription())
                        .append(create(" (Lv. 1)", NamedTextColor.DARK_GRAY)));
        if (clone.getMaxLevel() > 1)
            ret = ret.append(create("\n").append(max.getDescription()).append(create(" (Lv. " + clone.getMaxLevel() + ")", NamedTextColor.DARK_GRAY)));

        ret = ret.append(create("\n\n"));
        ret = ret.append(create("Item Type: ").append(create(MinecraftStringUtils.getTitledString(clone.getItemTypeTag().key().asMinimalString().replace("/", " ")), NamedTextColor.GOLD)));
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
        return merge(
                create("Unlocked ").decoration(BOLD, false),
                create(Symbols.SPARKLES, LIGHT_PURPLE),
                enchantment.getDisplayName().color(LIGHT_PURPLE).decoration(BOLD, true).hoverEvent(getHoverComponent()),
                create(" enchantment").decoration(BOLD, false)
        );
    }
}
