package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.inventory.meta.components.FoodComponent;

public interface Edible {

    int getNutrition();
    float getSaturation();
    boolean canAlwaysEat();
}
