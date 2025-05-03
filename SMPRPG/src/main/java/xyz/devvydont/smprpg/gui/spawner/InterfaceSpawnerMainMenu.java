package xyz.devvydont.smprpg.gui.spawner;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class InterfaceSpawnerMainMenu extends MenuBase {

    private final EntitySpawner spawner;

    public InterfaceSpawnerMainMenu(Player owner, EntitySpawner spawner) {
        super(owner, 6);
        this.spawner = spawner;
        render();
    }

    public void render() {
        this.setBorderFull();
        this.setBackButton(49);

        // Create the button to delete this spawner.
        this.setButton(43, createDeleteButtonItem(), (event -> {
            this.getSpawner().getEntity().remove();
            this.playSound(Sound.BLOCK_ANVIL_BREAK, 1, 1.5f);
            player.closeInventory();
            player.sendMessage(ComponentUtils.success("Successfully deleted spawner!"));
        }));

        // Create the button to open the submenu to add/remove entities to this spawner.
        this.setButton(31, createEntriesButtonItem(), (event -> {
            this.playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE);
            this.openSubMenu(new InterfaceSpawnerEntitySubmenu(this.player, this));
        }));

        // Create the button to tweak the spawn limits of this spawner.
        this.setButton(15, createLimitButtonItem(), (event -> {

            int delta = event.getClick().isShiftClick() ? 5 : 1;
            if (event.getClick().isRightClick())
                delta *= -1;

            long level = this.getSpawner().getOptions().getLimit();
            long newLevel = level + delta;
            newLevel = Math.min(Math.max(1, newLevel), 75);
            this.getSpawner().getOptions().setLimit(newLevel);
            this.getSpawner().saveOptions();
            this.playSound(Sound.UI_BUTTON_CLICK);
            this.render();
        }));

        this.setButton(11, createLevelButtonItem(), (event -> {

            int delta = event.getClick().isShiftClick() ? 5 : 1;
            if (event.getClick().isRightClick())
                delta *= -1;

            long level = this.getSpawner().getOptions().getLevel();
            long newLevel = level + delta;
            newLevel = Math.min(Math.max(1, newLevel), 99);
            this.getSpawner().getOptions().setLevel(newLevel);
            this.getSpawner().saveOptions();
            this.playSound(Sound.UI_BUTTON_CLICK);
            this.render();
        }));

        this.setButton(13, createRangeButtonItem(), (event -> {

            int delta = event.getClick().isShiftClick() ? 5 : 1;
            if (event.getClick().isRightClick())
                delta *= -1;

            long level = this.getSpawner().getOptions().getRadius();
            long newLevel = level + delta;
            newLevel = Math.min(Math.max(1, newLevel), 30);
            this.getSpawner().getOptions().setRadius(newLevel);
            this.getSpawner().saveOptions();
            this.playSound(Sound.UI_BUTTON_CLICK);
            this.render();
        }));
    }

    public EntitySpawner getSpawner() {
        return spawner;
    }

    private ItemStack createDeleteButtonItem() {
        var delete = InterfaceUtil.getNamedItem(Material.BEDROCK, ComponentUtils.create("Delete Spawner", NamedTextColor.RED));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Click to remove this spawner from the world!", NamedTextColor.RED));
        delete.editMeta(meta -> {
            meta.lore(lore);
            meta.lore(ComponentUtils.cleanItalics(meta.lore()));
        });;
        return delete;
    }

    private ItemStack createRangeButtonItem() {

        ItemStack display = InterfaceUtil.getNamedItem(Material.SPYGLASS, ComponentUtils.create("Set Spawn Radius", NamedTextColor.GOLD));
        display.editMeta(meta -> {
            meta.lore(List.of(
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Current Radius:").append(ComponentUtils.create(" " + this.getSpawner().getOptions().getRadius() + " blocks", NamedTextColor.GREEN)),
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Left click to increase, Right click to decrease"),
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Range is how far away (in blocks) a mob can spawn from the location"),
                    ComponentUtils.create("of this spawner. Y position is ignored as a Y value is calculated dynamically")
            ));
            meta.lore(ComponentUtils.cleanItalics(meta.lore()));
        });
        return display;
    }

    private ItemStack createLevelButtonItem() {

        ItemStack display = InterfaceUtil.getNamedItem(Material.EXPERIENCE_BOTTLE, ComponentUtils.create("Set Level", NamedTextColor.GOLD));
        display.editMeta(meta -> {
            meta.lore(List.of(
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Current Level:").append(ComponentUtils.create(" " + this.getSpawner().getOptions().getLevel(), NamedTextColor.GREEN)),
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Left click to increase, Right click to decrease"),
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("The level to attempt to spawn mobs at. Mobs spawned at a different"),
                    ComponentUtils.create("level than their base level may have unexpected statistics however")
            ));
            meta.lore(ComponentUtils.cleanItalics(meta.lore()));
        });
        return display;
    }

    private ItemStack createEntriesButtonItem() {

        ItemStack display = InterfaceUtil.getNamedItem(Material.SKELETON_SKULL, ComponentUtils.create("Edit Entity Choices", NamedTextColor.GOLD));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Currently spawning " + this.getSpawner().getOptions().getEntries().size() + " entities"));
        for (EntitySpawner.SpawnerEntry entry : this.getSpawner().getOptions().getEntries())
            lore.add(ComponentUtils.create("- Entity: ").append(ComponentUtils.create(entry.type().Name, NamedTextColor.RED)).append(ComponentUtils.create(" Weight: ").append(ComponentUtils.create("" + entry.weight(), NamedTextColor.GREEN))));
        lore.add(ComponentUtils.EMPTY);
        lore.add(ComponentUtils.create("Click to edit!", NamedTextColor.YELLOW));
        display.editMeta(meta -> {
            meta.lore(lore);
            meta.lore(ComponentUtils.cleanItalics(meta.lore()));
        });
        return display;
    }

    private ItemStack createLimitButtonItem() {

        ItemStack display = InterfaceUtil.getNamedItem(Material.LEVER, ComponentUtils.create("Set Spawn Limit", NamedTextColor.GOLD));
        display.editMeta(meta -> {
            meta.lore(List.of(
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Current Limit:").append(ComponentUtils.create(" " + this.getSpawner().getOptions().getLimit() + " entities", NamedTextColor.GREEN)),
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Left click to increase, Right click to decrease"),
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Spawner limit is how many entities at a time this specific"),
                    ComponentUtils.create("spawner can add entities to the world. Spawning cycles are"),
                    ComponentUtils.create("skipped when " + this.getSpawner().getOptions().getLimit() + " entities spawned from this spawner are alive")
            ));
            meta.lore(ComponentUtils.cleanItalics(meta.lore()));
        });
        return display;
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.create("Spawner Editor", NamedTextColor.RED));
    }
}
