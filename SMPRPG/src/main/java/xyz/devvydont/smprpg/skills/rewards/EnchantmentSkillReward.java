package xyz.devvydont.smprpg.skills.rewards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class EnchantmentSkillReward extends SkillReward {

    private final CustomEnchantment enchantment;

    public EnchantmentSkillReward(CustomEnchantment enchantment) {
        super(enchantment.getSkillRequirement());
        this.enchantment = enchantment;
    }

    public CustomEnchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public Component getDisplayName() {
        return ComponentUtil.getDefaultText("Unlocked ").append(enchantment.getDisplayName().color(NamedTextColor.LIGHT_PURPLE));
    }
}
