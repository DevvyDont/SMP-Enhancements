package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
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
    public static final int INVENTORY_SLOT = 11;
    public static final int MISC_INFO = 29;

    private final LivingEntity viewing;

    public InterfaceStats(SMPRPG plugin, Player owner, LivingEntity viewing) {
        super(plugin, owner);
        this.viewing = viewing;
    }

    public LivingEntity getViewing() {
        return viewing;
    }

    public boolean isPlayer() {
        return viewing instanceof Player;
    }

    public Player getPlayer() {
        return (Player) viewing;
    }

    public ItemStack getHead() {
        ItemStack item;
        if (isPlayer()) {
            item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setPlayerProfile(getPlayer().getPlayerProfile());
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.SKELETON_SKULL);
        }

        return item;
    }

    public ItemStack getStats() {

        ItemStack skull = getHead();
        ItemMeta meta = skull.getItemMeta();
        LeveledEntity entity = plugin.getEntityService().getEntityInstance(viewing);

        meta.displayName(viewing.name().color(NamedTextColor.AQUA).append(Component.text("'s stats").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        // Add the power rating
        lore.add(Component.text("Power Rating: ").color(NamedTextColor.GOLD).append(Component.text(Symbols.POWER + entity.getLevel()).color(NamedTextColor.YELLOW)));
        lore.add(Component.empty());

        double hp = viewing.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        int def = entity.getDefense();
        double ehp = EntityDamageCalculatorService.calculateEffectiveHealth(hp, def);

        for (AttributeWrapper attributeWrapper : AttributeWrapper.values()) {

            // We can skip attributes we don't have
            AttributeInstance attribute = viewing.getAttribute(attributeWrapper.getAttribute());
            if (attribute == null)
                continue;

            NamedTextColor numberColor = NamedTextColor.DARK_GRAY;
            double attributeValue = AttributeUtil.getAttributeValue(attributeWrapper.getAttribute(), viewing);
            double baseAttributeValue = attribute.getBaseValue();
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
            if (attributeWrapper.equals(AttributeWrapper.DEFENSE)) {
                lore.add(Component.text("- Effective Health: ").color(NamedTextColor.YELLOW)
                        .append(Component.text(String.format("%d ", (int)ehp)).color(NamedTextColor.GREEN))
                        .append(Component.text(String.format("EHP=%dHP/%.2fDEF%%", (int)hp, EntityDamageCalculatorService.calculateResistancePercentage(def)*100)).color(NamedTextColor.DARK_GRAY))
                );
            }

        }

        meta.lore(ComponentUtils.cleanItalics(lore));
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

        meta.lore(ComponentUtils.cleanItalics(lore));

        paper.setItemMeta(meta);
        return paper;
    }

    private List<Component> getSkillDisplay(LeveledPlayer player) {

        List<Component> display = new ArrayList<>();
        display.add(Component.empty());
        for (SkillInstance skill : player.getSkills())
            display.add(
                    ComponentUtils.getColoredComponent(skill.getType().getDisplayName() + " " + skill.getLevel(), NamedTextColor.AQUA)
                            .append(ComponentUtils.getDefaultText(" - "))
                            .append(ComponentUtils.getColoredComponent(MinecraftStringUtils.formatNumber(skill.getExperienceProgress()), NamedTextColor.GREEN))
                            .append(ComponentUtils.getDefaultText("/"))
                            .append(ComponentUtils.getColoredComponent(MinecraftStringUtils.formatNumber(skill.getNextExperienceThreshold()), NamedTextColor.GOLD))
                            .append(ComponentUtils.getDefaultText(" ("))
                            .append(ComponentUtils.getColoredComponent(MinecraftStringUtils.formatNumber(skill.getExperience()) + "XP", NamedTextColor.DARK_GRAY))
                            .append(ComponentUtils.getDefaultText(")"))
            );
        display.add(Component.empty());
        display.add(ComponentUtils.getDefaultText("Skill Average: ").append(ComponentUtils.getColoredComponent(String.format("%.2f", player.getAverageSkillLevel()), NamedTextColor.GOLD)));
        return display;
    }

    public ItemStack getSkills() {
        ItemStack paper = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(viewing.name().color(NamedTextColor.AQUA).append(Component.text("'s Skills").color(NamedTextColor.GRAY)).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());

        if (getViewing() instanceof Player player)
            lore.addAll(getSkillDisplay(plugin.getEntityService().getPlayerInstance(player)));
        else
            lore.add(ComponentUtils.getColoredComponent("Only players have skills!", NamedTextColor.RED));
        meta.lore(ComponentUtils.cleanItalics(lore));
        paper.setItemMeta(meta);
        return paper;
    }

    public ItemStack getInventory() {
        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta meta = chest.getItemMeta();

        meta.displayName(viewing.name().color(NamedTextColor.AQUA).append(Component.text("'s Full Inventory").color(NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = List.of(
                Component.empty(),
                Component.text("Click to view their entire inventory and Ender Chest!", NamedTextColor.GRAY)
        );
        meta.lore(ComponentUtils.cleanItalics(lore));
        chest.setItemMeta(meta);
        return chest;
    }


    @Nullable
    public ItemStack getEquipment(int slot) {

        if (viewing.getEquipment() == null)
            return null;

        return switch (slot) {
            case HELMET_SLOT -> viewing.getEquipment().getHelmet();
            case CHESTPLATE_SLOT -> viewing.getEquipment().getChestplate();
            case LEGGINGS_SLOT -> viewing.getEquipment().getLeggings();
            case BOOTS_SLOT -> viewing.getEquipment().getBoots();
            case MAIN_HAND_SLOT -> viewing.getEquipment().getItemInMainHand();
            case OFF_HAND_SLOT -> viewing.getEquipment().getItemInOffHand();
            case STATS_SLOT -> getStats();
            case INVENTORY_SLOT -> getInventory();
            case MISC_INFO -> getSkills();
            default -> null;
        };
    }

    public Component getMissingComponent(int slot) {
        Component name = viewing.name();
        return switch (slot) {
            case HELMET_SLOT -> name.append(Component.text(" is not wearing a helmet").color(NamedTextColor.RED));
            case CHESTPLATE_SLOT -> name.append(Component.text(" is not wearing a chestplate").color(NamedTextColor.RED));
            case LEGGINGS_SLOT -> name.append(Component.text(" is not wearing leggings").color(NamedTextColor.RED));
            case BOOTS_SLOT -> name.append(Component.text(" is not wearing boots").color(NamedTextColor.RED));
            case MAIN_HAND_SLOT -> name.append(Component.text(" is not holding anything").color(NamedTextColor.RED));
            case OFF_HAND_SLOT -> name.append(Component.text(" is not holding anything in their off hand").color(NamedTextColor.RED));
            case INVENTORY_SLOT -> name.append(Component.text(" does not have an inventory").color(NamedTextColor.RED));
            case STATS_SLOT -> name.append(Component.text(" does not have stats").color(NamedTextColor.RED));
            case MISC_INFO -> name.append(Component.text(" does not have information").color(NamedTextColor.RED));
            default -> Component.empty();
        };
    }

    public boolean shouldBePopulated(int slot) {
        return !getMissingComponent(slot).equals(Component.empty());
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

            ItemStack empty = InterfaceUtil.getNamedItem(Material.CLAY_BALL, getMissingComponent(slot));
            inventory.setItem(slot, empty);
        }

        inventory.setItem(STATS_SLOT-1, getHelp());
    }

    @Override
    public void handleInventoryClick(InventoryClickEvent event) {
        super.handleInventoryClick(event);
        event.setCancelled(true);

        if (inventory.equals(event.getClickedInventory()) && event.getSlot() == INVENTORY_SLOT && viewing instanceof Player player) {
            owner.playSound(owner.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
            owner.closeInventory();
            var gui = new InterfaceInventoryPeek(plugin, owner);
            gui.open();
            gui.showPlayer(player);
        }
    }
}
