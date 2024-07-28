package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class QuickChargeEnchantment extends UnchangedEnchantment {

    public static int getChargePercentageReduction(int level) {
        return level * 25;
    }

    public QuickChargeEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Quick Charge");
    }

    @Override
    public Component getDescription() {
        return Component.text("Sets loading time to ").color(NamedTextColor.GRAY)
                .append(Component.text("-" + getChargePercentageReduction(getLevel()) + "%").color(getLevel() >= 5 ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_CROSSBOW;
    }

    @Override
    public int getSkillRequirement() {
        return 44;
    }
}
