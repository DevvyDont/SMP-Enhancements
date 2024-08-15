package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

public class ProficientEnchantment extends CustomEnchantment implements Listener {

    public static final int PERCENT_PER_LEVEL = 10;

    /**
     * Gets the level of proficiency a certain sum of levels will grant. Keep in mind that this enchantment can stack
     * levels from armor, offhand, and main hand meaning that the max possible "level" of this enchantment can be
     * percentPerLevel * maxLevel * 6 (+600% if max level is 10 and we have 10% increase per level)
     *
     * @param level
     * @return
     */
    public static int getProficiencyPercentIncrease(int level) {
        return PERCENT_PER_LEVEL * level;
    }

    public ProficientEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Proficient");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtil.getDefaultText("Increases skill experience gains by ")
                .append(ComponentUtil.getColoredComponent("+" + getProficiencyPercentIncrease(getLevel()) + "%", NamedTextColor.GREEN))
                .append(ComponentUtil.getColoredComponent(" (stacks!)", NamedTextColor.DARK_GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_VANISHING;  // Anything that can be enchanted basically
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getWeight() {
        return 3;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ANY;
    }

    @Override
    public int getSkillRequirement() {
        return 17;
    }

    @EventHandler
    public void onObtainSkillExperience(SkillExperienceGainEvent event) {

        int totalLevels = EnchantmentUtil.getWornEnchantLevel(this, event.getPlayer().getEquipment());
        totalLevels += EnchantmentUtil.getHoldingEnchantLevel(this, EquipmentSlotGroup.HAND, event.getPlayer().getEquipment());
        if (totalLevels <= 0)
            return;

        double exp = event.getExperienceEarned();
        exp *= (getProficiencyPercentIncrease(totalLevels) / 100.0 + 1.0);
        event.setExperienceEarned((int) Math.round(exp));
    }
}