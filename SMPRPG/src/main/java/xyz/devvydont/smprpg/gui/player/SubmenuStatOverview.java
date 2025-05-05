package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapperLegacy;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;

public class SubmenuStatOverview extends MenuBase {

    public SubmenuStatOverview(@NotNull Player player, MenuBase parentMenu) {
        super(player, 6, parentMenu);
        render();
    }

    private int findNextEmpty(int start) {
        var current = start + 1;

        if (current >= this.getInventorySize())
            return -1;

        if (this.getItem(current) == null)
            return current;
        return findNextEmpty(current);
    }

    public void render() {
        this.setBorderEdge();
        this.setBackButton(49);

        var index = 0;
        for (var attribute : AttributeWrapperLegacy.values()) {
            var attributeInstance = this.player.getAttribute(attribute.getAttribute());
            if (attributeInstance == null)
                continue;

            index = findNextEmpty(index);
            if (index == -1)
                return;

            this.setButton(index, generateItemDisplay(attribute), e -> handleClick(attribute));
        }
    }

    private ItemStack generateItemDisplay(AttributeWrapperLegacy attribute) {

        var item = ItemStack.of(Material.DIAMOND_PICKAXE);
        var attributeInstance = this.player.getAttribute(attribute.getAttribute());

        item.editMeta(meta -> {
            meta.displayName(ComponentUtils.create(attribute.getCleanName()));

            if (attributeInstance == null)
                return;

            var lore = new ArrayList<Component>();
            lore.add(ComponentUtils.create("Base: " + attributeInstance.getBaseValue()));
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Active Modifiers:"));
            for (var modifier : attributeInstance.getModifiers())
                lore.add(ComponentUtils.create(modifier.getName() + " " + modifier.getOperation() + " " + modifier.getAmount() + " (" + modifier.getSlotGroup() + ")"));
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Final: " + attributeInstance.getValue()));
            meta.lore(lore);
        });

        return item;
    }

    private void handleClick(AttributeWrapperLegacy attribute) {

    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.merge(
                ComponentUtils.create("Stat Overview"),
                ComponentUtils.create(" WORK IN PROGRESS", NamedTextColor.RED, TextDecoration.BOLD)
        ));
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        this.playInvalidAnimation();
        event.setCancelled(true);
    }
}
