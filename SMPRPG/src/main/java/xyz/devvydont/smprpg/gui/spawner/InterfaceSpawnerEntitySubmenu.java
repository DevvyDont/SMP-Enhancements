package xyz.devvydont.smprpg.gui.spawner;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.PrivateInterface;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceSpawnerEntitySubmenu extends PrivateInterface {

    private final Map<Integer, SpawnerButton> buttonHandlers;
    private InterfaceSpawnerMainMenu mainMenu;

    private SpawnerButton backButton;

    public InterfaceSpawnerEntitySubmenu(SMPRPG plugin, Player owner, InterfaceSpawnerMainMenu mainMenu) {
        super(plugin, owner);
        this.mainMenu = mainMenu;

        this.buttonHandlers = new HashMap<>();

        backButton = new SpawnerButton() {
            @Override
            public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                return InterfaceUtil.getNamedItem(Material.SPECTRAL_ARROW, ComponentUtils.create("Go Back to Main Menu", NamedTextColor.RED));
            }

            @Override
            public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {
                gui.open();
            }
        };

        buttonHandlers.put(49, backButton);

        int limit = 44;
        int position = 0;
        for (CustomEntityType type : CustomEntityType.values()) {
            if (!type.canBeSpawnerSpawned())
                continue;

            if (position > limit) {
                owner.sendMessage(ComponentUtils.error("Could not display all entities. Yell at dev to stop being lazy and paginate this GUI!"));
                break;
            }

            SpawnerButton button = new SpawnerButton() {
                @Override
                public ItemStack getItem(InterfaceSpawnerMainMenu gui) {
                    ItemStack display = InterfaceUtil.getNamedItem(type.getInterfaceButton(), ComponentUtils.create("Set Weight: ", NamedTextColor.GOLD).append(ComponentUtils.create(type.getName(), NamedTextColor.RED)));
                    display.editMeta(meta -> {
                        meta.lore(List.of(
                                ComponentUtils.EMPTY,
                                ComponentUtils.create("Current Weight:").append(ComponentUtils.create(" " + gui.getSpawner().getOptions().getWeight(type), NamedTextColor.GREEN)),
                                ComponentUtils.EMPTY,
                                ComponentUtils.create("Default Statistics:", NamedTextColor.GOLD),
                                ComponentUtils.powerLevel(type.getBaseLevel()).append(ComponentUtils.create(" " + type.getName(), NamedTextColor.RED)),
                                ComponentUtils.create("Base Health: ").append(ComponentUtils.create(MinecraftStringUtils.formatNumber(type.getBaseHp()), NamedTextColor.GREEN)).append(ComponentUtils.create(Symbols.HEART, NamedTextColor.RED)),
                                ComponentUtils.create("Base Damage: ").append(ComponentUtils.create(MinecraftStringUtils.formatNumber(type.getBaseDamage()), NamedTextColor.RED)).append(ComponentUtils.create(Symbols.SKULL, NamedTextColor.DARK_GRAY)),
                                ComponentUtils.create("Base Entity: ").append(ComponentUtils.create(type.getEntityType().name(), NamedTextColor.GOLD)),
                                ComponentUtils.EMPTY,
                                ComponentUtils.create("Left click to increase, Right click to decrease"),
                                ComponentUtils.EMPTY,
                                ComponentUtils.create("Setting a weight of 0 will remove this entity from this spawner"),
                                ComponentUtils.create("Higher weights means this mob is more likely to spawn"),
                                ComponentUtils.create("If only one entity is enabled in a spawner, weight only needs to be 1")
                        ));
                        meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                        meta.setEnchantmentGlintOverride(gui.getSpawner().getOptions().getWeight(type) > 0);
                    });
                    return display;
                }

                @Override
                public void handleClick(InterfaceSpawnerMainMenu gui, Player player, ClickType clickType) {

                    int delta = clickType.isShiftClick() ? 5 : 1;
                    if (clickType.isRightClick())
                        delta *= -1;

                    int level = gui.getSpawner().getOptions().getWeight(type);
                    int newLevel = level + delta;
                    newLevel = Math.min(Math.max(0, newLevel), 100);
                    if (newLevel <= 0)
                        gui.getSpawner().getOptions().removeEntity(type);
                    else
                        gui.getSpawner().getOptions().setWeight(type, newLevel);
                    gui.getSpawner().saveOptions();
                }
            };
            buttonHandlers.put(position, button);
            position++;
        }

    }

    public EntitySpawner getSpawner() {
        return mainMenu.getSpawner();
    }

    public void updateButtons() {
        for (Map.Entry<Integer, SpawnerButton> entry : buttonHandlers.entrySet())
            setSlot(entry.getKey(), entry.getValue().getItem(mainMenu));
    }

    @Override
    public void initializeDefaultState() {
        super.initializeDefaultState();
        for (int i = 45; i < 54; i++)
            setSlot(i, InterfaceUtil.getInterfaceBorder());
        updateButtons();
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        SpawnerButton button = buttonHandlers.get(event.getSlot());
        if (button != null) {
            button.handleClick(mainMenu, (Player) event.getWhoClicked(), event.getClick());
            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_COMPARATOR_CLICK, .5f, 1.5f);
        }

        updateButtons();
    }

}
