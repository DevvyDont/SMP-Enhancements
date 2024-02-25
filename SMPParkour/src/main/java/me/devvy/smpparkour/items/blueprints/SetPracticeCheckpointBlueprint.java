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

public class SetPracticeCheckpointBlueprint extends ParkourUtilityItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.displayName(Component.text("Set Waypoint!", NamedTextColor.AQUA));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String key() {
        return "setwaypoint-parkour-button";
    }

    @Override
    public void onRightClick(Player player) {
        ParkourPlayer pp = SMPParkour.getInstance().getPlayerManager().getParkourPlayer(player);
        if (pp == null)
            return;

        // Add a cooldown to this item and the stop item because events are fucking stupid and fire so many times
        player.setCooldown(getVanillaType(), 20);

        pp.setPracticeCheckpointOverride(player.getLocation());
        player.sendMessage(Component.text("Set a waypoint at your current location!", NamedTextColor.GREEN));
        pp.getPlayer().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
    }

}
