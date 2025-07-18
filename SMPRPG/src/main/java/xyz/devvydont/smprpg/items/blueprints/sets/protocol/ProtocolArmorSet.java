package xyz.devvydont.smprpg.items.blueprints.sets.protocol;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ITrimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.Collection;
import java.util.List;

public class ProtocolArmorSet extends CustomAttributeItem implements ITrimmable, IHeaderDescribable {

    public static final int POWER = 45;
    public static final double CHESTPLATE_DEFENSE = 225;
    public static final double CHESTPLATE_HEALTH = 50;

    public static final double END_DAMAGE_REDUCTION = .15;

    public ProtocolArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private double getHealth() {
        return switch (this.getCustomItemType()) {
            case PROTOCOL_HELMET -> CHESTPLATE_HEALTH / 2 + 10;
            case PROTOCOL_CHESTPLATE -> CHESTPLATE_HEALTH;
            case PROTOCOL_LEGGINGS -> CHESTPLATE_HEALTH - 10;
            case PROTOCOL_BOOTS -> CHESTPLATE_HEALTH / 2;
            default -> 0;
        };
    }

    private double getDefense() {
        return switch (this.getCustomItemType()) {
            case PROTOCOL_HELMET -> CHESTPLATE_DEFENSE / 2 + 20;
            case PROTOCOL_CHESTPLATE -> CHESTPLATE_DEFENSE;
            case PROTOCOL_LEGGINGS -> CHESTPLATE_DEFENSE - 35;
            case PROTOCOL_BOOTS -> CHESTPLATE_DEFENSE / 2;
            default -> 0;
        };
    }

    private double getStrength() {
        return switch (this.getCustomItemType()) {
            case PROTOCOL_LEGGINGS, PROTOCOL_CHESTPLATE -> .4;
            case PROTOCOL_BOOTS, PROTOCOL_HELMET -> .35;
            default -> 0;
        };
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                AbilityUtil.getAbilityComponent("Protocol: Stabilization (Passive)"),
                ComponentUtils.create("Take ").append(ComponentUtils.create("-" + ((int)(END_DAMAGE_REDUCTION*100)) + "%", NamedTextColor.GREEN)).append(ComponentUtils.create(" damage while in "), ComponentUtils.create("The End", NamedTextColor.LIGHT_PURPLE)),
                ComponentUtils.create("(stacks!)", NamedTextColor.DARK_GRAY)
        );
    }

    /**
     * Determine what type of item this is.
     */
    @Override
    public ItemClassification getItemClassification() {
        return switch (this.getCustomItemType()) {
            case PROTOCOL_HELMET -> ItemClassification.HELMET;
            case PROTOCOL_CHESTPLATE -> ItemClassification.CHESTPLATE;
            case PROTOCOL_LEGGINGS -> ItemClassification.LEGGINGS;
            case PROTOCOL_BOOTS -> ItemClassification.BOOTS;
            default -> ItemClassification.ITEM;
        };
    }

    /**
     * What modifiers themselves will be contained on the item if there are no variables to affect them?
     *
     * @param item The item that is supposed to be holding the modifiers.
     * @return
     */
    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.HEALTH, this.getHealth()),
                AttributeEntry.additive(AttributeWrapper.DEFENSE, this.getDefense()),
                AttributeEntry.scalar(AttributeWrapper.STRENGTH, this.getStrength())
        );
    }

    /**
     * How much should we increase the power rating of an item if this container is present?
     *
     * @return
     */
    @Override
    public int getPowerRating() {
        return POWER;
    }

    /**
     * The slot that this item has to be worn in for attributes to kick in.
     *
     * @return
     */
    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.FLOW;
    }

    /**
     * When we take damage in the end while wearing this piece, decrease some damage.
     */
    @EventHandler
    public void __onTakeDamageInEnd(CustomEntityDamageByEntityEvent event) {

        if (!event.getDamaged().getWorld().getEnvironment().equals(World.Environment.THE_END))
            return;

        if (!(event.getDamaged() instanceof LivingEntity living))
            return;

        var equipment = living.getEquipment();
        if (equipment == null)
            return;

        var found = false;
        for (var piece : equipment.getArmorContents()) {
            if (isItemOfType(piece)) {
                found = true;
                break;
            }
        }

        if (!found)
            return;

        event.removeScalarDamage(END_DAMAGE_REDUCTION);
    }


}
