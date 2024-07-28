package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;

public class RiptideEnchantment extends UnchangedEnchantment {

    public static int getBlocksThrown(int level) {
        return (4 * level) + 3;
    }


    public RiptideEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Riptide");
    }

    @Override
    public Component getDescription() {
        return Component.text("Propels you ").color(NamedTextColor.GRAY)
                .append(Component.text(getBlocksThrown(getLevel())).color(NamedTextColor.GREEN))
                .append(Component.text(" blocks when used").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_TRIDENT;
    }

    @Override
    public int getSkillRequirement() {
        return 58;
    }
}
