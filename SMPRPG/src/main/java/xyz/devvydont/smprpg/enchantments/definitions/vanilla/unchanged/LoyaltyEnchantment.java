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

public class LoyaltyEnchantment extends UnchangedEnchantment {

    public static Component getSpeedModifier(int level) {
        return switch (level) {
            case 0 -> ComponentUtils.create("");
            case 1 -> ComponentUtils.create("slowly", NamedTextColor.YELLOW);
            case 2 -> ComponentUtils.create("quickly", NamedTextColor.GREEN);
            case 3 -> ComponentUtils.create("very quickly", NamedTextColor.LIGHT_PURPLE);
            default -> ComponentUtils.create("AT LIGHTSPEED", NamedTextColor.LIGHT_PURPLE);
        };
    }

    public LoyaltyEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Loyalty");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.create("Returns when thrown ", NamedTextColor.GRAY).append(getSpeedModifier(getLevel()));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_TRIDENT;
    }

    @Override
    public int getSkillRequirement() {
        return 31;
    }

}
