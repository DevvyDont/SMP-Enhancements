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

public class RespirationEnchantment extends UnchangedEnchantment {

    public static int getAdditionalBreath(int level) {
        return level * 15;
    }

    public RespirationEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Respiration");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Increases lung capacity by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getAdditionalBreath(getLevel()) + "s").color(NamedTextColor.GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_HEAD_ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 25;
    }
}
