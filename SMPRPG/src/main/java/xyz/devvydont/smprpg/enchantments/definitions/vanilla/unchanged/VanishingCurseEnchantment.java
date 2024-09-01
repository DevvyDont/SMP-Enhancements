package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class VanishingCurseEnchantment extends UnchangedEnchantment {

    public VanishingCurseEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Curse of Vanishing");
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return NamedTextColor.RED;
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Vanishes when "),
            ComponentUtils.create("dropped from death", NamedTextColor.DARK_RED)
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_VANISHING;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }

    @Override
    public int getSkillRequirementToAvoid() {
        return 10;
    }
}
