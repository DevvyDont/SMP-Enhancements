package xyz.devvydont.smprpg.items.blueprints.sets.smite;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimPattern;
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

public abstract class SmiteArmorSet extends CustomArmorBlueprint implements ToolBreakable, Trimmable {

    public SmiteArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getDefense()),
                new AdditiveAttributeEntry(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE, .25),
                new ScalarAttributeEntry(AttributeWrapper.BURNING_TIME, -.2),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .1)
        );
    }

    public abstract int getDefense();

    @Override
    public int getPowerRating() {
        return 15;
    }

    @Override
    public int getMaxDurability() {
        return 4_000;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RAISER;
    }
}
