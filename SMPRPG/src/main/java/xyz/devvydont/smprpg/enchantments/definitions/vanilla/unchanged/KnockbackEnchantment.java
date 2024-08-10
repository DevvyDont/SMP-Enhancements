package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class KnockbackEnchantment extends UnchangedEnchantment {

    public static int getKnockbackPower(int level) {
        return switch (level) {
          case 0 -> 0;
          case 1 -> 105;
          case 2 -> 190;
          default -> level * 100;
        };
    }

    public KnockbackEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Knockback");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases knockback by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + getKnockbackPower(getLevel()) + "%").color(NamedTextColor.GREEN));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON;
    }

    @Override
    public int getSkillRequirement() {
        return 3;
    }
}
