package xyz.devvydont.smprpg.items.blueprints.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.List;

public class CottonCandy extends CustomItemBlueprint implements IEdible, ISellable {

    public CottonCandy(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return 25 * itemStack.getAmount();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public int getNutrition() {
        return 4;
    }

    @Override
    public float getSaturation() {
        return 2;
    }

    @Override
    public boolean canAlwaysEat() {
        return false;
    }

    @Override
    public Consumable getConsumableComponent() {
        return Consumable.consumable()
                .consumeSeconds(.8f)
                .addEffect(ConsumeEffect.applyStatusEffects(List.of(new PotionEffect(PotionEffectType.JUMP_BOOST, 20*45, 0, true, true)), .2f))
                .build();
    }
}
