package xyz.devvydont.smprpg.items.blueprints.sets.araxys;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomArmorBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Craftable;
import xyz.devvydont.smprpg.items.interfaces.Dyeable;
import xyz.devvydont.smprpg.items.interfaces.ToolBreakable;
import xyz.devvydont.smprpg.items.interfaces.Trimmable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public abstract class AraxysArmorPiece extends CustomArmorBlueprint implements ToolBreakable, Dyeable, Trimmable, Craftable {

    public static final int POWER = 20;

    public AraxysArmorPiece(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return POWER;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(CustomItemType.ENCHANTED_STRING));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0x474f52);
    }

    @Override
    public int getMaxDurability() {
        return 35_000;
    }

    @Override
    public TrimMaterial getTrimMaterial() {
        return TrimMaterial.NETHERITE;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.DUNE;
    }
}
