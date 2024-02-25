package me.devvy.smpparkour.items.blueprints;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.items.ParkourUtilityItemBlueprint;
import me.devvy.smpparkour.player.ParkourPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StartParkourBlueprint extends ParkourUtilityItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.displayName(Component.text("START!", NamedTextColor.GREEN));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String key() {
        return "start-parkour-button";
    }

    @Override
    public void onRightClick(Player player) {
        ParkourPlayer pp = SMPParkour.getInstance().getPlayerManager().getParkourPlayer(player);
        if (pp == null)
            return;

        // Add a cooldown to this item and the stop item because events are fucking stupid and fire so many times
        player.setCooldown(getVanillaType(), 20);
        player.setCooldown(SMPParkour.getInstance().getItemManager().getBlueprint(EndParkourBlueprint.class).getVanillaType(), 20);

        pp.startParkour();
    }
}
