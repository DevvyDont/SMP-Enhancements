package xyz.devvydont.smprpg.items.blueprints.sets.unstable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class UnstableArmorSet extends CustomArmorBlueprint implements Trimmable, ToolBreakable {

    public UnstableArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private int randomInt(int start, int end) {
        return (int) (Math.random() * (end-start) + start);
    }

    private float randomFloat(float start, float end) {
        return (float) (Math.random() * (end-start) + start);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, (int)(randomInt(50, 500) * getStatMultiplier())),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, (int)(randomInt(30, 400) * getStatMultiplier())),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, randomFloat(.05f, .3f) * getStatMultiplier()),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, randomFloat(.02f, .3f) * getStatMultiplier())
        );
    }

    public abstract double getStatMultiplier();

    @Override
    public List<Component> getFooterComponent(ItemMeta meta) {
        List<Component> footer = new ArrayList<>(super.getFooterComponent(meta));
        footer.add(Component.empty());
        Component lore = ComponentUtil.getDefaultText("Stats ").append(ComponentUtil.getColoredComponent("randomly", NamedTextColor.LIGHT_PURPLE)).append(ComponentUtil.getDefaultText(" shuffle!"));
        footer.add(lore);
        return footer;
    }

    @Override
    public int getPowerRating() {
        return 55;
    }


    @Override
    public int getMaxDurability() {
        return 20_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

}
