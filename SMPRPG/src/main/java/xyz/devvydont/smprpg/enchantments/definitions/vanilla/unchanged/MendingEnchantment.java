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

public class MendingEnchantment extends UnchangedEnchantment {


    public MendingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Mending");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Repaired when ").color(NamedTextColor.GRAY)
                .append(Component.text("earning experience").color(NamedTextColor.DARK_GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_DURABILITY;
    }

    @Override
    public int getSkillRequirement() {
        return 30;
    }
}
