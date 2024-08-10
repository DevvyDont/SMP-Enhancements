package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class SoulSpeedEnchantment extends UnchangedEnchantment {

    public static int getSoulSpeedPercentage(int level) {
        return (int) (((level * 0.105) + .3) * 100);
    }


    public SoulSpeedEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Soul Speed");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases speed by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getSoulSpeedPercentage(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" on ").color(NamedTextColor.GRAY))
                .append(Component.text("soul sand/soil").color(NamedTextColor.GOLD));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_FOOT_ARMOR;
    }

    @Override
    public int getSkillRequirement() {
        return 23;
    }
}
