package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class SwiftSneakEnchantment extends UnchangedEnchantment {

    public static int getSneakPercent(int level) {
        return level * 15 + 30;
    }

    public SwiftSneakEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Swift Sneak");
    }

    @Override
    public Component getDescription() {
        return Component.text("Sneak speed is ").color(NamedTextColor.GRAY)
                .append(Component.text(getSneakPercent(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" of walk speed").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_LEG_ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 35;
    }
}
