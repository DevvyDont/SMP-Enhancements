package xyz.devvydont.smprpg.items.blueprints.equipment;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.fishing.utils.TemperatureReading;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.List;

public class ThermometerBlueprint extends CustomItemBlueprint implements Listener, IModelOverridden, IHeaderDescribable {

    /**
     * Affects the number display of the temperature. When on, displays real value as well as the fake converted one.
     */
    private static final boolean DEBUG = false;

    // Constants for mapping
    private static final float MIN_MC_TEMP = -0.5f;
    private static final float MAX_MC_TEMP = 2.0f;
    private static final float MIN_C = -20f;  // freezing biomes
    private static final float MAX_C = 50f;   // nether heat

    public ThermometerBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                AbilityUtil.getAbilityComponent("Measure Temperature"),
                ComponentUtils.create("Use to measure the temperature"),
                ComponentUtils.create("of where you are currently standing!")
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.EQUIPMENT;
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.REDSTONE_TORCH;
    }

    /**
     * Converts Minecraft temperature to Celsius.
     * @param mcTemp The Minecraft temperature value (e.g., from Block#getTemperature()).
     * @return Temperature in degrees Celsius.
     */
    public static double toCelsius(double mcTemp) {
        double ratio = (mcTemp - MIN_MC_TEMP) / (MAX_MC_TEMP - MIN_MC_TEMP);
        return MIN_C + ratio * (MAX_C - MIN_C);
    }

    /**
     * Converts Minecraft temperature to Fahrenheit.
     * @param mcTemp The Minecraft temperature value (e.g., from Block#getTemperature()).
     * @return Temperature in degrees Fahrenheit.
     */
    public static double toFahrenheit(double mcTemp) {
        double celsius = toCelsius(mcTemp);
        return celsius * 9 / 5 + 32;
    }

    @EventHandler
    private void __onInteract(PlayerInteractEvent event) {

        if (!isItemOfType(event.getItem()))
            return;

        var rawTemp = event.getPlayer().getLocation().getBlock().getTemperature();
        var temp = TemperatureReading.fromValue(rawTemp);
        SMPRPG.getService(ActionBarService.class).addActionBarComponent(
                event.getPlayer(),
                ActionBarService.ActionBarSource.MISC,
                ComponentUtils.merge(
                        ComponentUtils.create("Temp: ", NamedTextColor.GOLD),
                        temp.Component,
                        ComponentUtils.SPACE,
                        ComponentUtils.create(String.format("(%.0f°F)", toFahrenheit(rawTemp)), NamedTextColor.DARK_GRAY),
                        DEBUG ? ComponentUtils.create(" " + rawTemp, NamedTextColor.RED) : ComponentUtils.EMPTY
                ),
                3
                );
    }
}
