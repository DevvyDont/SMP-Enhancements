package xyz.devvydont.smprpg.items.blueprints.sets.reaver;

import io.papermc.paper.datacomponent.item.Equippable;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.IEquippableOverride;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.services.ItemService;

public class ReaverHelmet extends ReaverArmorSet implements IHeaderDescribable, IBreakableEquipment, IEquippableOverride, Listener {

    public ReaverHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Equippable getEquipmentOverride() {
        return IEquippableOverride.generateDefault(EquipmentSlot.HEAD);
    }

    @Override
    public int getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.NETHERITE_HELMET)+5;
    }

    @Override
    public int getHealth() {
        return 12;
    }

    @Override
    public double getStrength() {
        return ItemArmor.getDamageFromMaterial(Material.NETHERITE_HELMET)*2;
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
