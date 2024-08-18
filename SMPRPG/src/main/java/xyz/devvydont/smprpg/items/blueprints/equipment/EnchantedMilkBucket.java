package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.ChargedItemBlueprint;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.List;

public class EnchantedMilkBucket extends ChargedItemBlueprint implements Listener {

    public EnchantedMilkBucket(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        return List.of(
                ComponentUtil.getDefaultText("Consume to cleanse ").append(ComponentUtil.getColoredComponent("ALL", NamedTextColor.GOLD)),
                ComponentUtil.getDefaultText("status effects you have!")
        );
    }

    @Override
    public int maxCharges(ItemMeta meta) {
        return 12;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @EventHandler
    public void onConsumeEnchantedMilk(PlayerItemConsumeEvent event) {

        if (!isItemOfType(event.getItem()))
            return;

        ItemStack milk = event.getItem();
        useCharge(event.getPlayer(), milk);
        event.getPlayer().getInventory().setItem(event.getHand(), milk);
        event.setCancelled(true);
        event.getPlayer().clearActivePotionEffects();
        SMPRPG.getInstance().getActionBarService().addActionBarComponent(event.getPlayer(), ActionBarService.ActionBarSource.MISC, ComponentUtil.getColoredComponent("CLEANSED!", NamedTextColor.GREEN), 2);
    }
}
