package xyz.devvydont.smprpg.items.blueprints.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomFakeHelmetBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.attributes.AttributeWrapper;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameBreaker extends CustomFakeHelmetBlueprint {

    public GameBreaker(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> components = new ArrayList<>(super.getDescriptionComponent(meta));
        components.add(ComponentUtils.EMPTY);
        components.add(ComponentUtils.create("Rewarded for discovering a", NamedTextColor.GRAY));
        components.add(ComponentUtils.create("bug/exploit and reporting it ", NamedTextColor.GRAY));
        return components;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.HEALTH, -1)
        );
    }

    @Override
    public int getPowerRating() {
        return 1;
    }
}
