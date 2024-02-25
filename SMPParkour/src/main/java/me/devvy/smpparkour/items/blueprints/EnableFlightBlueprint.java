package me.devvy.smpparkour.items.blueprints;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.items.ParkourUtilityItemBlueprint;
import me.devvy.smpparkour.player.ParkourPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnableFlightBlueprint extends ParkourUtilityItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.displayName(Component.text("Toggle flight!", NamedTextColor.AQUA));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String key() {
        return "flight-parkour-button";
    }

    @Override
    public void onRightClick(Player player) {
        ParkourPlayer pp = SMPParkour.getInstance().getPlayerManager().getParkourPlayer(player);
        if (pp == null)
            return;

        if (!pp.isPracticing())
            return;

        // Add a cooldown to this item and the start item because events are fucking stupid and fire so many times
        player.setCooldown(getVanillaType(), 20);

        pp.getPlayer().setAllowFlight(!pp.getPlayer().getAllowFlight());

        if (pp.getPlayer().getAllowFlight()) {
            pp.getPlayer().sendMessage(Component.text("Obtained the power of flight!", NamedTextColor.GREEN));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        }
        else {
            pp.getPlayer().sendMessage(Component.text("Lost the ability to fly!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_AMBIENT, 1, 1);
        }

    }
}
