package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class SilkTouchEnchantment extends UnchangedEnchantment {

    public SilkTouchEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Silk Touch");
    }

    @Override
    public Component getDescription() {
        return Component.text("Mines blocks for their ").color(NamedTextColor.GRAY)
                .append(Component.text("pure").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" form").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_MINING;
    }

    @Override
    public int getSkillRequirement() {
        return 20;
    }
}
