package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeCategory;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.ChatService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.tasks.PlaytimeTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.*;

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

    private static final Set<AttributeWrapper> ATTRIBUTES_TO_SHOW = Set.of(
            AttributeWrapper.HEALTH,
            AttributeWrapper.DEFENSE,
            AttributeWrapper.STRENGTH,
            AttributeWrapper.ATTACK_SPEED,
            AttributeWrapper.INTELLIGENCE,
            AttributeWrapper.MOVEMENT_SPEED,
            AttributeWrapper.LUCK
    );

    private final LivingEntity targetEntity;
    private final EntityService entityService;

    public InterfaceStats(SMPRPG plugin, Player owner, LivingEntity targetPlayer) {
        super(owner, 6);
        this.targetEntity = targetPlayer;
        this.entityService = SMPRPG.getService(EntityService.class);
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        // Prepare the inventory

        var name = this.targetEntity instanceof Player castedPlayer ?
                SMPRPG.getService(ChatService.class).getPlayerDisplay(castedPlayer) :
                targetEntity.name();

        event.titleOverride(merge(
                create("Statistics Viewer (", NamedTextColor.BLACK),
                name,
                create(")", NamedTextColor.BLACK)
        ));

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

            this.setButton(SLOT_STATS, getStats(), e -> this.openSubMenu(new SubmenuStatOverview(this.player, this.targetEntity, this)));
        }

        if (this.isPlayer())
            this.setSlot(SLOT_STATS - 1, getInfo());
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
        var entity = this.entityService.getEntityInstance(this.targetEntity);

        meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(create("'s stats")).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);

        // Add the power rating
        lore.add(create("Power Rating: ", NamedTextColor.GOLD).append(create(Symbols.POWER + entity.getLevel(), NamedTextColor.YELLOW)));
        lore.add(ComponentUtils.EMPTY);

        var hpAttr = AttributeService.getInstance().getAttribute(this.targetEntity, AttributeWrapper.HEALTH);
        var hp = hpAttr != null ? hpAttr.getValue() : 0;
        var def = entity.getDefense();
        var ehp = EntityDamageCalculatorService.calculateEffectiveHealth(hp, def);

        for (var wrapper : AttributeWrapper.values()) {

            // We can skip attributes we don't have
            var attribute = AttributeService.getInstance().getAttribute(this.targetEntity, wrapper);
            if (attribute == null)
                continue;

            // Skip an attribute if it isn't whitelisted to show up
            if (!ATTRIBUTES_TO_SHOW.contains(wrapper))
                continue;

            var attributeValue = attribute.getValue();

            NamedTextColor numberColor = NamedTextColor.DARK_GRAY;
            double baseAttributeValue = attribute.getBaseValue();
            if (attributeValue > baseAttributeValue)
                numberColor = NamedTextColor.GREEN;
            if (attributeValue < baseAttributeValue)
                numberColor = NamedTextColor.RED;

            var attributeNameColor = NamedTextColor.GOLD;
            if (wrapper.Category.equals(AttributeCategory.SPECIAL))
                attributeNameColor = NamedTextColor.LIGHT_PURPLE;

            var deltaDiff = (baseAttributeValue - attributeValue) / baseAttributeValue * 100 * -1;
            Component deltaPercentComponent = create(String.format(" (%s%.2f%%)",deltaDiff > 0 ? "+" : "", deltaDiff), deltaDiff > 0 ? NamedTextColor.AQUA : NamedTextColor.DARK_RED);
            if (deltaDiff == 0 || Double.isNaN(deltaDiff) || Double.isInfinite(deltaDiff))
                deltaPercentComponent = ComponentUtils.EMPTY;

            lore.add(create(wrapper.DisplayName + ": ", attributeNameColor).append(create(String.format("%.2f", attributeValue), numberColor)).append(deltaPercentComponent));

            // Append Defense/EHP if def stat
            if (wrapper.equals(AttributeWrapper.DEFENSE)) {
                lore.add(merge(
                    create("- Effective Health: ", NamedTextColor.YELLOW),
                    create(String.format("%d ", (int)ehp), NamedTextColor.GREEN),
                    create(String.format("EHP=%dHP/%.2fDEF%%", (int)hp, EntityDamageCalculatorService.calculateResistancePercentage(def)*100), NamedTextColor.DARK_GRAY)
                ));
            }

        }

        lore.add(EMPTY);
        lore.add(create("Click to get a more detailed breakdown!", NamedTextColor.YELLOW));
        meta.lore(ComponentUtils.cleanItalics(lore));
        skull.setItemMeta(meta);
        return skull;
    }

    private List<Component> getSkillDisplay(LeveledPlayer player) {
        List<Component> display = new ArrayList<>();
        for (SkillInstance skill : player.getSkills())
            display.add(ComponentUtils.merge(
                    ComponentUtils.create(skill.getType().getDisplayName() + " " + skill.getLevel(), NamedTextColor.GOLD),
                    ComponentUtils.create(" - "),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getExperienceProgress()), NamedTextColor.GREEN),
                    ComponentUtils.create("/"),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getNextExperienceThreshold()), NamedTextColor.GRAY),
                    ComponentUtils.create(" ("),
                    ComponentUtils.create(MinecraftStringUtils.formatNumber(skill.getExperience()) + "XP", NamedTextColor.DARK_GRAY),
                    ComponentUtils.create(")")
            ));
        display.add(ComponentUtils.EMPTY);
        display.add(create("Skill Average: ", NamedTextColor.GOLD).append(create(String.format("%.2f", player.getAverageSkillLevel()), NamedTextColor.GREEN)));
        return display;
    }

    private String formatTime(long minutes, boolean includeDays) {

        var hours = minutes / 60;
        var onlyMinutes = minutes % 60;
        var days = hours / 24;

        // If multiple days are present, don't include minutes.
        if (includeDays && days > 0)
            return String.format("%dd%dh", days, hours % 24);

        // If we only care about hours, return 12h34m.
        if (hours > 0)
            return String.format("%dh%dm", hours, onlyMinutes);

        // Otherwise, just minutes.
        return String.format("%d minutes", onlyMinutes);
    }

    private ItemStack getInfo() {
        ItemStack paper = new ItemStack(Material.PAPER);
        var player = SMPRPG.getService(EntityService.class).getPlayerInstance(this.getPlayer());
        var timePlayed = formatTime(PlaytimeTracker.getPlaytime(player.getPlayer()), false);
        // Calculate how old this player is. Take the ms difference and convert it to minutes.
        var ageMs = System.currentTimeMillis() - PlaytimeTracker.getFirstSeen(player.getPlayer());
        var age = formatTime(ageMs / 1000 / 60, true);

        paper.editMeta(meta -> {
            meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(create("'s Information")).decoration(TextDecoration.ITALIC, false));
            meta.lore(ComponentUtils.cleanItalics(List.of(
                    EMPTY,
                    merge(create("Profile type: "), create(player.getDifficulty().Display, player.getDifficulty().Color)),
                    EMPTY,
                    merge(create("Playtime: "), create(timePlayed, NamedTextColor.GREEN)),
                    merge(create("First Seen: "), create(age + " ago", NamedTextColor.GREEN))
            )));
        });

        return paper;
    }

    private ItemStack getSkills() {
        ItemStack paper = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(create("'s Skills")).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);

        if (this.isPlayer())
            lore.addAll(getSkillDisplay(this.entityService.getPlayerInstance(this.getPlayer())));
        else
            lore.add(create("Only players have skills!", NamedTextColor.RED));
        meta.lore(ComponentUtils.cleanItalics(lore));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        paper.setItemMeta(meta);
        return paper;
    }

    private ItemStack getInventory() {
        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta meta = chest.getItemMeta();

        meta.displayName(this.targetEntity.name().color(NamedTextColor.AQUA).append(create("'s Full Inventory")).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = List.of(
                ComponentUtils.EMPTY,
                merge(create("Click to view their "), create("inventory", NamedTextColor.GOLD), create(" and "), create("ender chest", NamedTextColor.LIGHT_PURPLE), create("!"))
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
            case SLOT_HELMET -> name.append(create(" is not wearing a helmet", NamedTextColor.RED));
            case SLOT_CHESTPLATE -> name.append(create(" is not wearing a chestplate", NamedTextColor.RED));
            case SLOT_LEGGINGS -> name.append(create(" is not wearing leggings", NamedTextColor.RED));
            case SLOT_BOOTS -> name.append(create(" is not wearing boots", NamedTextColor.RED));
            case SLOT_MAIN_HAND -> name.append(create(" is not holding anything", NamedTextColor.RED));
            case SLOT_OFF_HAND -> name.append(create(" is not holding anything in their off hand", NamedTextColor.RED));
            case SLOT_STATS -> name.append(create(" does not have stats", NamedTextColor.RED));
            case SLOT_MISC_INFO -> name.append(create(" does not have information", NamedTextColor.RED));
            default -> ComponentUtils.EMPTY;
        };
    }

    private boolean shouldBePopulated(int slot) {
        return !this.getMissingComponent(slot).equals(ComponentUtils.EMPTY);
    }
}
