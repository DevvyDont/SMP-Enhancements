package xyz.devvydont.smprpg.gui.spawner;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.PrivateInterface;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceSpawnerMainMenu extends PrivateInterface {

    private EntitySpawner spawner;

    private final SpawnerButton deleteButton;
    private final SpawnerButton entriesButton;
    private final SpawnerButton levelButton;
    private final SpawnerButton rangeButton;
    private final SpawnerButton limitButton;

    private final Map<Integer, SpawnerButton> buttonHandlers;

    public InterfaceSpawnerMainMenu(SMPRPG plugin, Player owner, EntitySpawner spawner) {
        super(plugin, owner);
        this.spawner = spawner;

        deleteButton = new SpawnerButton() {
            @Override
            public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                ItemStack display = InterfaceUtil.getNamedItem(Material.BEDROCK, ComponentUtils.create("Delete Spawner", NamedTextColor.RED));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.empty());
                lore.add(ComponentUtils.create("Click to remove this spawner from the world!", NamedTextColor.RED));
                display.editMeta(meta -> {
                    meta.lore(lore);
                    meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                });
                return display;
            }

            @Override
            public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {
                gui.getSpawner().getEntity().remove();
                player.closeInventory();
                player.sendMessage(ComponentUtils.success("Successfully deleted spawner!"));
            }
        };

        entriesButton = new SpawnerButton() {
            @Override
            public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                ItemStack display = InterfaceUtil.getNamedItem(Material.SKELETON_SKULL, ComponentUtils.create("Edit Entity Choices", NamedTextColor.GOLD));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.empty());
                lore.add(ComponentUtils.create("Currently spawning " + gui.getSpawner().getOptions().getEntries().size() + " entities"));
                for (EntitySpawner.SpawnerEntry entry : gui.getSpawner().getOptions().getEntries())
                    lore.add(ComponentUtils.create("- Entity: ").append(ComponentUtils.create(entry.type().name, NamedTextColor.RED)).append(ComponentUtils.create(" Weight: ").append(ComponentUtils.create("" + entry.weight(), NamedTextColor.GREEN))));
                lore.add(Component.empty());
                lore.add(ComponentUtils.create("Click to edit!", NamedTextColor.YELLOW));
                display.editMeta(meta -> {
                    meta.lore(lore);
                    meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                });
                return display;
            }

            @Override
            public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {
                new InterfaceSpawnerEntitySubmenu(plugin, owner, gui).open();
            }
        };

        levelButton = new SpawnerButton() {
            @Override
            public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                ItemStack display = InterfaceUtil.getNamedItem(Material.EXPERIENCE_BOTTLE, ComponentUtils.create("Set Level", NamedTextColor.GOLD));
                display.editMeta(meta -> {
                    meta.lore(List.of(
                            Component.empty(),
                            ComponentUtils.create("Current Level:").append(ComponentUtils.create(" " + gui.getSpawner().getOptions().getLevel(), NamedTextColor.GREEN)),
                            Component.empty(),
                            ComponentUtils.create("Left click to increase, Right click to decrease"),
                            Component.empty(),
                            ComponentUtils.create("The level to attempt to spawn mobs at. Mobs spawned at a different"),
                            ComponentUtils.create("level than their base level may have unexpected statistics however")
                    ));
                    meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                });
                return display;
            }

            @Override
            public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {

                int delta = clickType.isShiftClick() ? 5 : 1;
                if (clickType.isRightClick())
                    delta *= -1;

                long level = gui.getSpawner().getOptions().getLevel();
                long newLevel = level + delta;
                newLevel = Math.min(Math.max(1, newLevel), 99);
                gui.getSpawner().getOptions().setLevel(newLevel);
                gui.getSpawner().saveOptions();
            }
        };

        rangeButton = new SpawnerButton() {
            @Override
            public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                ItemStack display = InterfaceUtil.getNamedItem(Material.SPYGLASS, ComponentUtils.create("Set Spawn Radius", NamedTextColor.GOLD));
                display.editMeta(meta -> {
                    meta.lore(List.of(
                            Component.empty(),
                            ComponentUtils.create("Current Radius:").append(ComponentUtils.create(" " + gui.getSpawner().getOptions().getRadius() + " blocks", NamedTextColor.GREEN)),
                            Component.empty(),
                            ComponentUtils.create("Left click to increase, Right click to decrease"),
                            Component.empty(),
                            ComponentUtils.create("Range is how far away (in blocks) a mob can spawn from the location"),
                            ComponentUtils.create("of this spawner. Y position is ignored as a Y value is calculated dynamically")
                    ));
                    meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                });
                return display;
            }

            @Override
            public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {

                int delta = clickType.isShiftClick() ? 5 : 1;
                if (clickType.isRightClick())
                    delta *= -1;

                long level = gui.getSpawner().getOptions().getRadius();
                long newLevel = level + delta;
                newLevel = Math.min(Math.max(1, newLevel), 30);
                gui.getSpawner().getOptions().setRadius(newLevel);
                gui.getSpawner().saveOptions();
            }
        };

        limitButton = new SpawnerButton() {
            @Override
            public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                ItemStack display = InterfaceUtil.getNamedItem(Material.LEVER, ComponentUtils.create("Set Spawn Limit", NamedTextColor.GOLD));
                display.editMeta(meta -> {
                    meta.lore(List.of(
                            Component.empty(),
                            ComponentUtils.create("Current Limit:").append(ComponentUtils.create(" " + gui.getSpawner().getOptions().getLimit() + " entities", NamedTextColor.GREEN)),
                            Component.empty(),
                            ComponentUtils.create("Left click to increase, Right click to decrease"),
                            Component.empty(),
                            ComponentUtils.create("Spawner limit is how many entities at a time this specific"),
                            ComponentUtils.create("spawner can add entities to the world. Spawning cycles are"),
                            ComponentUtils.create("skipped when " + gui.getSpawner().getOptions().getLimit() + " entities spawned from this spawner are alive")
                    ));
                    meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                });
                return display;
            }

            @Override
            public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {

                int delta = clickType.isShiftClick() ? 5 : 1;
                if (clickType.isRightClick())
                    delta *= -1;

                long level = gui.getSpawner().getOptions().getLimit();
                long newLevel = level + delta;
                newLevel = Math.min(Math.max(1, newLevel), 75);
                gui.getSpawner().getOptions().setLimit(newLevel);
                gui.getSpawner().saveOptions();
            }
        };

        buttonHandlers = new HashMap<>();
        buttonHandlers.put(11, levelButton);
        buttonHandlers.put(13, rangeButton);
        buttonHandlers.put(15, limitButton);
        buttonHandlers.put(31, entriesButton);
        buttonHandlers.put(43, deleteButton);
    }

    public EntitySpawner getSpawner() {
        return spawner;
    }

    public void updateButtons() {
        for (Map.Entry<Integer, SpawnerButton> entry : buttonHandlers.entrySet())
            setSlot(entry.getKey(), entry.getValue().getItem(this));
    }

    @Override
    public void initializeDefaultState() {
        super.initializeDefaultState();
        fill(InterfaceUtil.getInterfaceBorder());
        updateButtons();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        SpawnerButton button = buttonHandlers.get(event.getSlot());
        if (button != null) {
            button.handleClick(this, (Player) event.getWhoClicked(), event.getClick());
            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_COMPARATOR_CLICK, .5f, 1.5f);
        }

        updateButtons();
    }
}
