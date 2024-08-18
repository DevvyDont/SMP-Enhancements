package xyz.devvydont.smprpg.items.blueprints.sets.neptune;

import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public abstract class NeptuneArmorSet extends CustomArmorBlueprint implements ToolBreakable, Dyeable, Trimmable, Craftable {

    public static final int POWER_LEVEL = 40;
    public static final int OXYGEN_BONUS = 20;
    public static final int DURABILITY = 75_000;

    public NeptuneArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public boolean wantNerfedSellPrice() {
        return false;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealth()),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, getStrength()/100.0),
                new AdditiveAttributeEntry(AttributeWrapper.OXYGEN_BONUS, OXYGEN_BONUS)
        );
    }

    public abstract int getDefense();

    public abstract int getHealth();

    public abstract int getStrength();

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.WAYFINDER;
    }

    @Override
    public int getMaxDurability() {
        return DURABILITY;
    }

    @Override
    public int getPowerRating() {
        return POWER_LEVEL;
    }
}
