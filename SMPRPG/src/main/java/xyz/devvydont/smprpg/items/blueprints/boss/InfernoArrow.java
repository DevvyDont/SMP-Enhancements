package xyz.devvydont.smprpg.items.blueprints.boss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtil;

import java.util.ArrayList;
import java.util.List;

public class InfernoArrow extends CustomItemBlueprint implements Sellable {

    public InfernoArrow(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public List<Component> getDescriptionComponent(ItemMeta meta) {
        List<Component> components = new ArrayList<>(super.getDescriptionComponent(meta));
        components.add(ComponentUtil.getDefaultText("Used to summon an ")
                .append(ComponentUtil.getColoredComponent("Infernal Phoenix", NamedTextColor.DARK_PURPLE).decorate(TextDecoration.OBFUSCATED)));
        components.add(ComponentUtil.getDefaultText("when ")
                .append(ComponentUtil.getColoredComponent("shot in lava", NamedTextColor.GOLD).decorate(TextDecoration.OBFUSCATED))
                .append(ComponentUtil.getDefaultText(" in the "))
                .append(ComponentUtil.getColoredComponent("Nether", NamedTextColor.RED)));
        return components;
    }

    @Override
    public void updateMeta(ItemMeta meta) {
        super.updateMeta(meta);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public int getWorth() {
        return 20_000;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return 20_000;
    }
}
