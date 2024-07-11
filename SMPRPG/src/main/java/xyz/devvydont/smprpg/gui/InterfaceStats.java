package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.listeners.DamageOverrideListener;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ChatUtil;
import xyz.devvydont.smprpg.util.formatting.Symbols;


import java.util.ArrayList;
import java.util.List;

public class InterfaceStats extends PrivateInterface {

    public static final int HELMET_SLOT = 14;
    public static final int CHESTPLATE_SLOT = 23;
    public static final int LEGGINGS_SLOT = 32;
    public static final int BOOTS_SLOT = 41;

    public static final int MAIN_HAND_SLOT = 24;
    public static final int OFF_HAND_SLOT = 22;

    public static final int STATS_SLOT = 20;
    public static final int MISC_INFO = 29;

    private final Player viewing;

    public InterfaceStats(SMPRPG plugin, Player owner, Player viewing) {
        super(plugin, owner);
        this.viewing = viewing;
    }

    public Player getViewing() {
        return viewing;
    }

    public ItemStack getStats() {

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.displayName(Component.text(viewing.getName()).color(NamedTextColor.AQUA).append(Component.text("'s stats").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        // Add the power rating
        lore.add(Component.text("Power Rating: ").color(NamedTextColor.GOLD).append(Component.text(Symbols.POWER + plugin.getEntityService().getPlayerInstance(viewing).getLevel()).color(NamedTextColor.YELLOW)));
        lore.add(Component.empty());

        double hp = viewing.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        int def = plugin.getEntityService().getPlayerInstance(viewing).getDefense();
        double ehp = DamageOverrideListener.calculateEffectiveHealth(hp, def);

        for (AttributeWrapper attributeWrapper : AttributeWrapper.values()) {

            // We can skip attributes we don't have
            if (viewing.getAttribute(attributeWrapper.getAttribute()) == null)
                continue;

            NamedTextColor numberColor = NamedTextColor.DARK_GRAY;
            double attributeValue = viewing.getAttribute(attributeWrapper.getAttribute()).getValue();
            double baseAttributeValue = viewing.getAttribute(attributeWrapper.getAttribute()).getBaseValue();
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

            lore.add(Component.text(attributeWrapper.getCleanName() + ": ").color(attributeNameColor).append(Component.text(String.format("%.2f", attributeValue)).color(numberColor)).append(deltaPercentComponent));

            // Append Defense/EHP if def stat
            if (attributeWrapper.equals(AttributeWrapper.HEALTH)) {
                lore.add(Component.text("- Defense: ").color(NamedTextColor.YELLOW)
                        .append(Component.text(String.format("%d ", def)).color(NamedTextColor.GREEN)));
                lore.add(Component.text("- Effective Health: ").color(NamedTextColor.YELLOW)
                        .append(Component.text(String.format("%d ", (int)ehp)).color(NamedTextColor.GREEN))
                        .append(Component.text(String.format("EHP=%dHP/%.2fDEF%%", (int)hp, DamageOverrideListener.calculateDefenseDamageMultiplier(def)*100)).color(NamedTextColor.DARK_GRAY))
                );
            }

        }

        meta.lore(ChatUtil.cleanItalics(lore));
        meta.setPlayerProfile(getViewing().getPlayerProfile());

        skull.setItemMeta(meta);
        return skull;
    }

    public ItemStack getHelp() {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(Component.text("Statistic Guide").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        lore.add(Component.text("Power Rating ").color(NamedTextColor.GOLD)
                .append(Component.text("- An estimation of ").color(NamedTextColor.GRAY))
                .append(Component.text("general combat effectiveness").color(NamedTextColor.YELLOW))
                .append(Component.text(" using currently equipped gear and stats.").color(NamedTextColor.GRAY))
        );
        lore.add(Component.empty());

        for (AttributeWrapper attributeWrapper : AttributeWrapper.values())
            lore.add(Component.text(attributeWrapper.getCleanName()).color(NamedTextColor.GOLD).append(Component.text(" - ").color(NamedTextColor.GRAY).append(attributeWrapper.getDescription())));

        meta.lore(ChatUtil.cleanItalics(lore));

        paper.setItemMeta(meta);
        return paper;
    }

    public ItemStack getMisc() {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(Component.text(viewing.getName()).color(NamedTextColor.AQUA).append(Component.text("'s info").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("todo"));

        meta.lore(ChatUtil.cleanItalics(lore));
        paper.setItemMeta(meta);
        return paper;
    }


    @Nullable
    public ItemStack getEquipment(int slot) {
        return switch (slot) {
            case HELMET_SLOT -> viewing.getInventory().getHelmet();
            case CHESTPLATE_SLOT -> viewing.getInventory().getChestplate();
            case LEGGINGS_SLOT -> viewing.getInventory().getLeggings();
            case BOOTS_SLOT -> viewing.getInventory().getBoots();
            case MAIN_HAND_SLOT -> viewing.getInventory().getItemInMainHand();
            case OFF_HAND_SLOT -> viewing.getInventory().getItemInOffHand();
            case STATS_SLOT -> getStats();
            case MISC_INFO -> getMisc();
            default -> null;
        };
    }

    public String getMissingString(int slot) {
        String name = viewing.getName();
        return switch (slot) {
            case HELMET_SLOT -> String.format("%s is not wearing a helmet", name);
            case CHESTPLATE_SLOT -> String.format("%s is not wearing a chestplate", name);
            case LEGGINGS_SLOT -> String.format("%s is not wearing leggings", name);
            case BOOTS_SLOT -> String.format("%s is not wearing boots", name);
            case MAIN_HAND_SLOT -> String.format("%s is not holding anything", name);
            case OFF_HAND_SLOT -> String.format("%s is not holding anything in their off hand", name);
            case STATS_SLOT -> String.format("%s does not have stats", name);
            case MISC_INFO -> String.format("%s does not have information", name);
            default -> "";
        };
    }

    public boolean shouldBePopulated(int slot) {
        return !getMissingString(slot).isEmpty();
    }

    @Override
    public void initializeDefaultState() {
        super.initializeDefaultState();

        inventoryView.setTitle(String.format("%sStatistics Viewer %s(%s)", ChatColor.BLUE, ChatColor.AQUA, viewing.getName()));

        fill(InterfaceUtil.getInterfaceBorder());

        for (int slot = 0; slot < inventory.getSize(); slot++) {

            if (!shouldBePopulated(slot))
                continue;

            ItemStack display = getEquipment(slot);
            if (display != null && !display.getType().equals(Material.AIR)) {
                inventory.setItem(slot, display);
                continue;
            }

            ItemStack empty = InterfaceUtil.getNamedItem(Material.CLAY_BALL, Component.text(getMissingString(slot)).color(NamedTextColor.RED));
            inventory.setItem(slot, empty);
        }

        inventory.setItem(STATS_SLOT-1, getHelp());
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        super.handleInventoryClick(event);
        event.setCancelled(true);
    }
}
