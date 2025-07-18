package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import io.papermc.paper.datacomponent.item.Equippable;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.IEquippableOverride;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class AraxysHelmet extends AraxysArmorPiece implements IBreakableEquipment, IModelOverridden, IEquippableOverride {

    public AraxysHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.SPAWNER;
    }

    @Override
    public Equippable getEquipmentOverride() {
        return Equippable.equippable(EquipmentSlot.HEAD)
                .build();
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HEAD;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 190),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, 190),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .6),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 50),
                new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 200)
        );
    }

    @Override
    public int getMaxDurability() {
        return AraxysArmorPiece.DURABILITY;
    }
}
