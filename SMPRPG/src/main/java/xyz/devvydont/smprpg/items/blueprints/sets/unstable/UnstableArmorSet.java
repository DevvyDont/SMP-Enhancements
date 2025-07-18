package xyz.devvydont.smprpg.items.blueprints.sets.unstable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable;
import xyz.devvydont.smprpg.items.interfaces.ITrimmable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class UnstableArmorSet extends CustomAttributeItem implements ITrimmable, IBreakableEquipment, IFooterDescribable {

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
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, (int)(randomInt(-100, 500) * getStatMultiplier())),
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, (int)(randomInt(-100, 500) * getStatMultiplier())),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, randomFloat(-.1f, .75f) * getStatMultiplier()),
                new ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, randomFloat(-.2f, .3f) * getStatMultiplier())
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.ARMOR;
    }

    public abstract double getStatMultiplier();

    @Override
    public List<Component> getFooter(ItemStack item) {
        var footer = new ArrayList<Component>();
        Component lore = ComponentUtils.create("Stats ").append(ComponentUtils.create("randomly", NamedTextColor.LIGHT_PURPLE)).append(ComponentUtils.create(" shuffle!"));
        footer.add(lore);
        return footer;
    }

    @Override
    public int getPowerRating() {
        return 40;
    }

    @Override
    public int getMaxDurability() {
        return randomInt(5_000, 30_000);
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.AMETHYST;
    }

}
