package xyz.devvydont.treasureitems.listeners;

import xyz.devvydont.treasureitems.events.RareItemDropEvent;
import xyz.devvydont.treasureitems.util.FormatUtil;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RareDropListener implements Listener {

    public RareDropListener() {
        xyz.devvydont.treasureitems.TreasureItems.getInstance().getServer().getPluginManager().registerEvents(this, xyz.devvydont.treasureitems.TreasureItems.getInstance());
    }

    @EventHandler
    public void onRareDrop(RareItemDropEvent event) {

        event.getItem().setGlowing(true);
        event.getItem().setCustomName(event.getItem().getItemStack().getItemMeta().getDisplayName());
        event.getItem().setCustomNameVisible(true);
        event.getItem().setUnlimitedLifetime(true);

        StringBuilder hoverText = new StringBuilder();
        hoverText.append(event.getItem().getItemStack().getItemMeta().getDisplayName() + "\n");
        for (String line : event.getItem().getItemStack().getItemMeta().getLore())
            hoverText.append(line).append("\n");

        TextComponent itemStackDisplay = new TextComponent(event.getItem().getItemStack().getItemMeta().getDisplayName());
        itemStackDisplay.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText.toString()).create()));

        BaseComponent[] msg = new ComponentBuilder("[").color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .append("!").color(net.md_5.bungee.api.ChatColor.YELLOW)
                .append("] ").color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .append("RARE DROP!!! ").color(net.md_5.bungee.api.ChatColor.GOLD).bold(true)
                .append(event.getPlayer().getName()).reset().color(net.md_5.bungee.api.ChatColor.AQUA)
                .append(" found a ").color(net.md_5.bungee.api.ChatColor.GRAY)
                .append(itemStackDisplay)
                .append(" from ").reset().color(net.md_5.bungee.api.ChatColor.GRAY)
                .append(event.getReason()).color(net.md_5.bungee.api.ChatColor.GRAY)
                .append("!").color(net.md_5.bungee.api.ChatColor.GRAY)
                .append(" (" + ((int)(event.getChance() * 100000)) / 1000f + "% chance!)").color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .create();

        for (Player p : Bukkit.getOnlinePlayers())
            p.spigot().sendMessage(msg);

        FormatUtil.spawnFireworksInstantly(event.getItem().getLocation(), Color.PURPLE);

        for (Player p : Bukkit.getOnlinePlayers())
            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);

    }
}
