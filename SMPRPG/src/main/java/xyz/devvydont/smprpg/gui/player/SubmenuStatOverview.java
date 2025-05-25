package xyz.devvydont.smprpg.gui.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;

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
        this.setBorderBottom();
        this.setBackButton(49);

        var index = -1;
        for (var attribute : AttributeWrapper.values()) {
            var attributeInstance = AttributeService.getInstance().getAttribute(this.player, attribute);
            if (attributeInstance == null)
                continue;

            index = findNextEmpty(index);
            if (index == -1)
                return;

            this.setButton(index, generateItemDisplay(attribute), e -> handleClick(attribute));
        }
    }

    private ItemStack generateItemDisplay(AttributeWrapper attribute) {

        var item = ItemStack.of(resolveAttributeDisplay(attribute));
        var attributeInstance = AttributeService.getInstance().getAttribute(this.player, attribute);

        item.editMeta(meta -> {
            var displayName = ComponentUtils.merge(
                    ComponentUtils.create(attribute.DisplayName, NamedTextColor.GOLD), ComponentUtils.SPACE,
                    ComponentUtils.create("(" + attribute.Category.DisplayName + ")", NamedTextColor.DARK_GRAY)
            );
            meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));

            if (attributeInstance == null)
                return;

            var lore = new ArrayList<Component>();
            lore.add(ComponentUtils.merge(ComponentUtils.create("Base: "), ComponentUtils.create("" + attributeInstance.getBaseValue(), NamedTextColor.GREEN)));

            if (!attributeInstance.getModifiers().isEmpty()) {
                lore.add(ComponentUtils.EMPTY);
                lore.add(ComponentUtils.create("Active Modifiers:", NamedTextColor.YELLOW));
            }
            var modifiers = sortModifiers(attributeInstance.getModifiers());
            for (var modifier : modifiers) {
                lore.add(ComponentUtils.merge(
                        ComponentUtils.create(modifier.getName(), NamedTextColor.WHITE), ComponentUtils.SPACE,
                        resolveOperation(modifier.getOperation(), modifier.getAmount()), ComponentUtils.SPACE,
                        ComponentUtils.create("(" + modifier.getSlotGroup().toString().toLowerCase() + ")", NamedTextColor.DARK_GRAY)
                ));
            }
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.merge(ComponentUtils.create("Final: "), ComponentUtils.create("" + attributeInstance.getValue(), NamedTextColor.GREEN)));
            meta.lore(ComponentUtils.cleanItalics(lore));
        });

        return item;
    }

    private void handleClick(AttributeWrapper attribute) {

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
            default -> Material.BARRIER;
        };
    }

    private Component resolveOperation(AttributeModifier.Operation operation, double amount) {
        return switch (operation) {
            case ADD_NUMBER -> ComponentUtils.create("+" + amount, NamedTextColor.GREEN);
            case ADD_SCALAR -> ComponentUtils.create("+" + amount + "%", NamedTextColor.GREEN);
            case MULTIPLY_SCALAR_1 -> ComponentUtils.create("x" + (amount+1), NamedTextColor.GREEN);
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
