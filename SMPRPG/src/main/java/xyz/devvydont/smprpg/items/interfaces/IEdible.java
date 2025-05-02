package xyz.devvydont.smprpg.items.interfaces;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public interface IEdible extends IConsumable {

    int getNutrition();
    float getSaturation();
    boolean canAlwaysEat();

    /**
     * Generates a section that is suitable to use for a chat/lore component for an item that implements this.
     * @param edible The item to generate a component for.
     */
    static List<Component> generateEdibilityComponent(IEdible edible) {

        var lore = new ArrayList<Component>();

        // Start with the header. Letting them know this is food and how long it takes to eat.
        lore.add(ComponentUtils.merge(
                ComponentUtils.create("When eaten: ", NamedTextColor.GOLD),
                ComponentUtils.create(String.format("(%.1fs)", edible.getConsumableComponent().consumeSeconds()), NamedTextColor.DARK_GRAY)
        ));

        // Now, nutrition and saturation.
        if (edible.getNutrition() != 0)
            lore.add(ComponentUtils.merge(
                ComponentUtils.create("* Nutrition: "),
                ComponentUtils.create("+" + edible.getNutrition(), NamedTextColor.GOLD)
            ));

        if (edible.getSaturation() != 0)
            lore.add(ComponentUtils.merge(
                ComponentUtils.create("* Saturation: "),
                ComponentUtils.create("+" + edible.getSaturation(), NamedTextColor.YELLOW)
            ));

        // Effects if they are present.
        if (!edible.getConsumableComponent().consumeEffects().isEmpty()) {
            lore.add(ComponentUtils.create("Additional Effects: "));

            // Loop through every effect. Depending on the type of interface that it implements, explain what it does.
            for (var effect : edible.getConsumableComponent().consumeEffects())
                lore.addAll(IConsumable.generateEffectComponent(effect));
        }

        lore.add(ComponentUtils.create(edible.canAlwaysEat() ? "Can be eaten any time" : "Can only eat when hungry", NamedTextColor.DARK_GRAY));
        return lore;
    }

    static IEdible fromVanillaData(FoodProperties foodProperties, Consumable consumable) {
        return new IEdible() {
            @Override
            public int getNutrition() {
                return foodProperties.nutrition();
            }

            @Override
            public float getSaturation() {
                return foodProperties.saturation();
            }

            @Override
            public boolean canAlwaysEat() {
                return foodProperties.canAlwaysEat();
            }

            @Override
            public Consumable getConsumableComponent() {
                return consumable;
            }
        };
    }
}
