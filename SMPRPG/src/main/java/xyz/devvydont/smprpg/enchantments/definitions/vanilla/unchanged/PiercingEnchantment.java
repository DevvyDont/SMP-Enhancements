package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class PiercingEnchantment extends UnchangedEnchantment {

    public PiercingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Piercing");
    }

    @Override
    public Component getDescription() {
        return Component.text("Pierces through ").color(NamedTextColor.GRAY)
                .append(Component.text(getLevel()).color(NamedTextColor.GREEN))
                .append(Component.text(" enemy(s)").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_CROSSBOW;
    }

    @Override
    public int getSkillRequirement() {
        return 24;
    }
}
