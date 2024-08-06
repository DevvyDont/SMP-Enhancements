package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.events.CustomItemDropRollEvent;

public class FortuityEnchantment extends CustomEnchantment implements Listener {

    public static float getChanceIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> .10f;
            case 2 -> .25f;
            case 3 -> .5f;
            case 4 -> .9f;
            case 5 -> 1.5f;
            default -> getChanceIncrease(5) + .5f * (level-5);
        };
    }


    public FortuityEnchantment(String id) {
        super(id);
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Fortuity");
    }

    @Override
    public Component getDescription() {
        return Component.text("Increases rare item drop chance by ").color(NamedTextColor.GRAY)
                .append(Component.text("+" + (int)(getChanceIncrease(getLevel())*100) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" from mobs").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_WEAPON;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMinimumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EnchantmentRegistryEntry.EnchantmentCost getMaximumCost() {
        return EnchantmentRegistryEntry.EnchantmentCost.of(1, 1);
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 17;
    }

    @EventHandler
    public void onItemRoll(CustomItemDropRollEvent event) {

        int fortuity = EnchantmentUtil.getEnchantLevel(getEnchantment(), event.getTool());
        if (fortuity < 1)
            return;

        float multiplier = getChanceIncrease(fortuity) + 1.0f;
        event.setChance(event.getChance() * multiplier);
    }
}
