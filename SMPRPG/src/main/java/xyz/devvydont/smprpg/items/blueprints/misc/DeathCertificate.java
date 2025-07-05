package xyz.devvydont.smprpg.items.blueprints.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeathCertificate extends CustomItemBlueprint implements Listener, ISellable, IHeaderDescribable {

    private NamespacedKey locationKey = new NamespacedKey("smprpg", "location");
    private NamespacedKey playerKey = new NamespacedKey("smprpg", "player");
    private NamespacedKey timestampKey = new NamespacedKey("smprpg", "timestamp");

    public DeathCertificate(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private Component getEnvironmentComponent(World.Environment environment) {

        return switch (environment) {
            case THE_END -> ComponentUtils.create("The End", NamedTextColor.LIGHT_PURPLE);
            case NETHER -> ComponentUtils.create("the ").append(ComponentUtils.create("Nether", NamedTextColor.RED));
            case NORMAL -> ComponentUtils.create("the ").append(ComponentUtils.create("Overworld", NamedTextColor.DARK_GREEN));
            case CUSTOM -> ComponentUtils.create("Some unknown world", NamedTextColor.LIGHT_PURPLE);
        };

    }

    private Component getCoordinatesComponent(int[] location) {
        String str = String.format("%d %d %d", location[0], location[1], location[2]);
        return ComponentUtils.create(str, NamedTextColor.BLUE);
    }

    private int[] getPrimitiveLocation(Location location) {
        return new int[] {location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getEnvironment().ordinal()};
    }

    public boolean hasData(ItemMeta meta) {
        return meta.getPersistentDataContainer().has(locationKey) && meta.getPersistentDataContainer().has(playerKey) && meta.getPersistentDataContainer().has(timestampKey);
    }

    public String getWhoDied(ItemMeta meta) {
        return meta.getPersistentDataContainer().getOrDefault(playerKey, PersistentDataType.STRING, "UNKNOWN");
    }

    public int[] getPrimitiveLocation(ItemMeta meta) {
        return meta.getPersistentDataContainer().getOrDefault(locationKey, PersistentDataType.INTEGER_ARRAY, new int[]{0, 0, 0, 0});
    }

    public long getTimestamp(ItemMeta meta) {
        return meta.getPersistentDataContainer().getOrDefault(timestampKey, PersistentDataType.LONG, System.currentTimeMillis());
    }

    public void setData(ItemStack item, Player player) {

        if (player.getLastDeathLocation() == null)
            return;

        item.editMeta(meta -> {
            meta.getPersistentDataContainer().set(locationKey, PersistentDataType.INTEGER_ARRAY, getPrimitiveLocation(player.getLastDeathLocation()));
            meta.getPersistentDataContainer().set(playerKey, PersistentDataType.STRING, player.getName());
            meta.getPersistentDataContainer().set(timestampKey, PersistentDataType.LONG, System.currentTimeMillis());
        });
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {

        var meta = itemStack.getItemMeta();
        if (!hasData(meta))
            return List.of();

        String playerName = getWhoDied(meta);
        int[] location = getPrimitiveLocation(meta);
        World.Environment environment = World.Environment.values()[location[3]];
        long timestamp = getTimestamp(meta);

        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateString = formatter.format(date);

        return List.of(
                ComponentUtils.create(playerName, NamedTextColor.AQUA).append(ComponentUtils.create(" died in ")).append(getEnvironmentComponent(environment)),
                ComponentUtils.create("Coordinates: ").append(getCoordinatesComponent(location)),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Death occurred at: " + dateString + "EST", NamedTextColor.DARK_GRAY)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        ItemStack paper = generate();
        setData(paper, event.getPlayer());
        updateItemData(paper);

        event.getPlayer().getInventory().addItem(paper);
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return itemStack.getAmount();
    }
}
