package xyz.devvydont.smprpg.items.blueprints.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.InterfaceStats;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.listeners.DamageOverrideListener;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;

public class EntityAnalyzer extends CustomItemBlueprint implements Listener {


    public EntityAnalyzer(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        return List.of(
                Component.text("Look at an entity").color(NamedTextColor.GRAY),
                Component.text("and ").color(NamedTextColor.GRAY).append(Component.text("right click", NamedTextColor.GOLD)).append(Component.text(" to analyze").color(NamedTextColor.GRAY)),
                Component.text("its attributes!").color(NamedTextColor.GRAY)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {

        if (!(event.getRightClicked() instanceof LivingEntity living))
            return;

        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());

        if (item.getType().equals(Material.AIR))
            return;

        if (!isItemOfType(item))
            return;

        InterfaceStats gui = new InterfaceStats(SMPRPG.getInstance(), event.getPlayer(), living);
        gui.open();

        if (event.getPlayer().isSneaking()) {
            ItemStack report = itemService.getCustomItem(CustomItemType.ENTITY_ANALYZER_REPORT);

            // Inject the statistics into the meta and tell the plugin to never update this item again
            report.editMeta(meta -> {
                List<Component> components = new ArrayList<>();
                components.addAll(gui.getStats().lore());
                components.addAll(report.lore());
                meta.lore(components);
                meta.setEnchantmentGlintOverride(true);
            });
            itemService.setIgnoreMetaUpdate(report);
            event.getRightClicked().getWorld().dropItemNaturally(((LivingEntity) event.getRightClicked()).getEyeLocation(), report);
            event.getRightClicked().getWorld().playSound(((LivingEntity) event.getRightClicked()).getEyeLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        }
    }

}
