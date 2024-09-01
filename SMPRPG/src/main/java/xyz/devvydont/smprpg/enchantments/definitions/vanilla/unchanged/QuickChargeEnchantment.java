package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class QuickChargeEnchantment extends UnchangedEnchantment {

    public static int getChargePercentageReduction(int level) {
        return level * 25;
    }

    public QuickChargeEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Quick Charge");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Sets loading time to "),
            ComponentUtils.create(
                "-" + getChargePercentageReduction(getLevel()) + "%",
                getLevel() >= 5 ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.GREEN
            )
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_CROSSBOW;
    }

    @Override
    public int getSkillRequirement() {
        return 37;
    }
}
