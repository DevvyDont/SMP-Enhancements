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
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.listeners.DamageOverrideListener;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;

public class EntityAnalyzer extends CustomItemBlueprint implements Listener {


    public EntityAnalyzer(ItemService itemService) {
        super(itemService);
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
    public CustomItemType getCustomItemType() {
        return CustomItemType.ENTITY_ANALYZER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    public List<Component> getStats(LivingEntity entity) {

        List<Component> messages = new ArrayList<>();
        SMPRPG plugin = SMPRPG.getInstance();

        messages.add(entity.name().append(Component.text("'s stats").color(NamedTextColor.GRAY)));
        messages.add(Component.empty());

        // Add the power rating
        messages.add(Component.text("Power Rating: ").color(NamedTextColor.GOLD).append(Component.text(Symbols.POWER + plugin.getEntityService().getEntityInstance(entity).getLevel()).color(NamedTextColor.YELLOW)));
        messages.add(Component.empty());

        double hp = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        int def = plugin.getEntityService().getEntityInstance(entity).getDefense();
        double ehp = DamageOverrideListener.calculateEffectiveHealth(hp, def);

        for (AttributeWrapper attributeWrapper : AttributeWrapper.values()) {

            // We can skip attributes we don't have
            if (entity.getAttribute(attributeWrapper.getAttribute()) == null)
                continue;

            NamedTextColor numberColor = NamedTextColor.DARK_GRAY;
            double attributeValue = entity.getAttribute(attributeWrapper.getAttribute()).getValue();
            double baseAttributeValue = entity.getAttribute(attributeWrapper.getAttribute()).getBaseValue();
            if (attributeValue > baseAttributeValue)
                numberColor = NamedTextColor.GREEN;
            if (attributeValue < baseAttributeValue)
                numberColor = NamedTextColor.RED;

            NamedTextColor attributeNameColor = NamedTextColor.GOLD;
            if (attributeWrapper.getAttributeType().equals(AttributeWrapper.AttributeType.SPECIAL))
                attributeNameColor = NamedTextColor.LIGHT_PURPLE;

            double deltaDiff = (baseAttributeValue - attributeValue) / baseAttributeValue * 100 * -1;
            Component deltaPercentComponent = Component.text(String.format(" (%s%.2f%%)",deltaDiff > 0 ? "+" : "", deltaDiff)).color(deltaDiff > 0 ? NamedTextColor.AQUA : NamedTextColor.DARK_RED);
            if (deltaDiff == 0 || Double.isNaN(deltaDiff) || Double.isInfinite(deltaDiff))
                deltaPercentComponent = Component.empty();

            messages.add(Component.text(attributeWrapper.getCleanName() + ": ").color(attributeNameColor).append(Component.text(String.format("%.2f", attributeValue)).color(numberColor)).append(deltaPercentComponent));

            // Append Defense/EHP if def stat
            if (attributeWrapper.equals(AttributeWrapper.HEALTH)) {
                messages.add(Component.text("- Defense: ").color(NamedTextColor.YELLOW)
                        .append(Component.text(String.format("%d ", def)).color(NamedTextColor.GREEN)));
                messages.add(Component.text("- Effective Health: ").color(NamedTextColor.YELLOW)
                        .append(Component.text(String.format("%d ", (int)ehp)).color(NamedTextColor.GREEN))
                        .append(Component.text(String.format("EHP=%dHP/%.2fDEF%%", (int)hp, DamageOverrideListener.calculateDefenseDamageMultiplier(def)*100)).color(NamedTextColor.DARK_GRAY))
                );
            }

        }

        return ChatUtil.cleanItalics(messages);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {

        if (!(event.getRightClicked() instanceof LivingEntity))
            return;

        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());

        if (item.getType().equals(Material.AIR))
            return;

        if (!isItemOfType(item))
            return;

        for (Component component : getStats((LivingEntity) event.getRightClicked()))
            event.getPlayer().sendMessage(component);

        if (event.getPlayer().isSneaking()) {
            ItemStack report = new ItemStack(Material.PAPER);
            report.editMeta(meta -> {
                meta.displayName(Component.text("Entity Analyzer Report").color(NamedTextColor.RED));
                meta.lore(getStats((LivingEntity) event.getRightClicked()));
                meta.setEnchantmentGlintOverride(true);
            });
            itemService.setIgnoreMetaUpdate(report);
            event.getRightClicked().getWorld().dropItemNaturally(((LivingEntity) event.getRightClicked()).getEyeLocation(), report);
            event.getRightClicked().getWorld().playSound(((LivingEntity) event.getRightClicked()).getEyeLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
        }
    }

}
