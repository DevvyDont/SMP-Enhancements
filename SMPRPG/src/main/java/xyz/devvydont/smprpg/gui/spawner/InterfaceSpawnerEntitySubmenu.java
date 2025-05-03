package xyz.devvydont.smprpg.gui.spawner;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.spawning.EntitySpawner;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.List;

public class InterfaceSpawnerEntitySubmenu extends MenuBase {

    private final EntitySpawner spawner;

    public InterfaceSpawnerEntitySubmenu(Player owner, InterfaceSpawnerMainMenu mainMenu) {
        super(owner, 6, mainMenu);
        this.spawner = mainMenu.getSpawner();
        render();
    }
    
    public void render() {
        this.setBorderFull();
        this.setBackButton(49);
        int limit = 44;
        int position = 0;
        for (CustomEntityType type : CustomEntityType.values()) {
            if (!type.canBeSpawnerSpawned())
                continue;

            if (position > limit) {
                this.sendMessageToPlayer(ComponentUtils.error("Could not display all entities. Yell at dev to stop being lazy and paginate this GUI!"));
                break;
            }

            ItemStack display = InterfaceUtil.getNamedItem(type.getInterfaceButton(), ComponentUtils.create("Set Weight: ", NamedTextColor.GOLD).append(ComponentUtils.create(type.getName(), NamedTextColor.RED)));
            display.editMeta(meta -> {
                meta.lore(List.of(
                        ComponentUtils.EMPTY,
                        ComponentUtils.create("Current Weight:").append(ComponentUtils.create(" " + spawner.getOptions().getWeight(type), NamedTextColor.GREEN)),
                        ComponentUtils.EMPTY,
                        ComponentUtils.create("Default Statistics:", NamedTextColor.GOLD),
                        ComponentUtils.powerLevel(type.getLevel()).append(ComponentUtils.create(" " + type.getName(), NamedTextColor.RED)),
                        ComponentUtils.create("Base Health: ").append(ComponentUtils.create(MinecraftStringUtils.formatNumber(type.getHp()), NamedTextColor.GREEN)).append(ComponentUtils.create(Symbols.HEART, NamedTextColor.RED)),
                        ComponentUtils.create("Base Damage: ").append(ComponentUtils.create(MinecraftStringUtils.formatNumber(type.getDamage()), NamedTextColor.RED)).append(ComponentUtils.create(Symbols.SKULL, NamedTextColor.DARK_GRAY)),
                        ComponentUtils.create("Base Entity: ").append(ComponentUtils.create(type.getType().name(), NamedTextColor.GOLD)),
                        ComponentUtils.EMPTY,
                        ComponentUtils.create("Left click to increase, Right click to decrease"),
                        ComponentUtils.EMPTY,
                        ComponentUtils.create("Setting a weight of 0 will remove this entity from this spawner"),
                        ComponentUtils.create("Higher weights means this mob is more likely to spawn"),
                        ComponentUtils.create("If only one entity is enabled in a spawner, weight only needs to be 1")
                ));
                meta.lore(ComponentUtils.cleanItalics(meta.lore()));
                meta.setEnchantmentGlintOverride(spawner.getOptions().getWeight(type) > 0);
            });

            setButton(position, display, (e) -> {

                var clickType = e.getClick();
                int delta = clickType.isShiftClick() ? 5 : 1;
                if (clickType.isRightClick())
                    delta *= -1;

                int level = spawner.getOptions().getWeight(type);
                int newLevel = level + delta;
                newLevel = Math.min(Math.max(0, newLevel), 100);
                if (newLevel == 0)
                    spawner.getOptions().removeEntity(type);
                else
                    spawner.getOptions().setWeight(type, newLevel);
                spawner.saveOptions();
                render();
                this.playSound(Sound.UI_BUTTON_CLICK);
            });

            position++;
        }
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(ComponentUtils.create("Spawner Editor - Entities", NamedTextColor.RED));
    }
}
