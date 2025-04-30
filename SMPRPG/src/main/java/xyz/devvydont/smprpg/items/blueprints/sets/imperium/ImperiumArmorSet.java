package xyz.devvydont.smprpg.items.blueprints.sets.imperium;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class ImperiumArmorSet extends CustomArmorBlueprint implements ToolBreakable, Trimmable {


    public ImperiumArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .4),
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, .25)
        );
    }

    public abstract int getDefense();

    public abstract int getHealth();


    @Override
    public int getPowerRating() {
        return 65;
    }

    @Override
    public int getMaxDurability() {
        return 60_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.QUARTZ;
    }

}
