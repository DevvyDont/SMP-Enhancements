package xyz.devvydont.smprpg.reforge.definitions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class OverheatingReforge extends ReforgeBase {

    public OverheatingReforge(ReforgeType type) {
        super(type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiersWithRarity(ItemRarity rarity) {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, SpicyReforge.getDamageBonus(rarity) - .05),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, SwiftReforge.getAttackSpeedBuff(rarity)),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, AgileReforge.getMovementSpeedBuff(rarity))
        );
    }


    @Override
    public List<Component> getDescription() {
        return List.of(
                ComponentUtils.create("Provides a").append(ComponentUtils.create(" SIGNIFICANT", NamedTextColor.GOLD)).append(ComponentUtils.create(" boost")),
                ComponentUtils.create("in attack damage, attack speed, and movement speed")
        );
    }

    @Override
    public int getPowerRating() {
        return 5;
    }
}
