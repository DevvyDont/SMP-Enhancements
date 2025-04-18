package xyz.devvydont.smprpg.items.blueprints.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IConsumable;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.items.interfaces.Sellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.List;

public class ChiliPepper extends CustomItemBlueprint implements Edible, Sellable, IConsumable {

    public ChiliPepper(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getNutrition() {
        return 2;
    }

    @Override
    public float getSaturation() {
        return 6;
    }

    @Override
    public int getWorth() {
        return 30;
    }

    @Override
    public int getWorth(ItemMeta meta) {
        return getWorth();
    }

    @Override
    public Consumable getConsumableComponent() {
        return Consumable.consumable()
                .consumeSeconds(.8f)
                .addEffect(ConsumeEffect.applyStatusEffects(List.of(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*45, 0, true, true)), .2f))
                .build();
    }

    @Override
    public boolean canAlwaysEat() {
        return false;
    }
}
