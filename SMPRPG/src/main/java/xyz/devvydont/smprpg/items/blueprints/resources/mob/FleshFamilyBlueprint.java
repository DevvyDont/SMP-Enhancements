package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.Material;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.items.interfaces.Edible;
import xyz.devvydont.smprpg.items.interfaces.IConsumable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;
import xyz.devvydont.smprpg.util.items.FoodUtil;

import java.util.ArrayList;
import java.util.List;

public class FleshFamilyBlueprint extends CustomCompressableBlueprint implements Edible, IConsumable {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(Material.ROTTEN_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_FLESH))
    );

    public FleshFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

    @Override
    public int getNutrition() {
        return switch (getCustomItemType()) {
            case ENCHANTED_FLESH -> 12;
            case PREMIUM_FLESH -> 4;
            default -> 2;
        };
    }

    @Override
    public float getSaturation() {
        return switch (getCustomItemType()) {
            case ENCHANTED_PORKCHOP -> 12;
            case PREMIUM_PORKCHOP -> 4;
            default -> 2;
        };
    }

    @Override
    public boolean canAlwaysEat() {
        return true;
    }

    @Override
    public Consumable getConsumableComponent() {

        var effects = new ArrayList<ConsumeEffect>();

        if (getCustomItemType().equals(CustomItemType.ENCHANTED_FLESH))
            effects.add(ConsumeEffect.applyStatusEffects(List.of(
                    new PotionEffect(PotionEffectType.ABSORPTION, 20*600, 2),
                    new PotionEffect(PotionEffectType.NIGHT_VISION, 20*600, 0)
            ), 1f));

        if (getCustomItemType().equals(CustomItemType.PREMIUM_FLESH))
            effects.add(ConsumeEffect.applyStatusEffects(List.of(
                    new PotionEffect(PotionEffectType.ABSORPTION, 20*600, 0)
            ), 1f));

        return Consumable.consumable()
                .consumeSeconds(4)
                .addEffects(effects)
                .build();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

}
