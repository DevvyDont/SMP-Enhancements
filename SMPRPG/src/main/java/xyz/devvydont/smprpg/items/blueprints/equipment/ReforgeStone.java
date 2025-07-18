package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.AttributeModifier;
import xyz.devvydont.smprpg.attribute.AttributeType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ReforgeApplicator;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class ReforgeStone extends CustomItemBlueprint implements ReforgeApplicator {

    public static final ItemRarity DISPLAY_RARITY = ItemRarity.EPIC;

    public ReforgeStone(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public ReforgeBase getReforge() {
        return itemService.getReforge(getReforgeType());
    }

    @Override
    public List<Component> getReforgeInformation() {
        List<Component> lines = new ArrayList<>();

        ReforgeBase reforge = getReforge();
        Component reforgeName = ComponentUtils.create(getReforgeType().display() + " Reforge", NamedTextColor.BLUE);

        // First the description of what this item does.
        lines.add(ComponentUtils.create("Combine this item with equipment in an"));
        lines.add(ComponentUtils.create("anvil to apply the following reforge:"));
        lines.add(ComponentUtils.EMPTY);

        // The reforge tag that shows up on items when applied
        lines.add(reforgeName);
        lines.addAll(reforge.getDescription());
        lines.add(ComponentUtils.EMPTY);

        // Sample of statistics that get altered for a certain rarity
        // Is this attribute present on this item? If not skip it
        lines.add(ComponentUtils.create("Stat Modifiers", NamedTextColor.BLUE));
        for (var entry : reforge.getAttributeModifiersWithRarity(DISPLAY_RARITY)){

            // There are three components to the string portion of the attribute number. The +/-, the amount, and percent.
            // The sign is a + if the amount is above 0. Otherwise, empty since the negative is put there for us.
            String sign = entry.getAmount() > 0 ? "+" : "";
            // The number is unchanged if this is an additive operation. If it isn't, x100 to make it show as a percentage.
            var option = AttributeUtil.getAttributeFormat(entry.getAttribute());
            var number = entry.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER) ? option.format(entry.getAmount()) : option.format(entry.getAmount()*100);
            // If this is a multiplicative operation, or we need to force the attribute to show as a percent, use percents.
            String percent = entry.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER) && !option.percentage() ? "" : "%";
            String numberSection = String.format("%s%s%s", sign, number, percent);

            var wrapper = entry.getAttribute();
            var numberColor = wrapper.Type.equals(AttributeType.SPECIAL) ? NamedTextColor.LIGHT_PURPLE :
                    wrapper.Type.equals(AttributeType.HELPFUL) && entry.getAmount() > 0 ? NamedTextColor.GREEN : NamedTextColor.RED;
            Component numberComponent = ComponentUtils.create(numberSection, numberColor);
            lines.add(ComponentUtils.create(wrapper.DisplayName + ": ").append(numberComponent));
        }
        lines.add(ComponentUtils.create("Example bonuses for " + DISPLAY_RARITY.name() +" item are shown.", NamedTextColor.DARK_GRAY));
        lines.add(ComponentUtils.create("Results vary based on item rarity!", NamedTextColor.DARK_GRAY));
        lines.add(ComponentUtils.EMPTY);
        lines.add(ComponentUtils.create("Valid Equipment:", NamedTextColor.BLUE));
        for (ItemClassification clazz : getReforgeType().getAllowedItems())
            lines.add(ComponentUtils.create("- " + MinecraftStringUtils.getTitledString(clazz.name())));

        return lines;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.REFORGE_STONE;
    }
}
