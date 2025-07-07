package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.listeners.EntityDamageCalculatorService;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.*;
import static xyz.devvydont.smprpg.util.formatting.ComponentUtils.*;

public class SubmenuStatOverview extends MenuBase {

    private final LivingEntity target;
    private final DecimalFormat df = new DecimalFormat("#.##");

    public SubmenuStatOverview(@NotNull Player player, LivingEntity target, MenuBase parentMenu) {
        super(player, 6, parentMenu);
        this.target = target;
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
        this.setBorderBottom();
        this.setBackButton(49);

        var index = -1;
        for (var attribute : AttributeWrapper.values()) {
            var attributeInstance = AttributeService.getInstance().getAttribute(this.target, attribute);
            if (attributeInstance == null)
                continue;

            index = findNextEmpty(index);
            if (index == -1)
                return;

            this.setButton(index, generateItemDisplay(attribute), e -> handleClick(attribute));
        }

        this.setSlot(45, getHelp());
    }

    private ItemStack generateItemDisplay(AttributeWrapper attribute) {

        var item = ItemStack.of(resolveAttributeDisplay(attribute));
        var attributeInstance = AttributeService.getInstance().getAttribute(this.target, attribute);

        item.editMeta(meta -> {
            var displayName = merge(
                    create(attribute.DisplayName, GOLD), ComponentUtils.SPACE,
                    create("(" + attribute.Category.DisplayName + ")", NamedTextColor.DARK_GRAY)
            );
            meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));

            if (attributeInstance == null)
                return;

            var lore = new ArrayList<Component>();
            lore.add(ComponentUtils.EMPTY);
            lore.add(attribute.Description);
            lore.add(ComponentUtils.EMPTY);
            lore.add(merge(create("Base: "), create(df.format(attributeInstance.getBaseValue()), NamedTextColor.GREEN)));

            if (!attributeInstance.getModifiers().isEmpty()) {
                lore.add(ComponentUtils.EMPTY);
                lore.add(create("Active Modifiers:", NamedTextColor.YELLOW));
            }
            var modifiers = sortModifiers(attributeInstance.getModifiers());
            for (var modifier : modifiers) {
                lore.add(merge(
                        create(modifier.getName(), NamedTextColor.WHITE), ComponentUtils.SPACE,
                        resolveOperation(modifier.getOperation(), modifier.getAmount()), ComponentUtils.SPACE,
                        create("(" + modifier.getSlotGroup().toString().toLowerCase() + ")", NamedTextColor.DARK_GRAY)
                ));
            }
            lore.add(ComponentUtils.EMPTY);
            lore.add(merge(create("Final: "), create(df.format(attributeInstance.getValue()), NamedTextColor.GREEN)));

            // Append Defense/EHP if def stat
            if (attribute.equals(AttributeWrapper.DEFENSE)) {

                var hpAttr = AttributeService.getInstance().getAttribute(this.target, AttributeWrapper.HEALTH);
                var hp = hpAttr != null ? hpAttr.getValue() : 0;
                var def = SMPRPG.getService(EntityService.class).getEntityInstance(this.target).getDefense();
                var ehp = EntityDamageCalculatorService.calculateEffectiveHealth(hp, def);

                lore.add(ComponentUtils.EMPTY);
                lore.add(create("Effective Health: ", NamedTextColor.YELLOW));
                lore.add(merge(
                        create(String.format("%d ", (int)ehp), NamedTextColor.GREEN),
                        create(String.format("EHP=%dHP/%.2fDEF%%", (int)hp, EntityDamageCalculatorService.calculateResistancePercentage(def)*100), NamedTextColor.DARK_GRAY)
                ));
            }

            meta.lore(ComponentUtils.cleanItalics(lore));
        });

        return item;
    }

    private ItemStack getHelp() {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();

        meta.displayName(create("Attribute Guide", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        lore.add(ComponentUtils.EMPTY);

        lore.addAll(List.of(
                merge(create("Attributes", GOLD), create(" are the foundation of your")),
                merge(create("character's "), create(Symbols.POWER + "power", NamedTextColor.YELLOW), create(". You can modify your")),
                merge(create("attributes", GOLD), create(" in many ways, including but not limited to:")),
                merge(create(Symbols.POINT + " Equipping "), create("armor", WHITE)),
                merge(create(Symbols.POINT + " Holding "), create("tools/equipment", BLUE)),
                merge(create(Symbols.POINT + " Leveling up "), create("skills", AQUA)),
                merge(create(Symbols.POINT + " utilizing equipment "), create("reforges", GOLD)),
                merge(create(Symbols.POINT + " augmenting equipment with "), create("enchantments", LIGHT_PURPLE)),
                merge(create(Symbols.POINT + " Summoning "), create("pets", GREEN)),
                EMPTY,
                merge(create("The individual effects that "), create("attributes", GOLD), create(" provide can")),
                merge(create("be found when hovering over items in your inventory.")),
                merge(create("The way that attribute "), create("modifiers", AQUA), create(" display and apply")),
                merge(create("can appear misleading and/or confusing due to the nature of how "), create("modifiers", AQUA)),
                merge(create("work. "), create("There are 3 types of modifiers that are applied in the following order:")),
                merge(create(Symbols.RIGHT_ARROW + " Additive:", GREEN), create(" adds to your "), create("base value", BLUE), create(" (ex. +15)", DARK_GRAY)),
                merge(create(Symbols.RIGHT_ARROW + " Scalar:", GREEN), create(" applies a single additive multiplier to base value + additive modifiers"), create(" (ex. +25%)", DARK_GRAY)),
                merge(create(Symbols.RIGHT_ARROW + " Multiplicative:", GREEN), create(" multiplies the final value after all other modifiers"), create(" (ex. x2)", DARK_GRAY)),
                EMPTY,
                merge(create("The most important distinction is that "), create("scalar modifiers", GREEN), create(" stack "), create("additively", GREEN)),
                merge(create("while "), create("multiplicative modifiers", GREEN), create(" stack "), create("multiplicatively!", LIGHT_PURPLE)),
                EMPTY,
                create("For example, if all 4 of your armor pieces have '+25% scalar modifiers'", DARK_GRAY),
                create("then you will have a net +100% (or x2) of that specific attribute!", DARK_GRAY),
                create("If they were instead '+25% multiplicative modifiers', you would then", DARK_GRAY),
                create("net an increase by about +144% (or ~2.44x) instead.", DARK_GRAY),
                create("The mathematical difference is due to the nature of what results from:", DARK_GRAY),
                create("value * (1 + .25 + .25 + ...)) vs. (value * 1.25 * 1.25 * ...)", DARK_GRAY)

        ));

        meta.lore(ComponentUtils.cleanItalics(lore));

        paper.setItemMeta(meta);
        return paper;
    }

    private void handleClick(AttributeWrapper attribute) {

    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        event.titleOverride(merge(
                create("Stat Overview"),
                create(" WORK IN PROGRESS", NamedTextColor.RED, TextDecoration.BOLD)
        ));
    }

    @Override
    protected void handleInventoryClicked(InventoryClickEvent event) {
        this.playInvalidAnimation();
        event.setCancelled(true);
    }

    private Material resolveAttributeDisplay(AttributeWrapper attribute) {
        return switch (attribute){
            case LUCK -> Material.EMERALD;
            case ARMOR -> Material.NETHERITE_CHESTPLATE;
            case STRENGTH -> Material.DIAMOND_SWORD;
            case MINING_EFFICIENCY -> Material.IRON_PICKAXE;
            case MINING_SPEED -> Material.GOLDEN_PICKAXE;
            case SCALE -> Material.LADDER;
            case HEALTH -> Material.APPLE;
            case DEFENSE -> Material.IRON_CHESTPLATE;
            case SAFE_FALL -> Material.FEATHER;
            case BURNING_TIME -> Material.BLAZE_POWDER;
            case OXYGEN_BONUS -> Material.GLASS_BOTTLE;
            case INTELLIGENCE -> Material.POTION;
            case ABSORPTION -> Material.GOLDEN_APPLE;
            case SNEAKING_SPEED -> Material.CHAINMAIL_LEGGINGS;
            case ATTACK_SPEED -> Material.CLOCK;
            case MOVEMENT_SPEED -> Material.SUGAR;
            case JUMP_HEIGHT -> Material.RABBIT_FOOT;
            case GRAVITY -> Material.WIND_CHARGE;
            case SWEEPING -> Material.NETHERITE_HOE;
            case COMBAT_REACH -> Material.SPYGLASS;
            case MINING_REACH -> Material.BRUSH;
            case MINING_FORTUNE -> Material.DIAMOND;
            case FARMING_FORTUNE -> Material.WHEAT;
            case WOODCUTTING_FORTUNE -> Material.OAK_LOG;
            case FISHING_RATING -> Material.FISHING_ROD;
            case FISHING_CREATURE_CHANCE -> Material.DROWNED_SPAWN_EGG;
            case FISHING_TREASURE_CHANCE -> Material.GOLD_BLOCK;
            case STEP -> Material.QUARTZ_SLAB;
            case UNDERWATER_MINING -> Material.TURTLE_HELMET;
            case WATER_MOVEMENT -> Material.LEATHER_BOOTS;
            case REGENERATION -> Material.GLISTERING_MELON_SLICE;
            case FALL_DAMAGE_MULTIPLIER -> Material.SLIME_BLOCK;
            case EXPLOSION_KNOCKBACK_RESISTANCE -> Material.GUNPOWDER;
            case FOLLOW_RANGE -> Material.SPYGLASS;
            case FLYING_SPEED -> Material.ELYTRA;
            case MOVEMENT_EFFICIENCY -> Material.SOUL_SAND;
            case ATTACK_KNOCKBACK -> Material.STICK;
            case ZOMBIE_REINFORCEMENTS -> Material.ZOMBIE_HEAD;
            case KNOCKBACK_RESISTANCE -> Material.SHIELD;
            case CRITICAL_CHANCE -> Material.GOLDEN_AXE;
            case CRITICAL_DAMAGE -> Material.DIAMOND_AXE;
            default -> Material.BARRIER;
        };
    }

    private Component resolveOperation(AttributeModifier.Operation operation, double amount) {
        return switch (operation) {
            case ADD_NUMBER -> create(String.format("%s%s", amount >= 0 ? "+" : "-", df.format(amount)), amount >= 0 ? NamedTextColor.GREEN : NamedTextColor.RED);
            case ADD_SCALAR -> create(String.format("%s%s%%", amount >= 0 ? "+" : "-", df.format(amount*100)), amount >= 0 ? NamedTextColor.GREEN : NamedTextColor.RED);
            case MULTIPLY_SCALAR_1 -> create(String.format("x%s", df.format(amount+1)), amount >= 0 ? NamedTextColor.GREEN : NamedTextColor.RED);
        };
    }

    private Collection<AttributeModifier> sortModifiers(Collection<AttributeModifier> modifiers) {
        var result = new ArrayList<AttributeModifier>();
        result.addAll(modifiers.stream().filter(m -> m.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER)).toList());
        result.addAll(modifiers.stream().filter(m -> m.getOperation().equals(AttributeModifier.Operation.ADD_SCALAR)).toList());
        result.addAll(modifiers.stream().filter(m -> m.getOperation().equals(AttributeModifier.Operation.MULTIPLY_SCALAR_1)).toList());
        return result;
    }
}
