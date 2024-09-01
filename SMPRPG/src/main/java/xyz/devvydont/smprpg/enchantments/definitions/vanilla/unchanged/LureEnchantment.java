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

public class LureEnchantment extends UnchangedEnchantment {

    public static int getDecreaseTime(int level) {
        return level * 5;
    }

    public LureEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_FISHING;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Lure");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Decreases catch time by "),
            ComponentUtils.create("-" + getDecreaseTime(getLevel()) + "s", NamedTextColor.GREEN)
        );
    }

    @Override
    public int getSkillRequirement() {
        return 13;
    }
}
