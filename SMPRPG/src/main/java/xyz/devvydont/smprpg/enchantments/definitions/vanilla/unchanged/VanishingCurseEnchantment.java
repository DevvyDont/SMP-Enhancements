package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class VanishingCurseEnchantment extends UnchangedEnchantment {

    public VanishingCurseEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Curse of Vanishing");
    }

    @Override
    public TextColor getEnchantColor() {
        return NamedTextColor.RED;
    }

    @Override
    public Component getDescription() {
        return Component.text("Vanishes when ").color(NamedTextColor.GRAY)
                .append(Component.text("dropped from death").color(NamedTextColor.DARK_RED));
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
