package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomHeadBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ReforgeApplicator;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeUtil;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils;

import java.util.*;

public abstract class ReforgeStone extends CustomHeadBlueprint implements ReforgeApplicator {

    public static final ItemRarity DISPLAY_RARITY = ItemRarity.EPIC;

    public ReforgeStone(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public ReforgeBase getReforge() {
        return itemService.getReforge(getReforgeType());
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> lines = new ArrayList<>(super.getDescriptionComponent(meta));

        ReforgeBase reforge = getReforge();
        Component reforgeName = Component.text(getReforgeType().display() + " Reforge", NamedTextColor.BLUE);

        // First the description of what this item does.
        lines.add(ComponentUtil.getDefaultText("Combine this item with equipment in an"));
        lines.add(ComponentUtil.getDefaultText("anvil to apply the following reforge:"));
        lines.add(Component.empty());

        // The reforge tag that shows up on items when applied
        lines.add(reforgeName);
        lines.addAll(reforge.getDescription());
        lines.add(Component.empty());

        // Sample of statistics that get altered for a certain rarity
        // Is this attribute present on this item? If not skip it
        lines.add(Component.text("Stat Modifiers", NamedTextColor.BLUE));
        for (AttributeEntry entry : reforge.getAttributeModifiersWithRarity(DISPLAY_RARITY)){

            // There are three components to the string portion of the attribute number. The +/-, the amount, and percent.
            // The sign is a + if the amount is above 0. Otherwise, empty since the negative is put there for us.
            String sign = entry.getAmount() > 0 ? "+" : "";
            // The number is unchanged if this is an additive operation. If it isn't, x100 to make it show as a percentage.
            boolean forcePercent = AttributeUtil.forceAttributePercentage(entry.getAttribute());
            int number = entry.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER) && !forcePercent ? (int)entry.getAmount() : (int)(Math.round(entry.getAmount() * 100));
            // If this is a multiplicative operation, or we need to force the attribute to show as a percent, use percents.
            String percent = entry.getOperation().equals(AttributeModifier.Operation.ADD_NUMBER) && !forcePercent ? "" : "%";
            String numberSection = String.format("%s%d%s", sign, number, percent);

            AttributeWrapper wrapper = AttributeWrapper.ofAttribute(entry.getAttribute());
            NamedTextColor numberColor = wrapper.getAttributeType().equals(AttributeWrapper.AttributeType.SPECIAL) ? NamedTextColor.LIGHT_PURPLE :
                    wrapper.getAttributeType().equals(AttributeWrapper.AttributeType.POSITIVE) && number > 0 ? NamedTextColor.GREEN : NamedTextColor.RED;
            Component numberComponent = Component.text(numberSection).color(numberColor);
            lines.add(ComponentUtil.getDefaultText(AttributeWrapper.ofAttribute(entry.getAttribute()).getCleanName() + ": ").append(numberComponent));
        }
        lines.add(Component.text("Example bonuses for " + DISPLAY_RARITY.name() +" item are shown.", NamedTextColor.DARK_GRAY));
        lines.add(Component.text("Results vary based on item rarity!", NamedTextColor.DARK_GRAY));
        lines.add(Component.empty());
        lines.add(Component.text("Valid Equipment:", NamedTextColor.BLUE));
        for (ItemClassification clazz : getReforgeType().getAllowedItems())
            lines.add(ComponentUtil.getDefaultText("- " + MinecraftStringUtils.getTitledString(clazz.name())));

        return lines;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.REFORGE_STONE;
    }
}
