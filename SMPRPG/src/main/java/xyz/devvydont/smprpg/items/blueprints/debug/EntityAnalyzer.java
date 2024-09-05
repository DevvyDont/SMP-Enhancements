package xyz.devvydont.smprpg.items.blueprints.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.gui.player.InterfaceStats;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityAnalyzer extends CustomItemBlueprint implements Listener {


    public EntityAnalyzer(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        return List.of(
                ComponentUtils.create("Look at an entity"),
                ComponentUtils.create("and ").append(ComponentUtils.create("right click", NamedTextColor.GOLD)).append(ComponentUtils.create(" to analyze")),
                ComponentUtils.create("its attributes!")
        );
    }

    public void execute(Player player, LivingEntity entity) {
        var gui = new InterfaceStats(SMPRPG.getInstance(), player, entity);
        gui.openMenu();

        if (player.isSneaking()) {
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
            entity.getWorld().dropItemNaturally(entity.getEyeLocation(), report);
            entity.getWorld().playSound(entity.getEyeLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        }

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

        execute(event.getPlayer(), living);
    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity living))
            return;

        if (!(event.getDamager() instanceof Player player))
            return;

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().equals(Material.AIR))
            return;

        if (!isItemOfType(item))
            return;

        execute(player, living);
    }

}
