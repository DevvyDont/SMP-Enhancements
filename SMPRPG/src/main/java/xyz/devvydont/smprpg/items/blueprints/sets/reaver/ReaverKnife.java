package xyz.devvydont.smprpg.items.blueprints.sets.reaver;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.events.CustomItemDropRollEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.HeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReaverKnife extends CustomAttributeItem implements ToolBreakable, HeaderDescribable, Listener {

    public ReaverKnife(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public static final int WITHER_SKULL_BOOST = 5;

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        List<Component> components = new ArrayList<>();
        components.add(ComponentUtils.EMPTY);
        components.add(AbilityUtil.getAbilityComponent("Decapitator (Passive)"));
        components.add(ComponentUtils.create("Wither Skeleton Skull drops are ").append(ComponentUtils.create(WITHER_SKULL_BOOST + "x", NamedTextColor.GREEN)).append(ComponentUtils.create(" more common")));
        components.add(ComponentUtils.create("when killing ").append(ComponentUtils.create("Wither", NamedTextColor.DARK_RED)).append(ComponentUtils.create(" type enemies with this weapon")));
        return components;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 130),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.4)
        );
    }

    @Override
    public int getPowerRating() {
        return ReaverArmorSet.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return ReaverArmorSet.DURABILITY;
    }

    @EventHandler
    public void onRollWitherSkull(CustomItemDropRollEvent event) {

        if (event.getTool() == null || event.getTool().getType().equals(Material.AIR))
            return;

        if (!isItemOfType(event.getTool()))
            return;

        SMPItemBlueprint drop = itemService.getBlueprint(event.getDrop());
        if (drop.isCustom() || !event.getDrop().getType().equals(Material.WITHER_SKELETON_SKULL))
            return;

        event.setChance(event.getChance() * WITHER_SKULL_BOOST);
    }
}
