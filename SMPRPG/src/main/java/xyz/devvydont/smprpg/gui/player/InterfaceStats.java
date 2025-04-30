package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;

public class InterfaceStats extends MenuBase {
    private static final int SLOT_HELMET = 14;
    private static final int SLOT_CHESTPLATE = 23;
    private static final int SLOT_LEGGINGS = 32;
    private static final int SLOT_BOOTS = 41;
    private static final int SLOT_MAIN_HAND = 24;
    private static final int SLOT_OFF_HAND = 22;
    private static final int SLOT_STATS = 20;
    private static final int SLOT_INVENTORY = 11;
    private static final int SLOT_MISC_INFO = 29;

    private final LivingEntity targetEntity;
    private final EntityService entityService;

    public InterfaceStats(SMPRPG plugin, Player owner, LivingEntity targetPlayer) {
        super(owner, 6);
        this.targetEntity = targetPlayer;
        this.entityService = plugin.getEntityService();
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        // Prepare the inventory
        var title = String.format("Statistics Viewer (%s)", targetEntity.getName());
        event.titleOverride(ComponentUtils.create(title, NamedTextColor.BLACK));

        // Render the UI
        this.clear();
        this.setBorderFull();

        // Work through each slot and render based on index.
        for (var slot = 0; slot < this.getInventorySize(); slot++) {
            // Ignore the slot if there's nothing to render there.
            if (!this.shouldBePopulated(slot)) {
                continue;
            }

            // Render equipment if it's present.
            var display = this.getEquipment(slot);
            if (display != null && !display.getType().equals(Material.AIR)) {
                this.setSlot(slot, display);
                continue;
            }

            var emptyItem = createNamedItem(Material.CLAY_BALL, this.getMissingComponent(slot));
            this.setSlot(slot, emptyItem);
        }

        if (isPlayer()) {
            this.setButton(SLOT_INVENTORY, this.getInventory(), (e) -> {
                this.playSound(Sound.BLOCK_CHEST_OPEN);
                new MenuInventoryPeek(this.player, this.getPlayer(), this).openMenu();
            });

            this.setButton(SLOT_STATS, getStats(), e -> this.openSubMenu(new SubmenuStatOverview(this.player, this)));
        }

        this.setSlot(SLOT_STATS - 1, getHelp());
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        this.playInvalidAnimation();
    }

    private boolean isPlayer() {
        return this.targetEntity instanceof Player;
    }

    private Player getPlayer() {
        return (Player) this.targetEntity;
    }

    private ItemStack getHead() {
        ItemStack item;
        if (this.isPlayer()) {
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
        ItemStack skull = this.getHead();
        ItemMeta meta = skull.getItemMeta();
        LeveledEntity entity = this.entityService.getEntityInstance(this.targetEntity);

        meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(ComponentUtils.create("'s stats")).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);

        // Add the power rating
        lore.add(ComponentUtils.create("Power Rating: ", NamedTextColor.GOLD).append(ComponentUtils.create(Symbols.POWER + entity.getLevel(), NamedTextColor.YELLOW)));
        lore.add(ComponentUtils.EMPTY);

        var hp = this.targetEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
        var def = entity.getDefense();
        var ehp = EntityDamageCalculatorService.calculateEffectiveHealth(hp, def);

        for (AttributeWrapper attributeWrapper : AttributeWrapper.values()) {

            // We can skip attributes we don't have
            AttributeInstance attribute = this.targetEntity.getAttribute(attributeWrapper.getAttribute());
            if (attribute == null)
                continue;

            NamedTextColor numberColor = NamedTextColor.DARK_GRAY;
            double attributeValue = AttributeUtil.getAttributeValue(attributeWrapper.getAttribute(), this.targetEntity);
            double baseAttributeValue = attribute.getBaseValue();
            if (attributeValue > baseAttributeValue)
                numberColor = NamedTextColor.GREEN;
            if (attributeValue < baseAttributeValue)
                numberColor = NamedTextColor.RED;

            NamedTextColor attributeNameColor = NamedTextColor.GOLD;
            if (attributeWrapper.getAttributeType().equals(AttributeWrapper.AttributeType.SPECIAL))
                attributeNameColor = NamedTextColor.LIGHT_PURPLE;

            double deltaDiff = (baseAttributeValue - attributeValue) / baseAttributeValue * 100 * -1;
            Component deltaPercentComponent = ComponentUtils.create(String.format(" (%s%.2f%%)",deltaDiff > 0 ? "+" : "", deltaDiff), deltaDiff > 0 ? NamedTextColor.AQUA : NamedTextColor.DARK_RED);
            if (deltaDiff == 0 || Double.isNaN(deltaDiff) || Double.isInfinite(deltaDiff))
                deltaPercentComponent = ComponentUtils.EMPTY;

            lore.add(ComponentUtils.create(attributeWrapper.getCleanName() + ": ", attributeNameColor).append(ComponentUtils.create(String.format("%.2f", attributeValue), numberColor)).append(deltaPercentComponent));

            // Append Defense/EHP if def stat
            if (attributeWrapper.equals(AttributeWrapper.DEFENSE)) {
                lore.add(ComponentUtils.merge(
                    ComponentUtils.create("- Effective Health: ", NamedTextColor.YELLOW),
                    ComponentUtils.create(String.format("%d ", (int)ehp), NamedTextColor.GREEN),
                    ComponentUtils.create(String.format("EHP=%dHP/%.2fDEF%%", (int)hp, EntityDamageCalculatorService.calculateResistancePercentage(def)*100), NamedTextColor.DARK_GRAY)
                ));
            }

        }

        meta.lore(ComponentUtils.cleanItalics(lore));
        skull.setItemMeta(meta);
        return skull;
    }

    private ItemStack getHelp() {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(ComponentUtils.create("Statistic Guide", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);

        lore.add(ComponentUtils.merge(
            ComponentUtils.create("Power Rating ", NamedTextColor.GOLD),
            ComponentUtils.create("- An estimation of "),
            ComponentUtils.create("general combat effectiveness", NamedTextColor.YELLOW),
            ComponentUtils.create(" using currently equipped gear and stats.")
        ));
        lore.add(ComponentUtils.EMPTY);

        for (AttributeWrapper attributeWrapper : AttributeWrapper.values())
            lore.add(ComponentUtils.create(attributeWrapper.getCleanName(), NamedTextColor.GOLD).append(ComponentUtils.create(" - ").append(attributeWrapper.getDescription())));

        meta.lore(ComponentUtils.cleanItalics(lore));

        paper.setItemMeta(meta);
        return paper;
    }

    private List<Component> getSkillDisplay(LeveledPlayer player) {
        List<Component> display = new ArrayList<>();
        display.add(ComponentUtils.EMPTY);
        for (SkillInstance skill : player.getSkills())
            display.add(ComponentUtils.merge(
                    ComponentUtils.create(skill.getType().getDisplayName() + " " + skill.getLevel(), NamedTextColor.AQUA),
                    ComponentUtils.create(" - "),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getExperienceProgress()), NamedTextColor.GREEN),
                    ComponentUtils.create("/"),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getNextExperienceThreshold()), NamedTextColor.GOLD),
                    ComponentUtils.create(" ("),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getExperience()) + "XP", NamedTextColor.DARK_GRAY),
                    ComponentUtils.create(")")
            ));
        display.add(ComponentUtils.EMPTY);
        display.add(ComponentUtils.create("Skill Average: ").append(ComponentUtils.create(String.format("%.2f", player.getAverageSkillLevel()), NamedTextColor.GOLD)));
        return display;
    }

    private ItemStack getSkills() {
        ItemStack paper = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(ComponentUtils.create("'s Skills")).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);

        if (this.isPlayer())
            lore.addAll(getSkillDisplay(this.entityService.getPlayerInstance(this.getPlayer())));
        else
            lore.add(ComponentUtils.create("Only players have skills!", NamedTextColor.RED));
        meta.lore(ComponentUtils.cleanItalics(lore));
        paper.setItemMeta(meta);
        return paper;
    }

    private ItemStack getInventory() {
        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta meta = chest.getItemMeta();

        meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(ComponentUtils.create("'s Full Inventory", NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = List.of(
                ComponentUtils.EMPTY,
                ComponentUtils.create("Click to view their entire inventory and Ender Chest!")
        );
        meta.lore(ComponentUtils.cleanItalics(lore));
        chest.setItemMeta(meta);
        return chest;
    }

    @Nullable
    private ItemStack getEquipment(int slot) {
        if (this.targetEntity.getEquipment() == null)
            return null;

        return switch (slot) {
            case SLOT_HELMET -> this.targetEntity.getEquipment().getHelmet();
            case SLOT_CHESTPLATE -> this.targetEntity.getEquipment().getChestplate();
            case SLOT_LEGGINGS -> this.targetEntity.getEquipment().getLeggings();
            case SLOT_BOOTS -> this.targetEntity.getEquipment().getBoots();
            case SLOT_MAIN_HAND -> this.targetEntity.getEquipment().getItemInMainHand();
            case SLOT_OFF_HAND -> this.targetEntity.getEquipment().getItemInOffHand();
            case SLOT_STATS -> this.getStats();
            case SLOT_MISC_INFO -> this.getSkills();
            default -> null;
        };
    }

    private Component getMissingComponent(int slot) {
        Component name = this.targetEntity.name();
        return switch (slot) {
            case SLOT_HELMET -> name.append(ComponentUtils.create(" is not wearing a helmet", NamedTextColor.RED));
            case SLOT_CHESTPLATE -> name.append(ComponentUtils.create(" is not wearing a chestplate", NamedTextColor.RED));
            case SLOT_LEGGINGS -> name.append(ComponentUtils.create(" is not wearing leggings", NamedTextColor.RED));
            case SLOT_BOOTS -> name.append(ComponentUtils.create(" is not wearing boots", NamedTextColor.RED));
            case SLOT_MAIN_HAND -> name.append(ComponentUtils.create(" is not holding anything", NamedTextColor.RED));
            case SLOT_OFF_HAND -> name.append(ComponentUtils.create(" is not holding anything in their off hand", NamedTextColor.RED));
            case SLOT_STATS -> name.append(ComponentUtils.create(" does not have stats", NamedTextColor.RED));
            case SLOT_MISC_INFO -> name.append(ComponentUtils.create(" does not have information", NamedTextColor.RED));
            default -> ComponentUtils.EMPTY;
        };
    }

    private boolean shouldBePopulated(int slot) {
        return !this.getMissingComponent(slot).equals(ComponentUtils.EMPTY);
    }
}
