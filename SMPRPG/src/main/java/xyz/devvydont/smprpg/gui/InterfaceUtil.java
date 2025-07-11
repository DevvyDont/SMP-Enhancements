package xyz.devvydont.smprpg.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Arrays;
import java.util.List;

public class InterfaceUtil {

    public static ItemStack getNamedItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name.decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getNamedItemWithDescription(Material material, Component name, List<Component> lines) {
        var item = getNamedItem(material, name);
        item.lore(ComponentUtils.cleanItalics(lines));
        return item;
    }

    public static ItemStack getNamedItemWithDescription(Material material, Component name, Component...lines) {
        return getNamedItemWithDescription(material, name, Arrays.asList(lines));
    }

}
