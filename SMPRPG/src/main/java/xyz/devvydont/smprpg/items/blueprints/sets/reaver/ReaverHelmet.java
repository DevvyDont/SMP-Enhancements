package xyz.devvydont.smprpg.items.blueprints.sets.reaver;

import io.papermc.paper.datacomponent.item.Equippable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.IEquippableOverride;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.List;

public class ReaverHelmet extends ReaverArmorSet implements IHeaderDescribable, IBreakableEquipment, IEquippableOverride, Listener {

    public ReaverHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Equippable getEquipmentOverride() {
        return IEquippableOverride.generateDefault(EquipmentSlot.HEAD);
    }

    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.EMPTY,
                AbilityUtil.getAbilityComponent("Necrotic (Passive)"),
                ComponentUtils.create("Resists ").append(ComponentUtils.create("-" + ReaverArmorSet.WITHER_RESIST + "%", NamedTextColor.GREEN)).append(ComponentUtils.create(" of wither damage")),
                ComponentUtils.create("(stacks multiplicatively)", NamedTextColor.DARK_GRAY)
        );
    }

    @Override
    public int getDefense() {
        return 45;
    }

    @Override
    public int getHealth() {
        return 5;
    }

    @Override
    public double getStrength() {
        return .25;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public int getMaxDurability() {
        return ReaverArmorSet.DURABILITY;
    }

}
