package me.devvy.smpparkour.items.blueprints;

import me.devvy.smpparkour.SMPParkour;
import me.devvy.smpparkour.checkpoints.Checkpoint;
import me.devvy.smpparkour.items.ParkourUtilityItemBlueprint;
import me.devvy.smpparkour.player.ParkourPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LastCheckpointParkourBlueprint extends ParkourUtilityItemBlueprint {

    @Override
    protected ItemStack constructBaseItemStack() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.displayName(Component.text("Last Checkpoint!", NamedTextColor.GOLD).decoration(TextDecoration.BOLD, false));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String key() {
        return "last-checkpoint-button";
    }

    @Override
    public void onRightClick(Player player) {
        ParkourPlayer pp = SMPParkour.getInstance().getPlayerManager().getParkourPlayer(player);
        if (pp == null)
            return;

        Checkpoint checkpoint = pp.getCurrentCheckpoint();
        if (checkpoint == null)
            return;

        player.setCooldown(getVanillaType(), 60);

        player.teleport(checkpoint.getSpawn());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.sendMessage(Component.text("Going to last checkpoint!", NamedTextColor.GRAY));
    }
}
