package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryFreezeEvent;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.events.CustomItemQuantityRollDropEvent;

public class LootingEnchantment extends VanillaEnchantment implements Listener {


    /**
     * By no means an actual value, simply just an estimation
     *
     * @param level
     * @return
     */
    public static int getLootingPercentEstimation(int level) {
        return level*100;
    }


    public LootingEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.text("Looting");
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.text("Provides a drop bonus of ").color(NamedTextColor.GRAY)
                .append(Component.text("+~" + getLootingPercentEstimation(getLevel()) + "%").color(NamedTextColor.GREEN))
                .append(Component.text(" from mobs").color(NamedTextColor.GRAY));
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getWeight() {
        return 4;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 15;
    }

    @EventHandler
    public void onItemQuantityRoll(CustomItemQuantityRollDropEvent event) {

        int looting = EnchantmentUtil.getEnchantLevel(Enchantment.LOOTING, event.getTool());
        if (looting < 1)
            return;

        int extraDrops = (int) Math.round(Math.random()*looting+1);
        event.setAmount(event.getAmount() + extraDrops);
    }
}
