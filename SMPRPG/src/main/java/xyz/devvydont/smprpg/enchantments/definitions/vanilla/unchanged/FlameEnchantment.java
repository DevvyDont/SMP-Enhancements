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

public class FlameEnchantment extends UnchangedEnchantment {

    public FlameEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getSecondsOfBurn(int level) {
        return level * 4;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Flame");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Ignites enemies for "),
            ComponentUtils.create(getSecondsOfBurn(getLevel()) + "s", NamedTextColor.GOLD)
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_BOW;
    }

    @Override
    public int getSkillRequirement() {
        return 19;
    }
}
