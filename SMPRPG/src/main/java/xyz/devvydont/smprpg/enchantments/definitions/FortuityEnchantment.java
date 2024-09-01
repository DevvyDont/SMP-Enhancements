package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.events.CustomItemDropRollEvent;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

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
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Fortuity");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases rare item drop chance by "),
            ComponentUtils.create("+" + (int)(getChanceIncrease(getLevel())*100) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" from mobs")
        );
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
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 25;
    }

    @EventHandler
    public void onItemRoll(CustomItemDropRollEvent event) {

        int fortuity = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.HAND, event.getPlayer().getEquipment());
        if (fortuity < 1)
            return;

        float multiplier = getChanceIncrease(fortuity) + 1.0f;
        event.setChance(event.getChance() * multiplier);
    }
}
